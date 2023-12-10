package br.edu.ifsp.aluno.bleinermathias.moviesmanager.data.sqlite


import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.MovieGenre
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie.MovieDAO
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie.MovieGenreDAO

@Database(entities = [Movie::class, MovieGenre::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    companion object {
        const val MOVIES_DATABASE_SQLITE = "moviesManagerDatabase"
    }

    abstract fun getMovieDao(): MovieDAO
    abstract fun getMovieGenreDao(): MovieGenreDAO

}