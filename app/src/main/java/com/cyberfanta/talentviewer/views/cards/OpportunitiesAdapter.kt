package com.cyberfanta.talentviewer.views.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.models.OpportunityItem

class OpportunitiesAdapter (private val items: List<OpportunityItem?>) : RecyclerView.Adapter<OpportunitiesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpportunitiesViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        return OpportunitiesViewHolder(layoutInflater.inflate(R.layout.card_opportunity, parent, false))
    }

    override fun onBindViewHolder(holder: OpportunitiesViewHolder, position: Int) {
        val item : OpportunityItem? = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

}