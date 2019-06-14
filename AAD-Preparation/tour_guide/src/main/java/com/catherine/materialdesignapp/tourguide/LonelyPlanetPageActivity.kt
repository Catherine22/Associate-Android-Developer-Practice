package com.catherine.materialdesignapp.tourguide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat

class LonelyPlanetPageActivity : AppCompatActivity() {
    companion object {
        private const val BBC_NEWS_URL = "https://www.lonelyplanet.com/"
        private const val CUSTOM_TABS_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_page)

        val customTabsIntent = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .build()
        customTabsIntent.intent.data = Uri.parse(BBC_NEWS_URL)
        startActivityForResult(customTabsIntent.intent, CUSTOM_TABS_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CUSTOM_TABS_REQUEST_CODE) {
            finish()
        }
    }
}