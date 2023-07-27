package com.abisayo.chemfootball.manageLMS

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.SelectClassActivity
import com.abisayo.chemfootball.databinding.ActivityAddUserBinding
import com.abisayo.chemfootball.models.Credentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private lateinit var database : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Get references to the EditText views
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val specialCodeEditText: EditText = findViewById(R.id.specialCodeEditText)

        // Get reference to the Submit button
        val submitButton: Button = findViewById(R.id.submitButton)

        // Set up click listener for the Submit button
        submitButton.setOnClickListener {
            // Get the input from the EditText views
            val name = nameEditText.text.toString()
            val specialCode = specialCodeEditText.text.toString()

            // Call the function and pass the inputs
            executeFunction(name, specialCode)

        }
    }

    // Function that will be executed when the Submit button is clicked
    private fun executeFunction(name: String, specialCode: String) {
        createNewUser()
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val specialCodeEditText: EditText = findViewById(R.id.specialCodeEditText)
        database = FirebaseDatabase.getInstance().getReference("PlayersCred")
        val credential = Credentials(name, specialCode)
        if (specialCode != null) {
            database.child("$specialCode").setValue(credential).addOnSuccessListener {
                val name = nameEditText.text.toString()
                val specialCode = specialCodeEditText.text.toString()

                nameEditText.text.clear()
                specialCodeEditText.text.clear()
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createNewUser()  {
        val email = binding.emailEditText.text.toString().trim()
        val pass = binding.passwordEditText.text.toString().trim()
        val confirmPass = binding.confirmPasswordEditText.text.toString().trim()

        if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (pass == confirmPass) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.emailEditText.text?.clear()
                        binding.passwordEditText.text?.clear()
                        binding.confirmPasswordEditText.text?.clear()
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