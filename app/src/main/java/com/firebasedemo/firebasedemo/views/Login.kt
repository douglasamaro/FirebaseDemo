package com.firebasedemo.firebasedemo.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.firebasedemo.firebasedemo.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_entrar.setOnClickListener {view ->
            if (tv_email.text.toString() == "") {
                Snackbar.make(view, "Preencha o campo email", Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show()
            } else if (tv_senha.text.toString() == "") {
                Snackbar.make(view, "Preencha o campo senha", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else {
                Login(view)
            }
        }

        tv_navegacao.setOnClickListener{
            val inte = Intent(this, Cadastrar::class.java)
            inte.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(inte)
        }
    }

    private fun Login(view: View) {
        val email: String = tv_email.text.toString()
        val senha: String = tv_senha.text.toString()


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        Snackbar.make(view, "bem vindo de volta", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("user_id", firebaseUser.uid)
                        intent.putExtra("email_id", email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }

}