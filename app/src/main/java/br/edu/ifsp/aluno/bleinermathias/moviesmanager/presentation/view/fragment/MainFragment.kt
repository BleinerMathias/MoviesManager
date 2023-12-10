package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.FragmentMainBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.adapter.MovieAdapter
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.viewModel.MovieViewModel

class MainFragment : Fragment() {

    // Instanciar o binding
    private lateinit var fragmentMainBinding: FragmentMainBinding

    private val moviesList: MutableList<Movie> = mutableListOf()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(moviesList)
    }

    // Navigator
    private val navigatorController: NavController by lazy {
        findNavController()
    }

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
        const val MOVIE_FRAGMENT_REQUEST_KEY = "MOVIE_FRAGMENT_REQUEST_KEY"
    }

    // ViewModel
    private val movieViewModel:MovieViewModel  by viewModels {
        MovieViewModel.MovieViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(MOVIE_FRAGMENT_REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == MOVIE_FRAGMENT_REQUEST_KEY) {

                val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(EXTRA_MOVIE, Movie::class.java)
                } else {
                    bundle.getParcelable(EXTRA_MOVIE)
                }

                movie?.also { receivedMovie ->
                    moviesList.indexOfFirst { it.id == receivedMovie.id }.also { position ->
                        if (position != -1) {  // Task editada
                            //movieViewModel.editTask(receivedTask)
                            moviesList[position] = receivedMovie
                            movieAdapter.notifyItemChanged(position)
                        } else {
                            movieViewModel.createMovie(receivedMovie)
                            moviesList.add(receivedMovie)
                            movieAdapter.notifyItemInserted(moviesList.lastIndex)
                        }
                    }
                }

                // Hiding soft keyboard
                (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    fragmentMainBinding.root.windowToken,
                    HIDE_NOT_ALWAYS
                )
            }
        }

        // Para observar o mutable list
        movieViewModel.moviesMutableLiveData.observe(requireActivity()){movies ->
            // Alterar para verifcar somente as célular específicas
            moviesList.clear()
            movies.forEachIndexed{index, movie ->
                moviesList.add(movie)
                movieAdapter.notifyItemChanged(index) // notifica o adapter que um indice foi alterado
            }
        }

        movieViewModel.getMovies()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        // action bar da activity main
        (activity as AppCompatActivity)?.supportActionBar?.title = getString(R.string.my_movies)

        fragmentMainBinding = FragmentMainBinding.inflate(inflater,container,false).apply {
            recyclerViewMovies.layoutManager = LinearLayoutManager(context)

            // Setar um adapter para as célular do recycler
            recyclerViewMovies.adapter = movieAdapter

            // Botão fluante
            btnAddNewMovie.setOnClickListener {
                navigatorController.navigate(
                    MainFragmentDirections.actionMovieListToMovieFragment(null,false)
                )
            }
        }
        // retorna a view
        return fragmentMainBinding.root
    }

}