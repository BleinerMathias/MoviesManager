package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.TileMovieBinding
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie.Companion.MOVIE_WATCHED_TRUE

// Recebe a lista para o adapter e os eventos de cliques
// RecyclerView.Adapter<CustomAdapter.ViewHolder>>
class MovieAdapter(private val movieList : List<Movie>, private val onMovieTileClickListener: OnMovieTileClickListener): RecyclerView.Adapter<MovieAdapter.MovieTileViewHolder>() {
    inner class MovieTileViewHolder(tileMovieBinding: TileMovieBinding):RecyclerView.ViewHolder(tileMovieBinding.root){
        // Itens da célula

        val textViewMovieName : TextView = tileMovieBinding.textViewMovieName
        val textViewReleaseYear : TextView = tileMovieBinding.textViewReleaseYear
        val checkBoxWatched : CheckBox = tileMovieBinding.checkBoxWatched
        val textViewGenre: TextView = tileMovieBinding.textViewGenre
        val textViewProducer: TextView = tileMovieBinding.textViewProducer
        val textViewDuration: TextView = tileMovieBinding.textViewDuration
        val textViewRating : TextView = tileMovieBinding.textViewRating

        init {
            // Definindo funções para o adapter - através de menus de contextos
            tileMovieBinding.apply {
                root.run {
                    setOnCreateContextMenuListener { menu, _, _ ->
                        (onMovieTileClickListener as? Fragment)?.activity?.menuInflater?.inflate(
                            R.menu.context_menu,
                            menu
                        )
                        menu?.findItem(R.id.menuRemoveMovie)?.setOnMenuItemClickListener {
                            onMovieTileClickListener.onRemoveMovieMenuItemClick(adapterPosition)
                            true
                        }
                        menu?.findItem(R.id.menuEditMovie)?.setOnMenuItemClickListener {
                            onMovieTileClickListener.onEditMovieMenuItemClick(adapterPosition)
                            true
                        }
                    }
                    setOnClickListener {
                        onMovieTileClickListener.onMovieClick(adapterPosition)
                    }
                }
                checkBoxWatched.run {
                    setOnClickListener {
                        onMovieTileClickListener.onWatchedCheckBoxClick(adapterPosition, isChecked)
                    }
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TileMovieBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ).run { MovieTileViewHolder(this) }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieTileViewHolder, position: Int) {
        movieList[position].let { movie ->
            with(holder) {
                textViewMovieName.text = movie.name
                textViewReleaseYear.text = "${movie.releaseYear.toString()}  -"
                textViewDuration.text = "Duração : ${movie.duration.toString()} minutos"
                textViewGenre.text = movie.genre
                textViewProducer.text = "Estúdio/Produtora : ${movie.producer} "
                textViewRating.text = "Nota: ${movie.rating.toString()}"
                checkBoxWatched.isChecked = movie.watched == MOVIE_WATCHED_TRUE
            }
        }
    }

}