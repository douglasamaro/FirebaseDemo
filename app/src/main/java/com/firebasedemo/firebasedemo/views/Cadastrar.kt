package com.firebasedemo.firebasedemo.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.firebasedemo.firebasedemo.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.firebasedemo.firebasedemo.R.drawable.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_cadastrar.*
import kotlinx.android.synthetic.main.activity_cadastrar.tc_email
import kotlinx.android.synthetic.main.activity_cadastrar.tc_senha
import kotlinx.android.synthetic.main.activity_login.*

class Cadastrar : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        progressBarC.visibility = View.GONE

        tv_cadastrar.setOnClickListener{view ->
            progressBarC.visibility = View.VISIBLE
            if (tc_nome.text.toString() == ""){
                Snackbar.make(view, "Preencha o campo nome", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else if (tc_nome.text.toString().length <= 3){
                Snackbar.make(view, "insira um nome maior do que 3 letras", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else if (tc_email.text.toString() == "") {
                Snackbar.make(view, "Preencha o campo email", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else if (!tc_email.text.toString().contains("@") || !tc_email.text.toString().contains(".") || tc_email.text.toString().length < 9){
               Snackbar.make(view, "Insira um E-mail valido", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else if (tc_senha.text.toString() == "") {
                Snackbar.make(view, "Preencha o campo senha", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else if (tc_senha.text.toString().length > 9 || tc_senha.text.toString().length < 6) {
                Snackbar.make(view, "senha deve sar maior que 6 e menor que 9", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            } else {
                Cadastrar(view)
            }
        }

        tv_navegacao1.setOnClickListener{
            val inte = Intent(this, Login::class.java)
            inte.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(inte)
        }

        if(!show_passwordc.isChecked) {
            show_passwordc.buttonDrawable = getDrawable(ic_eye_closed)
            tc_senha.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        show_passwordc.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                show_passwordc.buttonDrawable = getDrawable(ic_eye_open)
                tc_senha.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                show_passwordc.buttonDrawable = getDrawable(ic_eye_closed)
                tc_senha.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun Cadastrar(view: View) {
        progressBarC.visibility = View.VISIBLE
        val nome: String = tc_nome.text.toString()
        val email: String = tc_email.text.toString()
        val senha: String = tc_senha.text.toString()


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!


                        val profileUpdates = userProfileChangeRequest {
                            displayName = nome
                        }
                        firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()
                                } else {
                                    progressBarC.visibility = View.GONE
                                    Toast.makeText(this, "falha ao salvar o nome", Toast.LENGTH_LONG).show()
                                }
                            }
                       Snackbar.make(view, "Registrado com Sucesso", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show()
                    } else {
                        progressBarC.visibility = View.GONE
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }
}