package com.moustafa.mymediahub.features.imagegallerylistscreen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.moustafa.mymediahub.R
import com.moustafa.mymediahub.base.BaseFragment
import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.network.StateMonitor
import kotlinx.android.synthetic.main.fragment_my_hub_gallery.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

class MyHubGalleryFragment : BaseFragment(R.layout.fragment_my_hub_gallery) {

    private val myHubGalleryViewModel: MyHubGalleryViewModel by viewModel()

    private val imageGalleryListAdapter: ImageGalleryListAdapter = ImageGalleryListAdapter()

    override fun setupViews(rootView: View) {
        recyclerViewImagesList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

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

}