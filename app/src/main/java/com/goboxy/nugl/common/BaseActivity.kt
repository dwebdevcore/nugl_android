package com.goboxy.nugl.common

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class BaseActivity : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return true
        }

        if (item.itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item)
    }

}