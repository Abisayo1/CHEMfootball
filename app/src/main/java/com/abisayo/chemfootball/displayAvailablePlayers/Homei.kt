package com.abisayo.chemfootball.displayAvailablePlayers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abisayo.chemfootball.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
private lateinit var viewModels: AvailablePlayersViewModel
private lateinit var availablePlayersRecyclerView: RecyclerView
private lateinit var adapters : AvailablePlayersAdapter


class Homei : Fragment() {
    var clas: String? = null
    var player: String? = null
    var name : String? = null
    var topic: String? = null
    var admin: String? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_homei, container, false)

        val activity = activity as? DisplayAvailablePlayersActivity
        clas = activity?.getClass()
        player = activity?.getPLayer()
        name = activity?.getName()
        topic = activity?.getTopic()
        admin = activity?.getAdmin()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Homei().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        availablePlayersRecyclerView = view.findViewById(R.id.recyclerView)
        availablePlayersRecyclerView.layoutManager = LinearLayoutManager(context)
        availablePlayersRecyclerView.setHasFixedSize(true)
        adapters = AvailablePlayersAdapter(clas!!, player!!, name!!, topic!!, admin!!)
        availablePlayersRecyclerView.adapter = adapters

        adapters.setonItemClickListener(object : AvailablePlayersAdapter.onItemClickListener{
            override fun onItemClicked(position: Int) {



            }

        })

        viewModels = ViewModelProvider(this).get(AvailablePlayersViewModel::class.java)

        viewModels.allPlayers.observe(viewLifecycleOwner, Observer {
            adapters.updateplayerList(it)
        })

    }
}