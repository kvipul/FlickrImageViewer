package com.sablania.flickrimageviewer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sablania.baseandroidlibrary.BaseViewHolder
import com.sablania.flickrimageviewer.R
import com.sablania.flickrimageviewer.databinding.ItemImageBinding
import com.sablania.flickrimageviewer.databinding.ItemRecyclerViewBottomProgressBinding
import com.sablania.flickrimageviewer.models.FlickrImage

class FlickrImagesAdapter(private val onItemClick: (String, Any?) -> Unit) :
    RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        //item type
        const val LOAD_MORE_CLICKED = "LOAD_MORE_CLICKED"
        const val IMAGE_ITEM_CLICKED = "IMAGE_ITEM_CLICKED"

        private val VIEW_PROG = 0
        private val VIEW_IMAGE = 1
    }

    private val list = ArrayList<FlickrImage>()
    var showLoading = false
    var showLoadMore = false

    fun setData(photos: ArrayList<FlickrImage>) {
        list.clear()
        list.addAll(photos)
        notifyDataSetChanged()
    }

    fun addData(photos: ArrayList<FlickrImage>) {
        val prevSize = list.size
        list.addAll(photos)
        notifyItemRangeInserted(list.size, photos.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_IMAGE -> FlickrImagesViewHolder(
                ItemImageBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> ProgressViewHolder(
                ItemRecyclerViewBottomProgressBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
        }
    }

    //+1 for bottom progress view
    override fun getItemCount(): Int = list.size + 1

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position, if (position == itemCount - 1) null else list[position])
    }

    fun setBottomProgress(showLoading: Boolean = false, showLoadMore: Boolean = false) {
        this.showLoading = showLoading
        this.showLoadMore = showLoadMore
        notifyItemChanged(list.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) VIEW_PROG else VIEW_IMAGE
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    inner class FlickrImagesViewHolder(private val binding: ItemImageBinding) :
        BaseViewHolder(binding.root) {

        override fun onBind(position: Int, item: Any?) {
            val context = binding.root.context
            binding.apply {
                (item as FlickrImage).let {
                    //by default Glide caches images with DiskCacheStrategy.AUTOMATIC strategy, we can change it if needed
                    Glide.with(context).setDefaultRequestOptions(RequestOptions().apply {
                        placeholder(R.drawable.ic_baseline_image_24)
                        error(R.drawable.ic_baseline_error_outline_24)
                    }).load(item.getImageSquareThumbnail()).into(image)

                    itemView.setOnClickListener {
                        onItemClick.invoke(IMAGE_ITEM_CLICKED, item)
                    }
                }
            }
        }
    }

    inner class ProgressViewHolder(private val binding: ItemRecyclerViewBottomProgressBinding) :
        BaseViewHolder(binding.root) {

        override fun onBind(position: Int, item: Any?) {
            setProgress(showLoading, showLoadMore)
        }

        /**
         * only one of the below should be true at a time
         */
        fun setProgress(showLoading: Boolean = false, showLoadMore: Boolean = false) {
            binding.apply {
                if (showLoading) {
                    progressBar.visibility = View.VISIBLE
                    ivRetry.visibility = View.INVISIBLE //make view invisible instead of visible so that height of parent remain same
                } else if (showLoadMore) {
                    ivRetry.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE //make view invisible instead of visible so that height of parent remain same
                } else {
                    ivRetry.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                }

                ivRetry.setOnClickListener {
                    onItemClick.invoke(LOAD_MORE_CLICKED, null)
                }
            }
        }
    }
}
