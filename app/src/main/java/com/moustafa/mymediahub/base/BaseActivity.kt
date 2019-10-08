package com.moustafa.mymediahub.base

import androidx.appcompat.app.AppCompatActivity

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */

abstract class BaseActivity(layout: Int) : AppCompatActivity(layout) {

    override fun onStart() {
        super.onStart()
        setupViews()
    }

    /**
     * This function is called at the appropriate time to initialise the views
     * i.e. set listeners...
     */
    protected abstract fun setupViews()

}