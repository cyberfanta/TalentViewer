package com.cyberfanta.talentviewer.views.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.models.PeopleItem

class PeoplesAdapter (private val items: List<PeopleItem?>) : RecyclerView.Adapter<PeoplesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeoplesViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        return PeoplesViewHolder(layoutInflater.inflate(R.layout.card_people, parent, false))
    }

    override fun onBindViewHolder(holder: PeoplesViewHolder, position: Int) {
        val item : PeopleItem? = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

}