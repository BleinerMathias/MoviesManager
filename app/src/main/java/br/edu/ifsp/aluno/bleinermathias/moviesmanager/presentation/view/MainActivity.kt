package br.edu.ifsp.aluno.bleinermathias.moviesmanager.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.R
import br.edu.ifsp.aluno.bleinermathias.moviesmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        setSupportActionBar(activityMainBinding.mainTb)

        supportActionBar?.title = getString(R.string.app_name)

    }
}