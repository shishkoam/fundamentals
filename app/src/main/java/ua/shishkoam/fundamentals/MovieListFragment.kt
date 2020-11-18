package ua.shishkoam.fundamentals

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.shishkoam.fundamentals.dummy.DummyContent
import ua.shishkoam.fundamentals.recyclerview.FilmRecyclerViewAdapter
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieListFragment : Fragment(R.layout.movie_list_fragment) {
    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orientation = this.resources.configuration.orientation
        val columnCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        val viewManager = if (columnCount == 1) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), columnCount)
        }
        val defaultItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()


//        RecyclerView.ItemDecoration

        val viewAdapter = FilmRecyclerViewAdapter(DummyContent.films)
        viewAdapter.setOnItemClickListener(object : FilmRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        })
        val recyclerView = view.findViewById(R.id.list) as RecyclerView

        val swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager


            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
            itemAnimator = defaultItemAnimator


        }
        swipeRefreshLayout.setOnRefreshListener {
            // Initialize a new Runnable
            val runnable = Runnable {
                // Update the text view text with a random number
                // Hide swipe to refresh icon animation
                swipeRefreshLayout.isRefreshing = false
            }

            // Execute the task after specified time
            handler.postDelayed(
                runnable, 3000.toLong()
            )
        }
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            MovieListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}