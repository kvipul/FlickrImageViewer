package com.sablania.flickrimageviewer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sablania.baseandroidlibrary.BaseFragment
import com.sablania.baseandroidlibrary.EndlessRecyclerViewScrollListener
import com.sablania.baseandroidlibrary.hideKeyboard
import com.sablania.flickrimageviewer.R
import com.sablania.flickrimageviewer.adapters.FlickrImagesAdapter
import com.sablania.flickrimageviewer.databinding.FragmentFlickrImagesBinding
import com.sablania.flickrimageviewer.viewModels.FlickrImagesViewModel

class FlickrImagesFragment : BaseFragment() {

    private lateinit var endlessScrollListener: EndlessRecyclerViewScrollListener
    lateinit var binding: FragmentFlickrImagesBinding
    lateinit var viewModel: FlickrImagesViewModel
    lateinit var adapter: FlickrImagesAdapter
    private var currentPage = 1

    companion object {

        val TAG = this::class.java.simpleName
        private const val VISIBLE_THRESHOLD = 4

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
            adapter = FlickrImagesAdapter { clickType ->
                onListItemClicked(clickType)
            }
            rvImages.adapter = adapter
            val linearLayoutManager = LinearLayoutManager(context)
            rvImages.layoutManager = linearLayoutManager
            rvImages.addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )

            endlessScrollListener =
                object : EndlessRecyclerViewScrollListener(linearLayoutManager, VISIBLE_THRESHOLD) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        currentPage = page
                        if (currentPage != 1) loadMoreData() //for the first page, we'll call manually
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
            viewModel.getFlickrImagesLiveData().observe(viewLifecycleOwner, Observer {
                Log.i("vipul", "current page $currentPage" + Gson().toJson(it))

                if (currentPage == 1) {
                    adapter.setData(it.photos.photo)
                } else {
                    adapter.addData(it.photos.photo)
                }
            })
            viewModel.getErrorLiveData().observe(viewLifecycleOwner, Observer {
                Log.i("vipul", "current page $currentPage error $it")
                if (it) {
                    if (currentPage == 1) {
                        showListOrErrorLayout(true)
                        adapter.setBottomProgress(false, false)
                    } else {
                        showListOrErrorLayout(false)
                        adapter.setBottomProgress(false, true)
                    }
                } else {
                    showListOrErrorLayout(false)
                    adapter.setBottomProgress(false, false)
                }
            })
        }
    }

    private fun refreshData() {
        binding.apply {
            currentPage = 1
            tietSearch.hideKeyboard()
            endlessScrollListener.resetState()
            showListOrErrorLayout(false)
            adapter.clearData()
            adapter.setBottomProgress(showLoading = true)
            val searchText = tietSearch.text.toString()
            if (searchText.isEmpty()) {
                Toast.makeText(context!!, R.string.please_enter_text_to_search, Toast.LENGTH_LONG)
                    .show()
                return
            }
            viewModel.getFlickrImages(searchText, currentPage)
        }
    }

    private fun showListOrErrorLayout(showError: Boolean) {
        binding.apply {
            layoutApiErrorAndRetry.root.isVisible = showError
            rvImages.isVisible = !showError
        }
    }

    private fun loadMoreData() {
        binding.apply {
            rvImages.post { adapter.setBottomProgress(showLoading = true) }
            viewModel.loadMoreImages(currentPage)
        }
    }

    private fun onListItemClicked(clickType: String) {
        when (clickType) {
            FlickrImagesAdapter.IMAGE_ITEM_CLICKED -> {
            }
            FlickrImagesAdapter.LOAD_MORE_CLICKED -> {
                loadMoreData()
            }
        }
    }

}
