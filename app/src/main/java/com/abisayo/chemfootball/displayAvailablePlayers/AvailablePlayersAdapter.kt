package com.abisayo.chemfootball.displayAvailablePlayers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abisayo.chemfootball.ConfirmDetailsActivity
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.SelectPlayers.SelectPlayersActivity
import com.abisayo.chemfootball.data.Constants
import com.abisayo.chemfootball.models.Accept
import com.abisayo.chemfootball.models.Credentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AvailablePlayersAdapter(val clas: String, val player: String, val name: String, val topic: String, val admin: String): RecyclerView.Adapter<AvailablePlayersAdapter.MyViewHolder>() {

    private lateinit var database : DatabaseReference

    private lateinit var mListener: onItemClickListener
    private lateinit var currentitem : Credentials
    var code = ""
    var codes = "l"

    interface onItemClickListener{
        fun onItemClicked(position: Int)
    }

    fun setonItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    private val playerList: ArrayList<Credentials> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_player_view,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val  userID = FirebaseAuth.getInstance().currentUser?.uid



        currentitem = playerList[position]

        holder.playerName.text = currentitem.name
        holder.specialCode.text = currentitem.specialCode


        val specialCode = currentitem.specialCode
        val playerName = currentitem.name

        code = "$userID + $specialCode"
        codes = "$specialCode + $userID"

        val itemView = holder.itemView

        if (admin == "Admin") {
            holder.img.visibility = View.VISIBLE
        }

        holder.img.setOnClickListener {
            if (userID != specialCode) {
                deleteData2("${holder.specialCode.text}", itemView)
            }
        }



        itemView.setOnClickListener {
            if (userID == specialCode && admin != "Select") {
                Toast.makeText(itemView.context, "You cannot play against yourself", Toast.LENGTH_SHORT).show()
            } else if (admin != "Select") {
                val intent = Intent(itemView.context, ConfirmDetailsActivity::class.java)
                intent.putExtra("1111", specialCode)
                intent.putExtra("oppName", playerName)
                intent.putExtra(Constants.NAME, name)
                intent.putExtra("aaaaa", "$codes")
                intent.putExtra("123", "$code")
                intent.putExtra(Constants.CLASS, clas)
                intent.putExtra(Constants.PLAYER, player)
                intent.putExtra(Constants.GAME_MODE, "multi_player")
                intent.putExtra("re", topic)
                itemView.context.startActivity(intent)
            } else if (admin == "Select") {
                acceptedPlayers("${holder.specialCode.text}", itemView)
            }
        }
    }

    private fun acceptedPlayers(userID: String, itemView: View) {
        database = FirebaseDatabase.getInstance().getReference("AcceptedPlayers")
        val credential = Accept(clas, topic, userID)
            database.child("$userID").setValue(credential).addOnSuccessListener {
                Toast.makeText(itemView.context, "Successfully updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(itemView.context, "Failed", Toast.LENGTH_SHORT).show()

        }
    }



    override fun getItemCount(): Int {

        return playerList.size
    }


    fun updateplayerList(playerList: List<Credentials>) {
        this.playerList.clear()
        this.playerList.addAll(playerList)
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val playerName: TextView = itemView.findViewById(R.id.student_name)
        val specialCode : TextView = itemView.findViewById(R.id.id)
        val img : ImageView = itemView.findViewById(R.id.img)

        init {

            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }


        }


    }

    private fun deleteData2(specialCode : String, g: View ) {

        var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("PlayersCred")
        database.child("$specialCode").removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(g.context, "Successfully Deleted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(g.context, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

}