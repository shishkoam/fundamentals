package ua.shishkoam.fundamentals

import android.content.res.Configuration
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
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
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager.Companion.AUTO_FIT
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val likedFilms: HashMap<String, Boolean> = HashMap()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val savedData =
                CollectionUtils.fromBundleBooleanMap(savedInstanceState.getBundle("likes"))
            savedData?.let {
                likedFilms.putAll(savedData)
            }
        }

        val orientation = this.resources.configuration.orientation
        val filmRecyclerViewManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridAutofitLayoutManager(requireContext(), AUTO_FIT)
        } else {
            GridLayoutManager(requireContext(), 2)
        }

        val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()

        val firstColor = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_1);
        val secondColor = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_2);
        val thirdColor = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_3);
        val textShader = LinearGradient(
            0f,
            0f,
            0f,
            50f,
            intArrayOf(firstColor, secondColor, thirdColor),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        val films = DummyContent.films
        val filmRecyclerViewAdapter = FilmRecyclerViewAdapter(
            values = films, nameShader = textShader, likedFilms = likedFilms,
            onItemClickListener = object : FilmRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(itemView: View?, position: Int) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(DummyContent.films[position])
                    findNavController().navigate(action)
                }
            },
            onItemLikeListener = object : FilmRecyclerViewAdapter.OnItemLikeListener {
                override fun onItemLike(itemView: View?, position: Int) {
                    val isLiked = likedFilms[films[position].name] ?: false
                    likedFilms[films[position].name] = !isLiked
                }
            }
        )
        val recyclerView = view.findViewById(R.id.actor_list) as RecyclerView

        val swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView.run {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = filmRecyclerViewManager

            // specify an viewAdapter (see also next example)
            adapter = filmRecyclerViewAdapter
            itemAnimator = landingItemAnimator


        }
        swipeRefreshLayout.setOnRefreshListener {
            // Initialize a new Runnable
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("likes", CollectionUtils.toBundleBooleanMap(likedFilms))
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