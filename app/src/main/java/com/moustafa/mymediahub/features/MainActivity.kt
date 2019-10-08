package com.moustafa.mymediahub.features

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.moustafa.mymediahub.R
import com.moustafa.mymediahub.base.BaseActivity

/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val appBarConfiguration = AppBarConfiguration(
        setOf(R.id.myHubGalleryFragment)
    )

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    override fun setupViews() {

    }
}