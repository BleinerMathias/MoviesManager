package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MovieGenre(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var name: String
):Parcelable {
    override fun toString(): String {
        return name
    }
}

