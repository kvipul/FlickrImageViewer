package com.sablania.flickrimageviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sablania.baseandroidlibrary.BaseViewHolder
import com.sablania.flickrimageviewer.databinding.ItemImageBinding
import com.sablania.flickrimageviewer.models.FlickrImage

class FlickrImagesAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    val list = ArrayList<FlickrImage>()

    fun setData(photos: ArrayList<FlickrImage>) {
        list.clear()
        addData(photos)
    }

    fun addData(photos: ArrayList<FlickrImage>) {
        list.addAll(photos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return FlickrImagesViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position, list[position])
    }

    inner class FlickrImagesViewHolder(private val binding: ItemImageBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int, item: Any?) {
            val context = binding.root.context
            binding.apply {
                (item as FlickrImage).let {
                    Glide.with(context).load(getImageUrl(it)).into(image)
                }
            }
        }

        private fun getImageUrl(item: FlickrImage): String {
            return "https://farm${item.farm}.staticflickr.com/${item.server}/${item.id}_${item.secret}_m.jpg"
        }
    }
}
