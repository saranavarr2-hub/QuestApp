package com.example.questapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)



        val mainView = findViewById<View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }



        val tvGoToRegister = findViewById<TextView>(R.id.registerTextView)

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

            com.google.firebase.FirebaseApp.initializeApp(this)
            auth = FirebaseAuth.getInstance()
            auth = FirebaseAuth.getInstance()

            val emailField = findViewById<EditText>(R.id.et_email)
            val passwordField = findViewById<EditText>(R.id.et_password)
            val btnLogin = findViewById<Button>(R.id.btn_login)


            btnLogin.setOnClickListener {
                val email = emailField.text.toString().trim()
                val pass = passwordField.text.toString().trim()

                if (email.isNotEmpty() && pass.isNotEmpty()) {

                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {

                                Toast.makeText(
                                    this,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }


    }

