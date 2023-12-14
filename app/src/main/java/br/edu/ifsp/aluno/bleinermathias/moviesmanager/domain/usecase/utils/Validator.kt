package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.utils

abstract class Validator<T> {
    abstract fun validate(type: T): Notification

    companion object {
        fun nullOrEmpty(string: String?): Boolean {
            return string == null || string.isEmpty()
        }
    }
}