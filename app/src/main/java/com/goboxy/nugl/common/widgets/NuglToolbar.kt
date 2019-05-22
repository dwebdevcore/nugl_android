package com.goboxy.nugl.common.widgets

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.goboxy.nugl.R
import io.github.tonnyl.spark.Spark
import kotlinx.android.synthetic.main.nugl_toolbar.view.*


class NuglToolbar : FrameLayout {

    private var spark: Spark? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.nugl_toolbar, this)

        if (context is AppCompatActivity) {
            context.setSupportActionBar(toolbar)
        }

        setOnApplyWindowInsetsListener { v, insets ->
            setPadding(0, insets.systemWindowInsetTop, 0, 0)
            insets
        }

        spark = Spark.Builder()
                .setView(this)
                .setAnimList(R.drawable.nugl_toolbar_anim_list)
                .setDuration(resources.getInteger(R.integer.toolbar_animation_duration))
                .build()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            spark?.startAnimation()
        } else {
            spark?.stopAnimation()
        }
    }

    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            spark?.startAnimation()
        } else {
            spark?.stopAnimation()
        }
    }

}