package com.timgortworst.cleanarchitecture.presentation.features.movie.base

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.decoration.AdapterSpanSize
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.decoration.MovieListSpanSizeLookup

abstract class BaseListSpanAdapter<T, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>,
) : BaseListAdapter<T, VB>(diffCallback), AdapterSpanSize {

    abstract val itemViewType: Int

    // combine the layout and spansize to provide a unique but re-usable integer for the concatadapter
    // recycling between different adapters with the same layout
    override fun getItemViewType(position: Int): Int = itemViewType * getSpanSize()

    override fun getSpanSize(): Int = MovieListSpanSizeLookup.COLUMNS_SINGLE
}