package com.goboxy.nugl.main.search

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class SpacesItemDecoration(private val orientation: Int, private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == RecyclerView.HORIZONTAL) {
            outRect.left = space / 2
            outRect.right = space / 2
        } else {
            val firstItem = parent.getChildAdapterPosition(view) == 0
            val lastItem = parent.getChildAdapterPosition(view) == parent.childCount - 1
            outRect.top = if (firstItem) 0 else space / 2
            outRect.bottom = if (lastItem) 0 else space / 2
        }
    }

}