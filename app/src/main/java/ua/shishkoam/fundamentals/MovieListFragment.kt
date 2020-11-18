package ua.shishkoam.fundamentals

import android.content.res.Configuration
import android.graphics.LinearGradient
import android.graphics.Point
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.view.Display
import android.view.View
import android.view.WindowMetrics
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.shishkoam.fundamentals.dummy.DummyContent
import ua.shishkoam.fundamentals.recyclerview.FilmRecyclerViewAdapter
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator
import java.lang.Math.abs


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieListFragment : Fragment(R.layout.movie_list_fragment) {
    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orientation = this.resources.configuration.orientation
        val width = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display: WindowMetrics = requireActivity().windowManager.currentWindowMetrics
            kotlin.math.abs(display.bounds.right - display.bounds.left)
        } else {
            val display: Display = requireActivity().windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            size.x
        }
//        1600
//        720
        val columnCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        val viewManager = if (columnCount == 2) {
            GridLayoutManager(requireContext(), 2)
        } else {
            GridAutofitLayoutManager(requireContext(), -1)
        }
        val defaultItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()


//        RecyclerView.ItemDecoration

        val first = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_1);
        val second = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_2);
        val third = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_3);
        val textShader = LinearGradient(
            0f,
            0f,
            0f,
            50f,
            intArrayOf(first, second, third),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        val viewAdapter = FilmRecyclerViewAdapter(DummyContent.films, textShader)
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