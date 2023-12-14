package br.edu.ifsp.aluno.bleinermathias.moviesmanager.domain.usecase.utils

class Notification {

    private val errors: MutableList<Error> = ArrayList()

    fun addError(message: String) {
        addError(message, null)
    }

    fun addError(message: String, e: Exception?) {
        errors.add(Error(message, e))
    }

    fun isCorrect(): Boolean {
        return errors.isEmpty()
    }

    fun hasErrors(): Boolean {
        return !isCorrect()
    }

    data class Error(val message: String, val cause: Exception?)

    fun errorMessage(): String {
        return errors.joinToString(separator = " ,") { it.message }
    }
}