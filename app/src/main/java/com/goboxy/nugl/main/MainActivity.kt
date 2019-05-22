package com.goboxy.nugl.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.goboxy.nugl.R
import com.goboxy.nugl.common.BaseActivity
import com.goboxy.nugl.main.account.AccountFragment
import com.goboxy.nugl.main.home.HomeFragment
import com.goboxy.nugl.main.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainBottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        mainBottomNavigationView.selectedItemId = R.id.menu_main_tab_home
    }

    private val navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragmentType = MainBottomFragmentType.find(item.itemId)
        val fragment = supportFragmentManager.findFragmentByTag(fragmentType.tag)
        val transaction = supportFragmentManager.beginTransaction()

        if (fragment != null) {
            for (f in supportFragmentManager.fragments) {
                if (f == fragment) {
                    transaction.show(f)
                } else {
                    transaction.hide(f)
                }
            }
        } else {
            for (f in supportFragmentManager.fragments) {
                transaction.hide(f)
            }
            transaction.add(R.id.mainContentView, buildFragment(fragmentType), fragmentType.tag)
        }

        transaction.commitAllowingStateLoss()

        true
    }

    private fun buildFragment(fragment: MainBottomFragmentType): Fragment? {
        return when (fragment) {
            is MainBottomFragmentType.Home -> HomeFragment.newInstance()
            is MainBottomFragmentType.Search -> SearchFragment.newInstance()
            is MainBottomFragmentType.Account -> AccountFragment.newInstance()
        }
    }

    override fun onBackPressed() {
        val homeTabId = R.id.menu_main_tab_home

        if (mainBottomNavigationView.selectedItemId != homeTabId) {
            mainBottomNavigationView.selectedItemId = homeTabId
            return
        }

        super.onBackPressed()
    }

}