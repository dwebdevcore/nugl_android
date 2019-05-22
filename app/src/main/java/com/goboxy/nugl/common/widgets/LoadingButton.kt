package com.goboxy.nugl.common.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.goboxy.nugl.R

class LoadingButton : FrameLayout {

    private val button: Button
    private val progressBar: ProgressBar
    private val textColors: ColorStateList

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setPadding(0, 0, 0, 0)

        button = Button(context, attrs, defStyleAttr)

        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall)
        progressBar.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        progressBar.indeterminateTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        progressBar.visibility = View.GONE

        textColors = button.textColors

        addView(button)
        addView(progressBar)
    }

    override fun callOnClick(): Boolean {
        return button.callOnClick()
    }

    override fun setOnClickListener(clickListener: View.OnClickListener?) {
        button.setOnClickListener {
            clickListener?.onClick(this@LoadingButton)
        }
    }

    fun setLoading(loading: Boolean) {
        button.isEnabled = !loading
        if (loading) {
            button.setTextColor(Color.TRANSPARENT)
            progressBar.visibility = View.VISIBLE
        } else {
            button.setTextColor(textColors)
            progressBar.visibility = View.GONE
        }
    }

}