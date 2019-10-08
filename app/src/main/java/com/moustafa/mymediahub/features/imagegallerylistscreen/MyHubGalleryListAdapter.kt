package com.moustafa.mymediahub.features.imagegallerylistscreen

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.moustafa.mymediahub.R
import com.moustafa.mymediahub.models.PhotoInfo
import kotlinx.android.synthetic.main.item_gallery_image.view.*


/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class ImageGalleryListAdapter(val context: Context) :
    ListAdapter<PhotoInfo, ReposListViewHolder>(PHOTO_INFO_COMPARATOR),
    ListPreloader.PreloadModelProvider<PhotoInfo> {

    private val fullRequest by lazy {
        Glide.with(context).asDrawable()
    }


    private val thumbnailRequest by lazy {
        Glide.with(context).asDrawable()
            .override(context.resources.getDimensionPixelSize(R.dimen.grid_photo_side))
    }

    private val preloadRequest by lazy {
        fullRequest.clone().priority(Priority.HIGH)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposListViewHolder =
        ReposListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_image, parent, false),
            fullRequest,
            thumbnailRequest
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

    override fun getPreloadItems(position: Int): MutableList<PhotoInfo> =
        currentList.subList(position, position + 1)

    override fun getPreloadRequestBuilder(item: PhotoInfo): RequestBuilder<*>? {
        return preloadRequest.load(item)
    }
}

public class ReposListViewHolder(
    itemView: View,
    val fullRequest: RequestBuilder<Drawable>,
    val thumbnailRequest: RequestBuilder<Drawable>
) : RecyclerView.ViewHolder(itemView) {

    fun bind(photoInfo: PhotoInfo?) {

        if (photoInfo != null) {
            fullRequest
                .load(photoInfo.imageUrl)
                .thumbnail(thumbnailRequest.load(photoInfo.imageUrl))
                .into(itemView.imageViewPhoto)
        }
    }


}