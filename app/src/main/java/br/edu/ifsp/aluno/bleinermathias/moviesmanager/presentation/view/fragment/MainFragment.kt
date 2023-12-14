package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.FragmentMainBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_FALSE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_TRUE
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.MovieGenre
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.adapter.MovieAdapter
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.adapter.OnMovieTileClickListener
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.viewModel.MovieViewModel
import kotlin.math.log

enum class SortType {
    ALPHABETICAL,
    BY_RATING
}

class MainFragment : Fragment(), OnMovieTileClickListener {

    // Instanciar o binding
    private lateinit var fragmentMainBinding: FragmentMainBinding

    private val moviesList: MutableList<Movie> = mutableListOf()

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(moviesList, this)
    }

    // Navigator
    private val navigatorController: NavController by lazy {
        findNavController()
    }

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
        const val EXTRA_MOVIE_GENRE = "EXTRA_MOVIE_GENRE"
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

                val movieGenre = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(EXTRA_MOVIE_GENRE, MovieGenre::class.java)
                } else {
                    bundle.getParcelable(EXTRA_MOVIE_GENRE)
                }


                movie?.also { receivedMovie ->
                    moviesList.indexOfFirst { it.id == receivedMovie.id }.also { position ->
                        if (position != -1) {
                            movieViewModel.editMovie(receivedMovie)
                            moviesList[position] = receivedMovie
                            movieAdapter.notifyItemChanged(position)
                        } else {
                            movieViewModel.createMovie(receivedMovie)
                            moviesList.add(receivedMovie)
                            movieAdapter.notifyItemInserted(moviesList.lastIndex)
                        }
                    }
                }

                movieGenre?.also { receivedMovieGenre ->
                    showSimpleAlertDialog("O gênero ${receivedMovieGenre} foi adicionado com sucesso!")
                    movieViewModel.createMovieGenre(receivedMovieGenre);
                }

                (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    fragmentMainBinding.root.windowToken,
                    HIDE_NOT_ALWAYS
                )
            }
        }

        // Para observar o mutable list
        movieViewModel.moviesMutableLiveData.observe(requireActivity()){movies ->
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
        (activity as AppCompatActivity)?.supportActionBar?.subtitle = ""

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menuSortByAlphabetic -> {
                        sortMoviesInPlace(SortType.ALPHABETICAL)
                        return true
                    }
                    R.id.menuSortByRating -> {
                        sortMoviesInPlace(SortType.BY_RATING)
                        return true
                    }
                    R.id.menuAddMovieGenre -> {
                        navigatorController.navigate(
                            MainFragmentDirections.actionMovieListToMovieGenreFragment()
                        )
                        return true
                    }
                    else -> return true
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // Funções implementadas do listener do adapter
    override fun onMovieClick(position: Int) = navigateToMovieFragment(position, false)

    override fun onRemoveMovieMenuItemClick(position: Int) {
        movieViewModel.removeMovie(moviesList[position])
        moviesList.removeAt(position)
        movieAdapter.notifyItemRemoved(position)
    }

    override fun onEditMovieMenuItemClick(position: Int) = navigateToMovieFragment(position,true)

    override fun onWatchedCheckBoxClick(position: Int, checked: Boolean) {
        moviesList[position].apply {
            watched = if(checked) MOVIE_WATCHED_TRUE else MOVIE_WATCHED_FALSE
            movieViewModel.editMovie(this)
        }
    }

    // Método para navegar para edição e visualização
    private fun navigateToMovieFragment(position: Int, editMovie: Boolean) {
        moviesList[position].also {
            navigatorController.navigate(
                MainFragmentDirections.actionMovieListToMovieFragment(it, editMovie)
            )
        }
    }

    fun sortMoviesInPlace(sortType: SortType) {
        when (sortType) {
            SortType.ALPHABETICAL -> moviesList.sortBy { it.name }
            SortType.BY_RATING -> moviesList.sortByDescending { it.rating }
        }
        movieAdapter.notifyDataSetChanged()
    }

    private fun showSimpleAlertDialog(text:String) {
        val alertDialogBuilder = AlertDialog.Builder(this.context).apply {
            setMessage(text)
            setPositiveButton("OK") {dialog, _ ->
                dialog.dismiss()
            }

        }

        // Create and show the alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}