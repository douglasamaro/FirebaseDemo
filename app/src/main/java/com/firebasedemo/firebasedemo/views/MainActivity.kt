package com.firebasedemo.firebasedemo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebasedemo.firebasedemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var uEmail: String
    lateinit var uNome: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.my_toolbar))


        val user = Firebase.auth.currentUser
        uEmail = user?.email.toString()
        uNome = user?.displayName.toString()

        tv_nome.text = "Bem vindo, $uNome! "

        sair.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, Login::class.java))
            finish()
        }

        editar.setOnClickListener{
            startActivity(Intent(this@MainActivity, EditarUser::class.java))
            finish()
        }


    }
}
