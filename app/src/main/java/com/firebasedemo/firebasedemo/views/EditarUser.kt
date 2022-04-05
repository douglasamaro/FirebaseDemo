package com.firebasedemo.firebasedemo.views

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebasedemo.firebasedemo.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_editar_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarUser : AppCompatActivity() {

    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_user)
        //setSupportActionBar(findViewById(R.id.my_toolbar))

//        val standardBottomSheetBehavior = BottomSheetBehavior.from(standard_bottom_sheet)
        progressBar.visibility = View.GONE

        standard_bottom_sheet.visibility = View.GONE
        //standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val usered = Firebase.auth.currentUser
        ed_nome.setText(usered?.displayName)
        ed_email.setText(usered?.email)

            salvar.setOnClickListener { view ->
                if (ed_nome.text.toString() == "" || ed_email.text.toString() == "") {
                    Snackbar.make(view, "os campos não podem estar vazios", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show()
                } else {
                    standard_bottom_sheet.visibility = View.VISIBLE
                  //  standardBottomSheetBehavior.state = View.STATE_EXPANDED
                }
            }

            cancelar.setOnClickListener {
                toMain()
            }

            bottomsheet_button.setOnClickListener { view ->
                if (senha.text.toString() == "") {
                    Snackbar.make(view, "os campos não podem estar vazios", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show()
                } else {
                    login(ed_email.text.toString(), ed_nome.text.toString(), senha.text.toString(), view)
                }
            }

        standard_bottom_sheet.setOnClickListener {
            standard_bottom_sheet.visibility = View.GONE
        }
    }


    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun login(emaild: String, nome: String, senha: String, view: View) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(user?.email.toString(), senha)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        salvar(emaild, nome, view)
                    } else {
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }

    private fun salvar(emaild: String, nome: String, view: View) {
        progressBar.visibility = View.VISIBLE
        var resultado = false
        CoroutineScope(IO).launch {
            val profileUpdates = userProfileChangeRequest {
                displayName = nome
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        resultado = true
                    }
                }
            delay(3000)

            user!!.updateEmail(emaild)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        resultado = true
                    }
                    else {
                        val msg: String = task.exception.toString()
                        Log.d(TAG, "resultado --> $msg")
                    }
                }
            delay(3000)
            if (resultado) {
                Snackbar.make(view, "suas edições foram salvas", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
                delay(1000)
                toMain()
            } else {
                Snackbar.make(view, "falha na edição", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()
            }
        }
    }
}