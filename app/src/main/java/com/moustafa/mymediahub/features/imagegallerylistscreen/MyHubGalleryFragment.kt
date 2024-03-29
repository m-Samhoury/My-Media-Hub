package com.moustafa.mymediahub.features.imagegallerylistscreen

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.google.android.material.snackbar.Snackbar
import com.moustafa.mymediahub.R
import com.moustafa.mymediahub.base.BaseFragment
import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.network.StateMonitor
import com.moustafa.mymediahub.utils.Constants
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_my_hub_gallery.*
import kotlinx.android.synthetic.main.item_gallery_image.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

class MyHubGalleryFragment : BaseFragment(R.layout.fragment_my_hub_gallery) {

    private val myHubGalleryViewModel: MyHubGalleryViewModel by viewModel()

    private val imageGalleryListAdapter: ImageGalleryListAdapter by lazy {
        ImageGalleryListAdapter(context!!)
    }

    override fun setupViews(rootView: View) {
        configureRecyclerView()
        fabSubmitImage.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {


        PickImageDialog.build(
            PickSetup()
                .setButtonOrientation(LinearLayout.HORIZONTAL)
                .setIconGravity(Gravity.TOP)
        )
            .setOnPickResult { result ->
                cropImage(result.uri)
            }
            .setOnPickCancel {}.show(activity)
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(context?.cacheDir, "croppedtemp"))
        val options = UCrop.Options()
        options.setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        options.setActiveWidgetColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        options.setCropGridColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        options.setToolbarWidgetColor(Color.WHITE)

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(context!!, this)
    }

    private fun compressImageAndUpload(result: Uri) {
        myHubGalleryViewModel.viewModelScope.launch(Dispatchers.Main) {
            val compressedImage = myHubGalleryViewModel.compressImageAsync(context!!, result)
            if (compressedImage != null) {
                myHubGalleryViewModel.uploadImage(compressedImage)
            } else {
                showError(Exception(getString(R.string.message_error_compressing_image)))
            }
        }
    }

    private fun configureRecyclerView() {
        //efficient Recyclerview loading
        // @Link{https://github.com/bumptech/glide/blob/e86fd41e16aac1b95884494c1097417b4ab15a5a/samples/flickr/src/main/java/com/bumptech/glide/samples/flickr/FlickrPhotoGrid.java#L78}
        val photoSize = resources.getDimensionPixelOffset(R.dimen.grid_photo_side)
        val gridMargin = resources.getDimensionPixelOffset(R.dimen.grid_margin_value)
        val spanCount = resources.displayMetrics.widthPixels / (photoSize + 2 * gridMargin)
        val heightCount = resources.displayMetrics.heightPixels / photoSize
        val itemsPerPage = spanCount * heightCount
        val preloadSize = Constants.PRELOADED_ITEMS_NUMBER

        val gridLayoutManager = GridLayoutManager(context, spanCount)
        recyclerViewImagesList.layoutManager = gridLayoutManager

        recyclerViewImagesList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(gridMargin, gridMargin, gridMargin, gridMargin)
            }
        })

        recyclerViewImagesList.setRecyclerListener { holder ->
            val photoViewHolder = holder as ReposListViewHolder
            Glide.with(context!!).clear(photoViewHolder.itemView.imageViewPhoto)
        }

        recyclerViewImagesList.recycledViewPool.setMaxRecycledViews(0, itemsPerPage * 2)
        recyclerViewImagesList.setItemViewCacheSize(0)


        val preloadSizeProvider = FixedPreloadSizeProvider<PhotoInfo>(photoSize, photoSize)
        val preloader = RecyclerViewPreloader(
            Glide.with(this), imageGalleryListAdapter,
            preloadSizeProvider, preloadSize
        )
        recyclerViewImagesList.addOnScrollListener(preloader)
        recyclerViewImagesList.adapter = imageGalleryListAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myHubGalleryViewModel.stateLiveData.observe(this, Observer {
            handleStateChanged(it)
        })
    }

    private fun handleStateChanged(state: MyHubGalleryState) =
        when (val imagesListMonitor = state.imagesStateMonitor) {
            StateMonitor.Init -> {
                showLoading(false)
            }
            StateMonitor.Loading -> {
                showLoading(true)
            }
            is StateMonitor.Loaded -> {
                showLoading(false)
                populateImages(imagesListMonitor.result)
            }
            is StateMonitor.Failed -> {
                showLoading(false)
                showError(imagesListMonitor.failed, imagesListMonitor.action)
            }
        }

    private fun populateImages(imagesList: List<PhotoInfo>) {
        imageGalleryListAdapter.submitList(imagesList)
    }

    private fun showError(throwable: Throwable, action: (() -> Any)? = null) {
        val snackBar = Snackbar.make(
            constraintLayoutRoot,
            throwable.message ?: getString(R.string.generic_error_unknown),
            Snackbar.LENGTH_SHORT
        )
        if (action != null) {
            snackBar.setAction(R.string.action_retry) {
                action.invoke()
            }
            snackBar.duration = Snackbar.LENGTH_INDEFINITE
        }
        snackBar.show()
    }

    private fun showLoading(shouldShow: Boolean) {
        if (shouldShow && imageGalleryListAdapter.itemCount == 0) {
            progressBarLoadingImages.show()
        } else {
            progressBarLoadingImages.hide()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    compressImageAndUpload(UCrop.getOutput(data)!!)
                }
            }
            UCrop.RESULT_ERROR -> {
                showError(Exception(getString(R.string.message_error_compressing_image)))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}