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

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.new_movie)

        fragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false)


        // Edição - recebe argumentos
        val receivedMovie = navigationArgs.movie
        receivedMovie?.also { movie ->
            with(fragmentMovieBinding) {
                editTextMovieName.setText(movie.name)
                editTextReleaseYear.setText(movie.releaseYear)
                editTextProducer.setText(movie.producer)
                editTextDuration.setText(movie.duration.toString())
                editTextRating.setText(movie.rating.toString())

                // spinner (fazer)

                checkBoxWatched.isChecked = movie.watched == MOVIE_WATCHED_TRUE

                navigationArgs.editMovie.also { editTask ->
                    editTextMovieName.isEnabled = editTask
                    btnSaveMovie.visibility = if (editTask) VISIBLE else GONE
                }
            }
        }

        fragmentMovieBinding.run {

            btnSaveMovie.text = "Atualizar";

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