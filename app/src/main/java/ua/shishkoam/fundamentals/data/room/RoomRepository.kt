package ua.shishkoam.fundamentals.data.room

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.CacheRepository
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie
import java.util.*
import kotlin.collections.ArrayList

class RoomRepository(applicationContext: Context) : CacheRepository {

    private val db = DataBase.create(applicationContext)

    override fun getAllMovies(): Flow<List<Movie>> =
        db.dao.getAllMovies().map { movies ->
            val list = LinkedList<Movie>()
            for (movie in movies) {
                list.add(toMovie(movie))
            }
            list
        }

    override fun getMovie(id: Int): Flow<Movie> =
        db.dao.getById(id.toLong()).map { movie ->
            toMovie(movie)
        }

    override fun addMovies(movies: List<Movie>) {
        val movieEntityList = ArrayList<MovieEntity>()
        for (movie in movies) {
            movieEntityList.add(toEntity(movie))
        }
        GlobalScope.launch {
            db.dao.insertMovies(movieEntityList)
        }
    }

    override suspend fun clearMovies() {
        db.dao.clearMovies()
    }

    override suspend fun addActors(id: Long, actors: List<Actor>) = withContext(Dispatchers.IO) {
        val actorsIds = ArrayList<Long>()
        val actorsEntityList = ArrayList<ActorEntity>()
        for (actor in actors) {
            actorsEntityList.add(toEntity(actor))
            actorsIds.add(actor.id.toLong())
        }
        db.dao.insertActors(actorsEntityList)
        val movie = db.dao.getMovie(id)
        movie.actors = actorsIds
        db.dao.insert(movie)
    }

    override suspend fun getActors(id: Long): List<Actor> = withContext(Dispatchers.IO) {
        val movie = db.dao.getMovie(id)
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
        actors = actors,
        popularity = movie.popularity
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
        genresNames = entity.genresNames,
        popularity = entity.popularity
    )

    private fun toActor(entity: ActorEntity) = Actor(
        id = entity.id.toInt(),
        name = entity.name,
        order = entity.order,
        imageUrl = entity.imageUrl
    )
}