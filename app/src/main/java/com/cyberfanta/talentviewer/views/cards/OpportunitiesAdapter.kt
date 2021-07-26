package com.cyberfanta.talentviewer.views.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.CardOpportunityBinding
import com.cyberfanta.talentviewer.models.OpportunityItem
import com.squareup.picasso.Picasso

class OpportunitiesAdapter (private val items: List<OpportunityItem?>) : RecyclerView.Adapter<OpportunitiesAdapter.OpportunitiesViewHolder>(){
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

    class OpportunitiesViewHolder (view: View, itemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(view) {
        private val viewBinding = CardOpportunityBinding.bind(view)

        fun bind (item: OpportunityItem?) {
            Picasso.get().load(item?.organizations?.get(0)?.picture)
                .into(viewBinding.organizationsPicture)

            item?.objective?.let{ viewBinding.objective.text = item.objective }
            item?.organizations?.get(0)?.name?.let{ viewBinding.organizationsName.text = item.organizations.get(0)?.name }

            var string = this.itemView.context.getString(R.string.card_compensation_hidden)
            item?.compensation?.visible?.let{
                if (item.compensation.visible) {
                    string = ""
                    if (item.compensation.data?.minAmount != null)
                        string += item.compensation.data.minAmount.toString() + " " + item.compensation.data.currency
                    if (item.compensation.data?.maxAmount != null)
                        string += " - " + item.compensation.data.maxAmount.toString() + " " + item.compensation.data.currency
                    if (item.compensation.data?.periodicity != null)
                        string += " " + item.compensation.data.periodicity
                }
            }
            viewBinding.compensation.text = string
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpportunitiesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_opportunity, parent, false)
        return OpportunitiesViewHolder(view, itemClickListener)
    }

    //Bind an object with a card item
    override fun onBindViewHolder(holder: OpportunitiesViewHolder, position: Int) {
        val item : OpportunityItem? = items[position]
        holder.bind(item)

        if (position > itemCount - 12)
            bottomReachedListener?.onBottomReached(position)
    }

    override fun getItemCount(): Int = items.size

}