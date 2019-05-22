package com.goboxy.nugl.main

import com.goboxy.nugl.R

private const val TAG_FRAGMENT_HOME = "tag_fragment_home"
private const val TAG_FRAGMENT_SEARCH = "tag_fragment_search"
private const val TAG_FRAGMENT_ACCOUNT = "tag_fragment_account"

sealed class MainBottomFragmentType(val tag: String) {

    class Home : MainBottomFragmentType(TAG_FRAGMENT_HOME)
    class Search : MainBottomFragmentType(TAG_FRAGMENT_SEARCH)
    class Account : MainBottomFragmentType(TAG_FRAGMENT_ACCOUNT)

    companion object {

        fun find(resId: Int): MainBottomFragmentType {
            return when (resId) {
                R.id.menu_main_tab_home -> Home()
                R.id.menu_main_tab_search -> Search()
                R.id.menu_main_tab_account -> Account()
                else -> Home()
            }
        }

    }

}