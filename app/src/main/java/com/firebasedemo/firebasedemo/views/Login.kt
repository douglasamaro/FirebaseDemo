package com.firebasedemo.firebasedemo.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.tl_email
import kotlinx.android.synthetic.main.activity_login.tl_senha

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBarL.visibility = View.GONE

        tv_entrar.setOnClickListener {view ->
            progressBarL.visibility = View.VISIBLE
            if (tl_email.text.toString() == "") {
                Snackbar.make(view, "Preencha o campo email", Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show()
            } else if (tl_senha.text.toString() == "") {
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

        if(!show_passwordl.isChecked) {
            show_passwordl.buttonDrawable = getDrawable(ic_eye_closed)
            tl_senha.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        show_passwordl.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                show_passwordl.buttonDrawable = getDrawable(ic_eye_open)
                tl_senha.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                show_passwordl.buttonDrawable = getDrawable(ic_eye_closed)
                tl_senha.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun Login(view: View) {
        progressBarL.visibility = View.VISIBLE

        val email: String = tl_email.text.toString()
        val senha: String = tl_senha.text.toString()


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
                        progressBarL.visibility = View.GONE
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }

}