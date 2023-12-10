package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity
data class Movie(
    @PrimaryKey
    val id: String,
    val name: String,
    val releaseYear: Int,
    val producer: String,
    val duration: Int,
    val watched: Int = MOVIE_WATCHED_FALSE,
    val rating: Int,
    val genre: String
) : Parcelable {
    companion object {
        const val MOVIE_WATCHED_TRUE = 1
        const val MOVIE_WATCHED_FALSE = 0
    }
}
