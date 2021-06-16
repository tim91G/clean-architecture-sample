package com.timgortworst.cleanarchitecture.presentation.features.movie.discover.decoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.timgortworst.cleanarchitecture.presentation.R

/**
 * Decoration for adding margin between blocks when used with a grid layout.
 */
class ItemMarginDecoration(
    resources: Resources
) : RecyclerView.ItemDecoration() {
    private val spacing = resources.getDimension(R.dimen.default_padding).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as? GridLayoutManager
        if (layoutManager == null || layoutManager.orientation != VERTICAL) {
            return super.getItemOffsets(outRect, view, parent, state)
        }

        val position: Int = parent.getChildAdapterPosition(view)
        val spanCount = layoutManager.spanCount
        val column = position % spanCount

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount
        if (position < spanCount) outRect.top = spacing
        outRect.bottom = spacing
    }
}