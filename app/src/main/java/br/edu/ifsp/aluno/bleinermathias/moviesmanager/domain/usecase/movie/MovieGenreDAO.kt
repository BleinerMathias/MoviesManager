package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.MovieGenre

@Dao
interface MovieGenreDAO  {
    companion object {
        const val MOVIE_TABLE = "MovieGenre"
    }

    @Insert
    fun insert(movie: MovieGenre)

    @Query("SELECT * FROM $MOVIE_TABLE")
    fun getAllGenres(): List<MovieGenre>


}