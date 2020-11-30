package ua.shishkoam.fundamentals.dummy

import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Actor
import ua.shishkoam.fundamentals.data.Film
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object DummyContent {

    val films: MutableList<Film> = ArrayList()
    val actors: MutableList<Actor> = ArrayList()

    init {
        addActor(
            Actor(
                "Robert Downey Jr.",
                R.drawable.actor1
            )
        )
        addActor(
            Actor(
                "Chris Evans",
                R.drawable.actor2
            )
        )
        addActor(
            Actor(
                "Mark Ruffalo",
                R.drawable.actor3
            )
        )
        addActor(
            Actor(
                "Chris Hemsworth",
                R.drawable.actor4
            )
        )
        addActor(
            Actor(
                "Robert Downey Jr.",
                R.drawable.actor1
            )
        )
        addActor(
            Actor(
                "Chris Evans",
                R.drawable.actor2
            )
        )
        // Add some sample items.
        addFilm(
            Film(
                "Avengers: End Game",
                137,
                R.drawable.movie,
                rating = 4.0,
                reviewNum = 125,
                genres = "Action, Adventure, Drama",
                story = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos\' actions and restore balance to the universe.",
                cast = actors,
                imageBig = R.drawable.poster
            )
        )
        addFilm(
            Film(
                "Tenet",
                97,
                R.drawable.movie2,
                true,
                16,
                5.0,
                98,
                "Action, Sci-Fi, Thriller"
            )
        )
        addFilm(
            Film(
                "Black Widow",
                102,
                R.drawable.movie3,
                rating = 4.0,
                reviewNum = 38,
                genres = "Action, Adventure, Sci-Fi"
            )
        )
        addFilm(
            Film(
                "Wonder Woman 1984",
                120,
                R.drawable.movie4,
                rating = 5.0,
                reviewNum = 74,
                genres = "Action, Adventure, Fantasy"
            )
        )

    }


    private fun addFilm(item: Film) {
        films.add(item)
    }

    private fun addActor(item: Actor) {
        actors.add(item)
    }

}