package com.cyberfanta.talentviewer.views.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.CardPeopleBinding
import com.cyberfanta.talentviewer.models.PeopleItem
import com.squareup.picasso.Picasso

class PeoplesAdapter (private val items: MutableMap<String, PeopleItem>) : RecyclerView.Adapter<PeoplesAdapter.PeoplesViewHolder>(){
    //Internal Variables
    private var itemClickListener: OnItemClickListener? = null
    private var bottomReachedListener: OnBottomReachedListener? = null

    //Listeners Interfaces
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnBottomReachedListener {
        fun onBottomReached(position: Int)
    }

    //Listener Implementations
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun setOnBottomReachedListener(listener: OnBottomReachedListener) {
        this.bottomReachedListener = listener
    }

    class PeoplesViewHolder (view: View, itemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(view) {
        private val viewBinding = CardPeopleBinding.bind(view)

        fun bind (item: PeopleItem){
            item.picture?.let { Picasso.get().load(item.picture).into(viewBinding.picture) }
            item.name?.let{ viewBinding.name.text = item.name }
            item.professionalHeadline?.let { viewBinding.professionalHeadline.text = item.professionalHeadline }
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position)
                }
            }
        }
    }

    //Inflate the card created into the recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeoplesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_people, parent, false)
        return PeoplesViewHolder(view, itemClickListener)
    }

    //Bind an object with a card item
    override fun onBindViewHolder(holder: PeoplesViewHolder, position: Int) {
        val itemlist = items.values
        val item : PeopleItem = itemlist.elementAt(position)
        holder.bind(item)

        if (position > itemCount - 20)
            bottomReachedListener?.onBottomReached(position)
    }

    //Return item count
    override fun getItemCount(): Int = items.size
}