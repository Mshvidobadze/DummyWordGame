package com.adjarabet.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordsListAdapter(private val wordsList: ArrayList<String>): RecyclerView.Adapter<WordsListAdapter.WordsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val view: View = layoutInflater.inflate(R.layout.message_layout_left, parent, false)

//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_layout_right,parent, false)

        var itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.message_layout_left,
            parent,
            false
        )


        return WordsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        val currentWords = wordsList[position]
        holder.botAnswer.text = currentWords
        holder.userAnswer.text = currentWords
        if(position % 2 == 0){
            holder.botLinear.visibility = View.INVISIBLE
        }else{
            holder.userLinear.visibility = View.INVISIBLE
        }

    }



    override fun getItemCount(): Int {
        return wordsList.size
    }

    class WordsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val botLinear: LinearLayout = itemView.findViewById(R.id.botLinearLayout)
        val userLinear: LinearLayout = itemView.findViewById(R.id.userLinearLayout)
        val botAnswer: TextView = itemView.findViewById(R.id.txtBotAnswer)
        val userAnswer: TextView = itemView.findViewById(R.id.txtUserAnswer)

    }



}