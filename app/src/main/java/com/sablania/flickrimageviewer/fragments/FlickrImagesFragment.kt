package com.sablania.flickrimageviewer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.sablania.baseandroidlibrary.BaseFragment
import com.sablania.flickrimageviewer.databinding.FragmentFlickrImagesBinding
import com.sablania.flickrimageviewer.viewModels.FlickrImagesViewModel

class FlickrImagesFragment : BaseFragment() {

    lateinit var binding: FragmentFlickrImagesBinding
    lateinit var viewModel: FlickrImagesViewModel

    companion object {
        val TAG = this::class.java.simpleName

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
//            rvImages.adapter = FlickrImagesAdapter()
            rvImages.layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initViewModel() {
        viewModel = getViewModelProvider(this).get(FlickrImagesViewModel::class.java)
    }

}
