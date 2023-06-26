package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.abisayo.chemfootball.data.Constants.EXTRA_CLOSE_APP
import com.abisayo.chemfootball.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.textView4.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.editTextTextPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, SelectClassActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (intent.getBooleanExtra(EXTRA_CLOSE_APP, false) ){
            finish()
            exitProcess(0)
        }

        if (firebaseAuth.currentUser != null) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }

}