package com.abisayo.chemfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class DisplaySpecialCodeActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_special_code)

        val textView: TextView = findViewById(R.id.copyTextView)

        val  userID = FirebaseAuth.getInstance().currentUser?.uid

        textView.text = "$userID"

        
    }
}