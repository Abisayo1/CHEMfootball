package com.abisayo.chemfootball.MultiplayerData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abisayo.chemfootball.R
import com.abisayo.chemfootball.models.Player
import com.abisayo.chemfootball.models.Scores


class DisplayMultiplayerDataAdapter : RecyclerView.Adapter<DisplayMultiplayerDataAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener
    private lateinit var currentitem : Player

    interface onItemClickListener{
        fun onItemClicked(position: Int)
    }

    fun setonItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    private val dataList: ArrayList<Player> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.display_data_item,
            parent, false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        currentitem = dataList[position]

        holder.trialNum.text = currentitem.trialNum
        holder.studentName.text = currentitem.name
        holder.studentScore.text = currentitem.score

        val trialNum = currentitem.trialNum

        val itemView = holder.itemView

    }





    override fun getItemCount(): Int {

        return dataList.size
    }


    fun updatedataList(dataList: List<Player>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val trialNum: TextView = itemView.findViewById(R.id.trialNum)
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentScore: TextView = itemView.findViewById(R.id.student_score)

        init {

            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }


    }

}