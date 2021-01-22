package ua.shishkoam.fundamentals.data.room

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.CacheRepository
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class RoomRepository(applicationContext: Context) : CacheRepository {

    private val db = DataBase.create(applicationContext)

    override suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        db.dao.getAll().map {
            toMovie(it)
        }
    }

    override suspend fun addMovies(movies: List<Movie>): List<Movie> =
        withContext(Dispatchers.IO) {
            for (movie in movies) {
                db.dao.insert(toEntity(movie))
            }
            getAllMovies()
        }

    override suspend fun addActors(id: Long, actors: List<Actor>) = withContext(Dispatchers.IO) {
        val actorsIds = ArrayList<Long>()
        for (actor in actors) {
            db.dao.insert(toEntity(actor))
            actorsIds.add(actor.id.toLong())
        }
        val movie = db.dao.getById(id)
        movie.actors = actorsIds
        db.dao.insert(movie)
    }

    override suspend fun getActors(id: Long): List<Actor> = withContext(Dispatchers.IO) {
        val movie = db.dao.getById(id)
        val actorsIds = movie.actors
        val result = db.dao.getActorsByIds(actorsIds)
        val actors = ArrayList<Actor>()
        for (actorEntity in result) {
            actors.add(toActor(actorEntity))
        }
        actors
    }

    private fun toEntity(movie: Movie, actors: ArrayList<Long> = ArrayList()) = MovieEntity(
        id = movie.id.toLong(),
        originalLanguage = movie.originalLanguage,
        originalTitle = movie.originalTitle,
        overview = movie.overview,
        releaseDate = movie.releaseDate,
        title = movie.title,
        voteAverage = movie.voteAverage,
        voteCount = movie.voteCount,
        posterUrl = movie.posterUrl ?: "",
        backdropUrl = movie.backdropUrl ?: "",
        isFavorite = movie.isFavorite,
        genresNames = movie.genresNames,
        actors = actors
    )

    private fun toEntity(actor: Actor) = ActorEntity(
        id = actor.id.toLong(),
        name = actor.name ?: "",
        order = actor.order,
        imageUrl = actor.imageUrl ?: ""
    )

    private fun toMovie(entity: MovieEntity) = Movie(
        id = entity.id.toInt(),
        originalLanguage = entity.originalLanguage,
        originalTitle = entity.originalTitle,
        overview = entity.overview,
        releaseDate = entity.releaseDate,
        title = entity.title,
        voteAverage = entity.voteAverage,
        voteCount = entity.voteCount,
        posterUrl = entity.posterUrl,
        backdropUrl = entity.backdropUrl,
        isFavorite = entity.isFavorite,
        genresNames = entity.genresNames
    )

    private fun toActor(entity: ActorEntity) = Actor(
        id = entity.id.toInt(),
        name = entity.name,
        order = entity.order,
        imageUrl = entity.imageUrl
    )
}