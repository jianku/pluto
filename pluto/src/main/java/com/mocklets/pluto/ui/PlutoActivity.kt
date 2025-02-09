package com.mocklets.pluto.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.fragment.app.FragmentActivity
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.canDrawOverlays
import com.mocklets.pluto.core.extensions.openOverlaySettings
import com.mocklets.pluto.core.ui.routing.RouteManager
import com.mocklets.pluto.modules.network.proxy.NetworkProxyViewModel
import com.mocklets.pluto.modules.network.proxy.ui.NetworkProxySettingsFragment.Companion.IN_APP_BROWSER_RESULT_CODE
import com.mocklets.pluto.modules.setup.easyaccess.EasyAccessSetupDialog

@Keep
internal class PlutoActivity : FragmentActivity(R.layout.pluto___activity_pluto) {

    private lateinit var routeManager: RouteManager
    private val session = Pluto.session
    private val networkProxyViewModel: NetworkProxyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        routeManager = RouteManager(this, R.id.container)
    }

    override fun onResume() {
        super.onResume()
        if (!canDrawOverlays() && !session.isEasyAccessSetupDialogShown) {
            showEasyAccessSetup()
        }
    }

    override fun onBackPressed() {
        if (!routeManager.onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun showEasyAccessSetup() {
        EasyAccessSetupDialog(this) { openOverlaySettings() }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IN_APP_BROWSER_RESULT_CODE) {
            Pluto.activity.customTabClosed()
            networkProxyViewModel.onInAppBrowserClose()
        }
    }
}
