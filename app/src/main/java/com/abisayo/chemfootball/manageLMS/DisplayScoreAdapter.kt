package com.abisayo.chemfootball.manageLMS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.models.Scores
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DisplayScoreAdapter : RecyclerView.Adapter<DisplayScoreAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener
    private lateinit var currentitem : Scores
    lateinit var database: DatabaseReference

    interface onItemClickListener{
        fun onItemClicked(position: Int)
    }

    fun setonItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    private val dataList: ArrayList<Scores> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.display_score_item,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        currentitem = dataList[position]

        holder.courseTopic.text = currentitem.courseTitle
        holder.studentName.text = currentitem.studentName
        holder.studentScore.text = currentitem.studentScore
        val studentId = currentitem.userId
        val topic = currentitem.courseTitle






        val courseTitle = currentitem.courseTitle

        val itemView = holder.itemView


        holder.deleteImage.setOnClickListener {
            if (studentId != null) {
                if (topic != null) {
                    deleteData(studentId, topic, itemView)
                }
            }
        }


        itemView.setOnClickListener {

        }

    }





    override fun getItemCount(): Int {

        return dataList.size
    }


    fun updatedataList(scoreList: List<Scores>) {
        this.dataList.clear()
        this.dataList.addAll(scoreList)
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val courseTopic: TextView = itemView.findViewById(R.id.topic)
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentScore: TextView = itemView.findViewById(R.id.student_score)
        val deleteImage: ImageView = itemView.findViewById(R.id.img)



        init {



            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }


    }

    private fun deleteData(studentId : String, topic: String, g: View ) {

        var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Scores")
        database.child("$studentId, $topic").removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(g.context, "Successfuly Deleted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(g.context, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }



}