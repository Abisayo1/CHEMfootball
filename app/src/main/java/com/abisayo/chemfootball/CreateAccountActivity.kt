package com.abisayo.chemfootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.databinding.ActivityCreateAccountBinding
import com.abisayo.chemfootball.models.Credentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView8.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.editTextTextPassword.text.toString().trim()
            val pass = binding.editTextNumberPassword.text.toString().trim()
            val confirmPass = binding.editTextTextPassword2.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            saveCredentials("${binding.email.text}")
                            val intent = Intent(this, SelectClassActivity::class.java)
                            startActivity(intent)
                        }else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveCredentials(name: String) {
        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("PlayersCred")
        val credential = Credentials(name, userID)
        if (userID != null) {
            database.child("$userID").setValue(credential).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}