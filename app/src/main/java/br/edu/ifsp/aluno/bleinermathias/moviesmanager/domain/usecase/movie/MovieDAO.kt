package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie

interface MovieDAO  {
    companion object {
        const val MOVIE_TABLE = "movie"
    }

    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)
    @Delete
    suspend fun delete(movie: Movie)
    @Query("SELECT * FROM $MOVIE_TABLE")
    suspend fun getAllMovies(): List<Movie>
    @Query("SELECT * FROM $MOVIE_TABLE WHERE id = :id")
    suspend fun getMovieById(id: String): Movie
    @Query("SELECT * FROM $MOVIE_TABLE WHERE genre = :genre")
    suspend fun getMoviesByGenre(genre:String): List<Movie>
}