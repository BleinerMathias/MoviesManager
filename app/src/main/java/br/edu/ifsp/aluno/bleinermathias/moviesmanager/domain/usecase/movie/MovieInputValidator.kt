package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.movie

import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.entities.movie.Movie
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.utils.Notification
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.utils.Validator

class MovieInputValidator: Validator<Movie>() {
    override fun validate(movie: Movie): Notification {
        val notification = Notification()

        if(movie == null){
            notification.addError("Filme está nulo")
            return notification
        }

        if(nullOrEmpty(movie.name)){
            notification.addError("Nome está vazio ou nulo")
            return notification
        }

        if(nullOrEmpty(movie.producer)){
            notification.addError("Produtora/estúdio está vazio ou nulo")
            return notification
        }

        if(nullOrEmpty(movie.releaseYear.toString())){
            notification.addError("Ano de lançamento está vazio ou nulo")
            return notification
        }

        if(nullOrEmpty(movie.genre)){
            notification.addError("Genero está vazio ou nulo")
            return notification
        }

        if(nullOrEmpty(movie.rating.toString())){
            notification.addError("Avalição do filme está vazia ou nula")
            return notification
        }

        if(nullOrEmpty(movie.duration.toString())){
            notification.addError("Duração do filme está vazio ou nulo")
            return notification
        }

        return notification

    }
}