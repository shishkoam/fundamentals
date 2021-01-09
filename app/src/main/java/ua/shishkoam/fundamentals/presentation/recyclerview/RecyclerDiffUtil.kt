package ua.shishkoam.fundamentals.presentation.recyclerview

import androidx.recyclerview.widget.DiffUtil
import ua.shishkoam.fundamentals.domain.data.ListItem
import ua.shishkoam.fundamentals.domain.data.Movie

class RecyclerDiffUtil(
    private val oldItems: List<ListItem>,
    private val newItems: List<ListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        (oldItems[oldItemPosition] as? Movie)?.id == (newItems[newItemPosition] as? Movie)?.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}