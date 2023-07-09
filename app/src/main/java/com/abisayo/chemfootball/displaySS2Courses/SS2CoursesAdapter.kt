package com.abisayo.chemfootball.displaySS2Courses

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.SelectPlayers.SelectPlayersActivity
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.models.Courses
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SS2CoursesAdapter: RecyclerView.Adapter<SS2CoursesAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener
    private lateinit var currentitem : Courses
    private lateinit var database: DatabaseReference

    interface onItemClickListener{
        fun onItemClicked(position: Int)
    }

    fun setonItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    private val courseList: ArrayList<Courses> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.topic_item_view,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        currentitem = courseList[position]

        holder.courseTitle.text = currentitem.courseTitle
        val courseTitle = currentitem.courseTitle
        val repeated = currentitem.note

        val itemView = holder.itemView

        itemView.setOnClickListener {
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            database = FirebaseDatabase.getInstance().getReference("Scores")
            if (userID != null) {
                database.child("$userID, $courseTitle").get().addOnSuccessListener {
                    if (it.exists()) {
                        val courseTitles = it.child("courseTitle").getValue(String::class.java)!!
                        if (repeated == "Once") {
                            Toast.makeText(itemView.context, "Access Denied", Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(itemView.context, SelectPlayersActivity::class.java)
                            intent.putExtra(Constants.CLASS, "SS2")
                            intent.putExtra("re", courseTitle)
                            itemView.context.startActivity(intent)
                        }


                    } else {
                        val intent = Intent(itemView.context, SelectPlayersActivity::class.java)
                        intent.putExtra(Constants.CLASS, "SS2")
                        intent.putExtra("re", courseTitle)
                        itemView.context.startActivity(intent)
                    }
                }.addOnFailureListener {

                    Toast.makeText(itemView.context, "Failed", Toast.LENGTH_SHORT).show()


                }
            }


        }




    }



    override fun getItemCount(): Int {

        return courseList.size
    }


    fun updateCourseList(courseList: List<Courses>) {
        this.courseList.clear()
        this.courseList.addAll(courseList)
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val courseTitle: TextView = itemView.findViewById(R.id.question)

        init {

            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }


    }

}