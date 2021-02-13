package ua.shishkoam.fundamentals.presentation

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.databinding.FragmentMoviesDetailsBinding
import ua.shishkoam.fundamentals.domain.CalendarInteractor
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie
import ua.shishkoam.fundamentals.presentation.recyclerview.ActorDelegateAdapter
import ua.shishkoam.fundamentals.presentation.recyclerview.LandingAnimator
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieByIdViewModelFactory
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieDetailsViewModel
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieViewModelFactory
import ua.shishkoam.fundamentals.utils.ImageLoader
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment(R.layout.fragment_movies_details), DIAware {
    override val di: DI by di()

    private val args: FragmentMoviesDetailsArgs by navArgs()
    private val movieInteractor: MovieInteractor by instance()
    private val calendarInteractor: CalendarInteractor by instance()

    private val binding by viewBinding(FragmentMoviesDetailsBinding::bind)
    private val actorListStateObserver = Observer<List<Actor>> { actors ->
        actors ?: return@Observer
        listAdapter.items = actors
        listAdapter.notifyDataSetChanged()
    }

    private val errorStateObserver = Observer<RepositoryError> { error ->
        error ?: return@Observer
        if (error == RepositoryError.LOAD_ERROR) {
            showExceptionToUser(getString(R.string.cant_load_film_details))
        }
    }

    private fun showExceptionToUser(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private val listAdapter = ActorDelegateAdapter()

    private val movieDetails: MovieDetailsViewModel by lazy {
        if (args.currentMovieId != -1) {
            ViewModelProvider(
                this@FragmentMoviesDetails,
                MovieByIdViewModelFactory(movieInteractor, calendarInteractor, args.currentMovieId)
            ).get(MovieDetailsViewModel::class.java)
        } else {
            ViewModelProvider(
                this@FragmentMoviesDetails,
                MovieViewModelFactory(movieInteractor, calendarInteractor, args.currentMovie!!)
            ).get(MovieDetailsViewModel::class.java)
        }

    }

    private val movieStateObserver = Observer<Movie> { movie ->
        movie ?: return@Observer
        binding.run {
            ImageLoader.loadImage(poster, movie.backdropUrl)
            nameText.text = movie.title
            genreText.text = movie.getGenresString()
            ratingBar.rating = movie.getRatingIn5Stars()
            storyText.text = movie.overview
            langText.text = "${movie.originalLanguage}"
            reviewsText.text =
                requireContext().getString(R.string.reviews_number, movie.voteCount)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startDatePickDialog()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.movie_not_scheduled),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCast()
        movieDetails.run {
            movieLive.observe(viewLifecycleOwner, movieStateObserver)
            actors.observe(viewLifecycleOwner, actorListStateObserver)
            error.observe(viewLifecycleOwner, errorStateObserver)
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.scheduleButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startDatePickDialog()
            } else {

                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_CALENDAR
                )
            }
        }
    }



    private fun initCast() {
        val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
        binding.movieList.run {
            setHasFixedSize(true)
            adapter = listAdapter
            itemAnimator = landingItemAnimator
        }
    }

    private fun showTimePickerDialog(year: Int, month: Int, day: Int) {
        val newFragment = TimePickerFragment() { hourOfDay, minute ->
            val result = movieDetails.addMovieToCalendar(year, month, day, hourOfDay, minute)
            val msg = if (result)  getString(R.string.movie_scheduled) else getString(R.string.movie_not_scheduled)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
        newFragment.show(requireActivity().supportFragmentManager, "timePicker")
    }

    private fun startDatePickDialog() {
        val newFragment = DatePickerFragment() { year, month, day ->
            showTimePickerDialog( year, month, day)
        }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    class DatePickerFragment(private val onDatePick: ((year: Int, month: Int, day: Int) -> Unit)) :
        DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            onDatePick.invoke(year, month, day)
        }
    }


    class TimePickerFragment(
        private val onTimePick: ((hourOfDay: Int, minute: Int) -> Unit)) :
        DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            onTimePick.invoke(hourOfDay, minute)
        }
    }

}