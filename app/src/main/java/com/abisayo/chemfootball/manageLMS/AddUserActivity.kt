package com.abisayo.chemfootball.manageLMS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.models.Credentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

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
}