package com.goboxy.nugl.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goboxy.nugl.R
import com.goboxy.nugl.main.MainBottomFragment

class HomeFragment : MainBottomFragment() {

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}