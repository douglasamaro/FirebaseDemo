package com.firebasedemo.firebasedemo.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColor
import com.firebasedemo.firebasedemo.R
import com.firebasedemo.firebasedemo.R.drawable.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_cadastrar.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.tv_email
import kotlinx.android.synthetic.main.activity_login.tv_senha

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

//        if (!show_passwordl.isChecked) {
//            show_passwordl.buttonDrawable = getDrawable(ic_eye_closed)
//        }

        show_passwordl.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
               show_passwordl.buttonDrawable = getDrawable(ic_eye_closed)
                Toast.makeText(this, "Checked", Toast.LENGTH_LONG).show()
            }
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