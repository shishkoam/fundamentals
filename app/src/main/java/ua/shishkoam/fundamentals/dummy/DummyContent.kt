package ua.shishkoam.fundamentals.dummy

import ua.shishkoam.fundamentals.R
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object DummyContent {

    val ITEMS: MutableList<DummyItem> = ArrayList()

    init {
        // Add some sample items.
        addItem(
            DummyItem(
                "Avengers: End Game",
                137,
                R.drawable.movie,
                rating = 4.0,
                reviewNum = 125,
                genres = "Action, Adventure, Drama"
            )
        );
        addItem(
            DummyItem(
                "Tenet",
                97,
                R.drawable.movie2,
                true,
                16,
                5.0,
                98,
                "Action, Sci-Fi, Thriller"
            )
        );
        addItem(
            DummyItem(
                "Black Widow",
                102,
                R.drawable.movie3,
                rating = 4.0,
                reviewNum = 38,
                genres = "Action, Adventure, Sci-Fi"
            )
        );
        addItem(
            DummyItem(
                "Wonder Woman 1984",
                120,
                R.drawable.movie4,
                rating = 5.0,
                reviewNum = 74,
                genres = "Action, Adventure, Fantasy"
            )
        );

    }


    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(
        val name: String,
        val time: Int,
        val image: Int,
        var like: Boolean = false,
        var age: Int = 13,
        var rating: Double = 0.0,
        var reviewNum: Int = 0,
        val genres: String?
    ) {
        override fun toString(): String = name
    }

}