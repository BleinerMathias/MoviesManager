package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.data.sqlite.MoviesDatabase
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.MovieGenre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback


class MovieViewModel(application: Application):ViewModel() {

    private val movieDaoImplement = Room.databaseBuilder(
        application.applicationContext,
        MoviesDatabase::class.java,
        MoviesDatabase.MOVIES_DATABASE_SQLITE
    ).build().getMovieDao();

    private val movieGenreDaoImplement = Room.databaseBuilder(
        application.applicationContext,
        MoviesDatabase::class.java,
        MoviesDatabase.MOVIES_DATABASE_SQLITE
    ).build().getMovieGenreDao();

    val moviesMutableLiveData = MutableLiveData<List<Movie>>()
    val spinnerDataList = MutableLiveData<List<String>>()

    fun createMovie(movie: Movie){
        CoroutineScope(Dispatchers.IO).launch {
            movieDaoImplement.insert(movie);
        }
    }

    fun getMovies(genre: String = ""){
        if(genre.isNotEmpty()){

        }else{
            CoroutineScope(Dispatchers.IO).launch {
                val movies = movieDaoImplement.getAllMovies()
                moviesMutableLiveData.postValue(movies)
            }
        }
    }

    fun editMovie(movie: Movie){
        CoroutineScope(Dispatchers.IO).launch {
            movieDaoImplement.update(movie)
        }
    }

    fun removeMovie(movie:Movie){
        CoroutineScope(Dispatchers.IO).launch {
            movieDaoImplement.delete(movie)
        }
    }

    fun createMovieGenre(movieGenre: MovieGenre){
        CoroutineScope(Dispatchers.IO).launch {
            movieGenreDaoImplement.insert(movieGenre)
        }
    }

    fun getAllMovieGenres(){
        CoroutineScope(Dispatchers.IO).launch {
            val genres = movieGenreDaoImplement.getAllGenres()
            val genreNames = genres.map { it.name }
            spinnerDataList.postValue(genreNames)
        }
    }



    companion object {
        val MovieViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                MovieViewModel(checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as T
        }
    }

}


