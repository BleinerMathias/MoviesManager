package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie

@Dao
interface MovieDAO  {
    companion object {
        const val MOVIE_TABLE = "movie"
    }

    @Insert
    fun insert(movie: Movie)

    @Update
    fun update(movie: Movie)
    @Delete
    fun delete(movie: Movie)

    @Query("SELECT * FROM $MOVIE_TABLE")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE id = :id")
    fun getMovieById(id: String): Movie

    @Query("SELECT * FROM $MOVIE_TABLE WHERE genre = :genre")
    fun getMoviesByGenre(genre:String): List<Movie>

}