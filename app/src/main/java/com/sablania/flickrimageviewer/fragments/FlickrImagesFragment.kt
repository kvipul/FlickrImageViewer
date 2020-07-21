package com.sablania.flickrimageviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sablania.baseandroidlibrary.BaseFragment
import com.sablania.baseandroidlibrary.EndlessRecyclerViewScrollListener
import com.sablania.baseandroidlibrary.hideKeyboard
import com.sablania.baseandroidlibrary.openImageInGallery
import com.sablania.flickrimageviewer.R
import com.sablania.flickrimageviewer.adapters.FlickrImagesAdapter
import com.sablania.flickrimageviewer.databinding.FragmentFlickrImagesBinding
import com.sablania.flickrimageviewer.models.FlickrImage
import com.sablania.flickrimageviewer.viewModels.FlickrImagesViewModel

class FlickrImagesFragment : BaseFragment() {

    private lateinit var endlessScrollListener: EndlessRecyclerViewScrollListener
    lateinit var binding: FragmentFlickrImagesBinding
    lateinit var viewModel: FlickrImagesViewModel
    lateinit var adapter: FlickrImagesAdapter

    companion object {
        val TAG = this::class.java.simpleName
        private const val VISIBLE_THRESHOLD = 5

        fun newInstance(): FlickrImagesFragment = FlickrImagesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlickrImagesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        binding.apply {
            adapter = FlickrImagesAdapter { clickType, item ->
                onListItemClicked(clickType, item)
            }
            rvImages.adapter = adapter
            val layoutManager = GridLayoutManager(context!!, 3)
            rvImages.layoutManager = layoutManager
            rvImages.addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )

            endlessScrollListener =
                object : EndlessRecyclerViewScrollListener(layoutManager, VISIBLE_THRESHOLD) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        if (page != 1) viewModel.loadMoreImages() //for the first page, we'll call manually
                    }
                }
            rvImages.addOnScrollListener(endlessScrollListener)

            layoutApiErrorAndRetry.tvRetry.setOnClickListener {
                refreshData()
            }
            tietSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    refreshData()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun initViewModel() {
        binding.apply {
            viewModel =
                getViewModelProvider(this@FlickrImagesFragment).get(FlickrImagesViewModel::class.java)
            viewModel.getInitialLoadImagesLiveData().observe(viewLifecycleOwner, Observer {
                adapter.setData(it.photos.photo)
                binding.apply {
//                    rvImages.isVisible = true
                }
            })
            viewModel.getLoadMoreImagesLiveData().observe(viewLifecycleOwner, Observer {
                adapter.addData(it.photos.photo)
//                binding.rvImages.isVisible = true
            })
            viewModel.getFullPageErrorLiveData().observe(viewLifecycleOwner, Observer {
                binding.layoutApiErrorAndRetry.root.isVisible = it
            })
            viewModel.getInlineErrorLiveData().observe(viewLifecycleOwner, Observer {
                adapter.setBottomProgress(showLoading = false, showLoadMore = it)
            })
            viewModel.getProgressLiveData().observe(viewLifecycleOwner, Observer {
                adapter.setBottomProgress(showLoading = it, showLoadMore = false)
            })
            viewModel.getNoDataLiveData().observe(viewLifecycleOwner, Observer {
                binding.tvNoDataFound.isVisible = it
            })
            //First load
            viewModel.initialLoadImages("Random")
        }
    }

    private fun refreshData() {
        binding.apply {
            val searchText = tietSearch.text.toString()
            if (searchText.isEmpty()) {
                Toast.makeText(context!!, R.string.please_enter_text_to_search, Toast.LENGTH_LONG)
                    .show()
                return
            }
            tietSearch.hideKeyboard()
            endlessScrollListener.resetState()
            adapter.clearData()
            viewModel.initialLoadImages(searchText)
        }
    }

    private fun onListItemClicked(clickType: String, item: Any?) {
        when (clickType) {
            FlickrImagesAdapter.IMAGE_ITEM_CLICKED -> {
                context?.openImageInGallery((item as FlickrImage).getImageLarge())
            }
            FlickrImagesAdapter.LOAD_MORE_CLICKED -> viewModel.loadMoreImages()
        }
    }

}
