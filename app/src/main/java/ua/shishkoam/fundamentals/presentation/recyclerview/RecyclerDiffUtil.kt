package ua.shishkoam.fundamentals.presentation.recyclerview

import androidx.recyclerview.widget.DiffUtil
import ua.shishkoam.fundamentals.domain.data.Movie

class RecyclerDiffUtil(
    private val oldItems: List<Movie>,
    private val newItems: List<Movie>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}