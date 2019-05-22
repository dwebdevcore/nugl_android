package com.goboxy.nugl.main

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class MainBottomFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}