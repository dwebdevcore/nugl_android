package com.goboxy.nugl.common.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout

class WindowInsetFrameLayout : FrameLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View, child: View) {
                requestApplyInsets()
            }

            override fun onChildViewRemoved(parent: View, child: View) {

            }
        })
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child?.dispatchApplyWindowInsets(insets)
        }
        return super.onApplyWindowInsets(insets)
    }

}