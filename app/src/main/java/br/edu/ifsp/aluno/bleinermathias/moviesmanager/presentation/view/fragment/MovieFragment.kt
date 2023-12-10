package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.FragmentMovieBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_FALSE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_TRUE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment.MainFragment.Companion.EXTRA_MOVIE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment.MainFragment.Companion.MOVIE_FRAGMENT_REQUEST_KEY
import java.util.UUID


class MovieFragment : Fragment() {

    private lateinit var fragmentMovieBinding: FragmentMovieBinding
    private val navigationArgs: MovieFragmentArgs by navArgs()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false)

        // Edição - Recebe argumentos
        val receivedMovie = navigationArgs.movie

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.new_movie)

        receivedMovie?.also { movie ->
            with(fragmentMovieBinding) {
                editTextMovieName.setText(movie.name)
                editTextReleaseYear.setText(movie.releaseYear.toString())
                editTextProducer.setText(movie.producer)
                editTextDuration.setText(movie.duration.toString())
                editTextRating.setText(movie.rating.toString())

                // spinner (fazer)

                checkBoxWatched.isChecked = movie.watched == MOVIE_WATCHED_TRUE
                navigationArgs.editMovie.also { editTask ->

                    editTextMovieName.isEnabled = false
                    if(editTask){
                        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
                            getString(R.string.title_edit_movie)
                        btnSaveMovie.visibility = VISIBLE
                    }else{
                        btnSaveMovie.visibility = GONE
                        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = movie.name
                    }

                    editTextReleaseYear.isEnabled = editTask
                    editTextProducer.isEnabled = editTask
                    editTextDuration.isEnabled = editTask
                    editTextRating.isEnabled = editTask
                    spinnerGenre.isEnabled = editTask

                }
            }
        }

        fragmentMovieBinding.run {

            if (receivedMovie == null) {
                btnSaveMovie.text = getString(R.string.btn_save_movie)
            }else{
                btnSaveMovie.text = getString(R.string.btn_edit_movie)
            }

            btnSaveMovie.setOnClickListener {
                val checked = if (checkBoxWatched.isChecked) MOVIE_WATCHED_TRUE else MOVIE_WATCHED_FALSE
                setFragmentResult(MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    putParcelable(
                        EXTRA_MOVIE, Movie( UUID.randomUUID().toString(),
                            editTextMovieName.text.toString(),
                            editTextReleaseYear.text.toString().toInt(),
                            editTextProducer.text.toString(),
                            editTextDuration.text.toString().toInt(),
                            checked,
                            editTextRating.text.toString().toInt(),
                            spinnerGenre.selectedItem.toString()
                        )
                    )
                })
                findNavController().navigateUp()
            }
        }

        return fragmentMovieBinding.root
    }

}