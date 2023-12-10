package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.adapter

interface OnMovieTileClickListener {
    fun onMovieClick(position: Int)
    fun onRemoveMovieMenuItemClick(position: Int)
    fun onEditMovieMenuItemClick(position: Int)
    fun onWatchedCheckBoxClick(position: Int, checked: Boolean)
}