package ua.shishkoam.fundamentals.presentation.recyclerview

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val pageSize: Int = 4, private val delta: Int = 0,
    var isLoading: Boolean = false, var isLastPage: Boolean = false,
    private val loadMore: (() -> Unit)? = null
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = when (layoutManager) {
            layoutManager as LinearLayoutManager, layoutManager as GridLayoutManager,
            -> layoutManager.findFirstVisibleItemPosition()
            else -> 0
        }

        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition + delta >= totalItemCount
                && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize
            ) {
                loadMore?.invoke()
            }
        }
    }
}