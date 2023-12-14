package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view.components

import android.app.AlertDialog
import android.content.Context

class Alert(private val context: Context) {

    fun show(text: String) {
        val alertDialogBuilder = AlertDialog.Builder(context).apply {
            setMessage(text)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }

        // Create and show the alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}