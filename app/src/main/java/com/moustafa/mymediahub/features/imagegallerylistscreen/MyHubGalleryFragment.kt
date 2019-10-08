package com.moustafa.mymediahub.features.imagegallerylistscreen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.moustafa.mymediahub.R
import com.moustafa.mymediahub.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_hub_gallery.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

class MyHubGalleryFragment : BaseFragment(R.layout.fragment_my_hub_gallery) {

    private val myHubGalleryViewModel: MyHubGalleryViewModel by viewModel()

    private val imageGalleryListAdapter: ImageGalleryListAdapter =
        ImageGalleryListAdapter()

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

    private fun handleStateChanged(state: MyHubGalleryState) {

    }

}