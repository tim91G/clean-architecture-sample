package com.timgortworst.cleanarchitecture.presentation.features.movie.list.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.timgortworst.cleanarchitecture.presentation.R
import com.timgortworst.cleanarchitecture.presentation.databinding.LayoutRecyclerviewBinding
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.decoration.AdapterSpanSize
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.decoration.MovieListSpanSizeLookup.Companion.FULL_WIDTH

class NestedRecyclerAdapter<T, A : ListAdapter<T, *>>(
    private val items: List<T>,
    private val itemAdapter: A,
    private vararg val itemDecorations: RecyclerView.ItemDecoration,
) : RecyclerView.Adapter<NestedRecyclerAdapter<T, A>.ViewHolder>(), AdapterSpanSize {
    private val scrollStates: MutableMap<String, Parcelable?> = mutableMapOf()
    private val viewPool = RecyclerView.RecycledViewPool()

    private fun getSectionID(position: Int): String {
        return items.getOrNull(position)?.hashCode()?.toString().orEmpty()
    }

    override fun getItemViewType(position: Int): Int = R.layout.layout_recyclerview * getSpanSize()

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        val key = getSectionID(holder.layoutPosition)
        scrollStates[key] = holder.recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = if (items.isEmpty()) 0 else 1

    override fun getSpanSize() = FULL_WIDTH

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items)
    }

    inner class ViewHolder(
        binding: LayoutRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val recyclerView = binding.recyclerView.apply {
            setRecycledViewPool(viewPool)
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(
                binding.recyclerView.context, HORIZONTAL, false
            ).apply {
                initialPrefetchItemCount = 4
                itemDecorations.forEach { addItemDecoration(it) }
            }
        }

        fun bind(items: List<T>) {
            itemAdapter.submitList(items)
            restoreState()
        }

        private fun restoreState() {
            val key = getSectionID(layoutPosition)
            val state = scrollStates[key]
            if (state != null) {
                recyclerView.layoutManager?.onRestoreInstanceState(state)
            } else {
                recyclerView.layoutManager?.scrollToPosition(0)
            }
        }
    }
}