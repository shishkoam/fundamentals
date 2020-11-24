package ua.shishkoam.fundamentals

import android.content.res.Configuration
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ua.shishkoam.fundamentals.dummy.DummyContent
import ua.shishkoam.fundamentals.recyclerview.FilmRecyclerViewAdapter
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val handler = Handler()
    private val likes: HashMap<String, Boolean> = HashMap()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            likes.putAll(savedInstanceState.getSerializable("likes") as HashMap<String, Boolean>)
        }

        val orientation = this.resources.configuration.orientation
        val viewManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridAutofitLayoutManager(requireContext(), -1)
        } else {
            GridLayoutManager(requireContext(), 2)
        }

        val defaultItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()

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
        val data = DummyContent.films
        val viewAdapter = FilmRecyclerViewAdapter(
            data, textShader, likes = likes,
            object : FilmRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(itemView: View?, position: Int) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(DummyContent.films[position])
                    findNavController().navigate(action)
                }
            },
            object : FilmRecyclerViewAdapter.OnItemLikeListener {
                override fun onItemLike(itemView: View?, position: Int) {
                    val isLiked = likes[data[position].name] ?: false
                    likes[data[position].name] = !isLiked
                }
            }
        )
        val recyclerView = view.findViewById(R.id.list) as RecyclerView

        val swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView.run {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("likes", likes)

    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            FragmentMoviesList().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}