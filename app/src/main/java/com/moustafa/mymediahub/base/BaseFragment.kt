package com.moustafa.mymediahub.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */

abstract class BaseFragment(layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    /**
     * This function is called at the appropriate time to initialise the views
     * i.e. set listeners...
     *
     * @param rootView – The fragment's root view
     */
    protected abstract fun setupViews(rootView: View)

}
