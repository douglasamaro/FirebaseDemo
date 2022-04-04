package com.firebasedemo.firebasedemo.views

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebasedemo.firebasedemo.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = Firebase.auth.currentUser

        CoroutineScope(IO).launch {
            withContext(Main){
                delay(3000)
                if (user != null) {
                    val email: String = user.email.toString()
                    Cadastrar(user.displayName.toString())
                } else {
                    Logar()
                }
            }
        }
    }

    private fun Cadastrar(nome: String) {
        Toast.makeText(this, "Bem vindo de volta $nome", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun Logar() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}