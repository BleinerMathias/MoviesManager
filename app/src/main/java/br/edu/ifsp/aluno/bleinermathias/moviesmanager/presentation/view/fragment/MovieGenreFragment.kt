package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.FragmentMovieBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.FragmentMovieGenreBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.MovieGenre
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.viewModel.MovieViewModel
import java.util.UUID

class MovieGenreFragment : Fragment() {

    private lateinit var fragmentMovieGenreBinding: FragmentMovieGenreBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            getString(R.string.subtitle_new_genre)

        fragmentMovieGenreBinding = FragmentMovieGenreBinding.inflate(inflater, container, false)

        fragmentMovieGenreBinding.run {
            btnSaveMovieGenre.setOnClickListener {
                setFragmentResult(MainFragment.MOVIE_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    putParcelable(
                        MainFragment.EXTRA_MOVIE_GENRE, MovieGenre(
                            0,
                            editTextMovieGenre.text.toString()
                        )
                    )
                })
                findNavController().navigateUp()
            }
        }

        return fragmentMovieGenreBinding.root
    }

}