package com.goboxy.nugl.listingdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.goboxy.nugl.R
import com.goboxy.nugl.common.BaseActivity

class ListingDetailActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, ListingDetailActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_detail)
    }

}