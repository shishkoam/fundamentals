package ua.shishkoam.fundamentals.data

import androidx.annotation.NonNull
import ua.shishkoam.fundamentals.data.dto.Configuration
import ua.shishkoam.fundamentals.data.dto.MovieDTO
import ua.shishkoam.fundamentals.domain.data.Movie

fun MovieDTO.toDomainMovie(configuration: Configuration?, genres: HashMap<Int, String>): Movie {
    val movie = Movie(
        id, originalLanguage, originalTitle, overview,
        releaseDate, title, voteAverage, voteCount
    )
    configuration?.let { config ->
        movie.posterUrl = getPosterFullImageUrl(this, config)
        movie.backdropUrl = getBackdropFullImageUrl(this, config)
    }
    movie.genresNames = genresNamesFromIds(genres, genreIds)
    return movie
}


fun getPosterFullImageUrl(movieDTO: MovieDTO, configuration: Configuration): String {
    val imagePath: String? = if (movieDTO.posterPath.isNullOrEmpty()) {
        movieDTO.backdrop_path
    } else {
        movieDTO.posterPath
    }
    return configuration.getFullImageUrl(imagePath) ?: ""
}

fun getBackdropFullImageUrl(movieDTO: MovieDTO, configuration: Configuration): String {
    val imagePath: String? = movieDTO.backdrop_path
    return configuration.getFullImageUrl(imagePath)
}

fun genresNamesFromIds(genresMap: HashMap<Int, String>, genre_ids: List<Int>): HashSet<String> {
    val genresNames = HashSet<String>()
    for (genreId in genre_ids) {
        genresNames.add(genresMap[genreId] ?: "")
    }
    genresNames.remove("")
    return genresNames
}