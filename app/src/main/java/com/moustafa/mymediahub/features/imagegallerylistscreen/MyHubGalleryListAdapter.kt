package com.moustafa.mymediahub.features.imagegallerylistscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moustafa.mymediahub.R
import com.moustafa.mymediahub.models.PhotoInfo
import kotlinx.android.synthetic.main.item_gallery_image.view.*

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class ImageGalleryListAdapter :
    ListAdapter<PhotoInfo, ReposListViewHolder>(
        PHOTO_INFO_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposListViewHolder =
        ReposListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_image, parent, false)
        )

    override fun onBindViewHolder(holder: ReposListViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val PHOTO_INFO_COMPARATOR =
            object : DiffUtil.ItemCallback<PhotoInfo>() {
                override fun areContentsTheSame(oldItem: PhotoInfo, newItem: PhotoInfo): Boolean =
                    oldItem == newItem

                override fun areItemsTheSame(oldItem: PhotoInfo, newItem: PhotoInfo): Boolean =
                    oldItem.imageUrl == newItem.imageUrl

                override fun getChangePayload(oldItem: PhotoInfo, newItem: PhotoInfo): Any = Any()
            }
    }
}

class ReposListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(photoInfo: PhotoInfo?) {
        if (photoInfo != null) {
            Glide
                .with(itemView.imageViewPhoto)
                .load(photoInfo.imageUrl)
                .fitCenter()
                .into(itemView.imageViewPhoto)
        }
    }


}