package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.FragmentMovieBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_FALSE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_TRUE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.MovieGenre
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie.MovieInputValidator
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.utils.Notification
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.utils.Validator
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.components.Alert
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment.MainFragment.Companion.EXTRA_MOVIE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment.MainFragment.Companion.MOVIE_FRAGMENT_REQUEST_KEY
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.viewModel.MovieViewModel
import java.util.UUID


class MovieFragment : Fragment()  {

    private lateinit var fragmentMovieBinding: FragmentMovieBinding
    private val navigationArgs: MovieFragmentArgs by navArgs()

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModel.MovieViewModelFactory
    }


    private var selectedGenre = ""

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            getString(R.string.movie_details)

        fragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false)

        var alert = Alert(requireContext())

        val receivedMovie = navigationArgs.movie
        if(receivedMovie == null){
            (activity as AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.new_movie)
        }


        movieViewModel.spinnerDataList.observe(requireActivity()){genres ->

            if(genres.size === 0){

                movieViewModel.createMovieGenre( MovieGenre(0,"Ação"))
                movieViewModel.createMovieGenre( MovieGenre(0,"Animação"))
                movieViewModel.createMovieGenre( MovieGenre(0,"Suspense"))
                movieViewModel.getAllMovieGenres()

            }else{
                fragmentMovieBinding.apply {

                    spinnerGenre.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
                    (spinnerGenre.adapter as ArrayAdapter<String>).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedGenre = genres[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            return
                        }
                    }

                    val position = receivedMovie?.let { genres.indexOf(it.genre) }
                    if(position !== null){
                        spinnerGenre.setSelection(position);
                    }
                }
            }

        }

        movieViewModel.getAllMovieGenres()

        receivedMovie?.also { movie ->
            with(fragmentMovieBinding) {

                populateFormRegisterOrUpdateMovie(movie)

                navigationArgs.editMovie.also { editMovie ->
                    ratingBar.setIsIndicator(!editMovie)
                    editTextReleaseYear.isEnabled = editMovie
                    editTextProducer.isEnabled = editMovie
                    editTextDuration.isEnabled = editMovie
                    spinnerGenre.isEnabled = editMovie

                    btnSaveMovie.text = if (editMovie) getString(R.string.btn_edit_movie) else ""
                    btnSaveMovie.visibility = if (editMovie) VISIBLE else GONE

                    checkBoxWatched.isChecked = if(movie.watched === MOVIE_WATCHED_TRUE) true else false

                    if(editMovie){
                        (activity as AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.edit_movie)
                    }
                }
            }
        }

        fragmentMovieBinding.run {

            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                textViewRating.text = "Minha avaliação: ${rating.toString()}"
            }

            btnSaveMovie.setOnClickListener {
                saveOrUpdate(receivedMovie, alert)
            }

            checkBoxWatched.setOnCheckedChangeListener{_, isChecked ->
                if(receivedMovie !== null && isChecked){
                    saveOrUpdate(receivedMovie, alert)
                    findNavController().navigateUp()
                }
            }

        }
        return fragmentMovieBinding.root
    }

    private fun FragmentMovieBinding.saveOrUpdate(
        receivedMovie: Movie?,
        alert: Alert
    ) {

        fragmentMovieBinding.run {
            try {
                val releaseYear = editTextReleaseYear.text.toString().toInt()
                val duration = editTextDuration.text.toString().toInt()
                val rating = ratingBar.rating.toInt()
                val checked = if (checkBoxWatched.isChecked) MOVIE_WATCHED_TRUE else MOVIE_WATCHED_FALSE

                val movie = Movie(
                    receivedMovie?.id ?: UUID.randomUUID().toString(),
                    editTextMovieName.text.toString(),
                    releaseYear,
                    editTextProducer.text.toString(),
                    duration,
                    checked,
                    rating,
                    selectedGenre
                )

                val validator: Validator<Movie> = MovieInputValidator()
                val notification: Notification = validator.validate(movie)

                if (notification.hasErrors()) {
                    alert.show(notification.errorMessage())
                    return
                }

                // Verificações e validações para cada campo
                setFragmentResult(MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    putParcelable(
                        EXTRA_MOVIE, movie
                    )
                })

                findNavController().navigateUp()

            } catch (e: NumberFormatException) {
                alert.show("Existem campos não preenchidos e/ou incorretos")
            }
        }

    }

    private fun FragmentMovieBinding.populateFormRegisterOrUpdateMovie(
        movie: Movie
    ) {
        editTextMovieName.isEnabled = false
        editTextMovieName.setText(movie.name)
        editTextReleaseYear.setText(movie.releaseYear.toString())
        editTextProducer.setText(movie.producer)
        editTextDuration.setText(movie.duration.toString())
        ratingBar.rating = movie.rating.toFloat()
        textViewRating.text = "Minha avaliação: ${movie.rating.toString()}"
    }

}

