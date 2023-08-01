package com.abisayo.chemfootball.displaySS2Courses

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.SelectPlayers.SelectPlayersActivity
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.manageLMS.EdittingQuestionActivity
import com.abisayo.chemfootball.models.Courses
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SS2CoursesAdapter(val admin: String): RecyclerView.Adapter<SS2CoursesAdapter.MyViewHolder>() {

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

        val itemView = holder.itemView

        holder.courseTitle.text = currentitem.courseTitle
        val courseTitle = currentitem.courseTitle
        val repeated = currentitem.note
        val classs = currentitem.clas
        val question = currentitem.question
        val optionA = currentitem.option1
        val optionB = currentitem.option2
        val optionC = currentitem.option3
        val optionD = currentitem.option4
        val answer = currentitem.answer
        val timer = currentitem.timer





        if (admin == "Admin") {
            holder.deleteImg.visibility = View.VISIBLE
        }

        holder.deleteImg.setOnClickListener {
            classs?.let {
                if (courseTitle != null) {
                    if (question != null) {
                        deleteData(it, courseTitle, question, itemView)
                    }
                }
            }

            if (classs != null) {
                if (courseTitle != null) {
                    deleteData2(classs, courseTitle, itemView)
                }
            }
        }





        itemView.setOnClickListener {
            if (admin == "Admin") {
                val intent = Intent(itemView.context, EdittingQuestionActivity::class.java)
                intent.putExtra(Constants.CLASS, "SS2")
                intent.putExtra("re", courseTitle)
                intent.putExtra("question", question)
                intent.putExtra("option1", optionA)
                intent.putExtra("option2", optionB)
                intent.putExtra("option3", optionC)
                intent.putExtra("option4", optionD)
                intent.putExtra("answer", answer)
                intent.putExtra("repeated", repeated)
                intent.putExtra("timer", timer)
                itemView.context.startActivity(intent)

            }
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            database = FirebaseDatabase.getInstance().getReference("Scores")
            if (userID != null) {
                database.child("$userID, $courseTitle").get().addOnSuccessListener {
                    if (it.exists()) {
                        val courseTitles = it.child("courseTitle").getValue(String::class.java)!!
                        if (repeated == "Once") {
                            Toast.makeText(itemView.context, "Access Denied", Toast.LENGTH_SHORT).show()
                        } else {
                            if (admin != "Admin") {
                                val intent = Intent(itemView.context, SelectPlayersActivity::class.java)
                                intent.putExtra(Constants.CLASS, "SS2")
                                intent.putExtra("re", courseTitle)
                                itemView.context.startActivity(intent)
                            }
                        }


                    } else { if (admin != "Admin") {
                        val intent = Intent(itemView.context, SelectPlayersActivity::class.java)
                        intent.putExtra(Constants.CLASS, "SS2")
                        intent.putExtra("re", courseTitle)
                        itemView.context.startActivity(intent)
                    }
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
        val deleteImg : ImageView = itemView.findViewById(R.id.img_del)

        init {

            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }


    }


    private fun deleteData(classs : String, topic: String, question : String, g: View ) {

        var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("$classs, $topic")
        database.child("$question").removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(g.context, "Successfuly Deleted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(g.context, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteData2(classs : String, topic: String, g: View ) {

        var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("$classs")
        database.child("$topic").removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(g.context, "Successfuly Deleted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(g.context, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

}