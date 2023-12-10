package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment.MainFragment.Companion.EXTRA_MOVIE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment.MainFragment.Companion.MOVIE_FRAGMENT_REQUEST_KEY
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.viewModel.MovieViewModel
import java.util.UUID


class MovieFragment : Fragment() {

    private lateinit var fragmentMovieBinding: FragmentMovieBinding
    private val navigationArgs: MovieFragmentArgs by navArgs()

    private val spinnerDataList: MutableList<String> = mutableListOf()

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModel.MovieViewModelFactory
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            getString(R.string.movie_details)

        fragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false)


        movieViewModel.spinnerDataList.observe(requireActivity()){genres ->
            spinnerDataList.clear()
            genres.forEachIndexed{index, genre ->
                spinnerDataList.add(genre)
            }
        }
        movieViewModel.getAllMovieGenres()

        val receivedMovie = navigationArgs.movie

        if(receivedMovie == null){
            (activity as AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.new_movie)
        }

        receivedMovie?.also { movie ->
            with(fragmentMovieBinding) {

                editTextMovieName.isEnabled = false
                editTextMovieName.setText(movie.name)
                editTextReleaseYear.setText(movie.releaseYear.toString())
                editTextProducer.setText(movie.producer)
                editTextDuration.setText(movie.duration.toString())
                editTextRating.setText(movie.rating.toString())

                // se receber editavel, passar os valores do campo
                navigationArgs.editMovie.also { editMovie ->
                    editTextReleaseYear.isEnabled = editMovie
                    editTextProducer.isEnabled = editMovie
                    editTextDuration.isEnabled = editMovie
                    editTextRating.isEnabled = editMovie
                    spinnerGenre.isEnabled = editMovie

                    btnSaveMovie.text = if (editMovie) getString(R.string.btn_edit_movie) else ""
                    btnSaveMovie.visibility = if (editMovie) VISIBLE else GONE

                    if(editMovie){
                        (activity as AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.edit_movie)
                    }

                }
            }
        }

        fragmentMovieBinding.run {
            /* val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerDataList)
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             spinnerGenre.adapter = adapter
             spinnerGenre.setSelection(0)*/

            btnSaveMovie.setOnClickListener {
                val checked = if (checkBoxWatched.isChecked) MOVIE_WATCHED_TRUE else MOVIE_WATCHED_FALSE

                val movieName = editTextMovieName.text.toString()
                val releaseYearText = editTextReleaseYear.text.toString()
                val producer = editTextProducer.text.toString()
                val durationText = editTextDuration.text.toString()
                val ratingText = editTextRating.text.toString()
                val selectedGenre = spinnerGenre.selectedItem.toString()

                // Verificações e validações para cada campo
                if (movieName.isBlank()) {
                    showToast("Digite o nome do filme")
                    return@setOnClickListener
                }

                val releaseYear = validateYear(releaseYearText)
                if (releaseYear == null) {
                    return@setOnClickListener
                }

                if (producer.isBlank()) {
                    showToast("Digite o nome do produtor")
                    return@setOnClickListener
                }

                val duration = validateIntField(durationText, "Duração inválida")
                if (duration == null) {
                    return@setOnClickListener
                }

                val rating = validateIntField(ratingText, "Classificação inválida")
                if (rating == null) {
                    return@setOnClickListener
                }

                if (selectedGenre.isBlank()) {
                    showToast("Selecione um gênero")
                    return@setOnClickListener
                }

                setFragmentResult(MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    putParcelable(
                        EXTRA_MOVIE, Movie(receivedMovie?.id ?: UUID.randomUUID().toString(),
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

            checkBoxWatched.setOnCheckedChangeListener{_, isChecked ->
                setFragmentResult(MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    putParcelable(
                        EXTRA_MOVIE, Movie(receivedMovie?.id ?: UUID.randomUUID().toString(),
                            editTextMovieName.text.toString(),
                            editTextReleaseYear.text.toString().toInt(),
                            editTextProducer.text.toString(),
                            editTextDuration.text.toString().toInt(),
                            if(isChecked) MOVIE_WATCHED_TRUE else MOVIE_WATCHED_FALSE,
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

    // Função para exibir uma mensagem Toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Função para validar o ano
    private fun validateYear(yearText: String): Int? {
        if (yearText.isBlank()) {
            showToast("Digite o ano de lançamento")
            return null
        }
        return try {
            yearText.toInt()
        } catch (e: NumberFormatException) {
            showToast("Ano de lançamento inválido")
            null
        }
    }

    private fun validateIntField(valueText: String, errorMessage: String): Int? {
        if (valueText.isBlank()) {
            showToast(errorMessage)
            return null
        }
        return try {
            valueText.toInt()
        } catch (e: NumberFormatException) {
            showToast(errorMessage)
            null
        }
    }


}