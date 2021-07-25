package com.cyberfanta.talentviewer.views.cards

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.CardOpportunityBinding
import com.cyberfanta.talentviewer.models.OpportunityItem
import com.squareup.picasso.Picasso

class OpportunitiesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val viewBinding = CardOpportunityBinding.bind(view)

    fun bind (item: OpportunityItem?){
        Picasso.get().load(item?.organizations?.get(0)?.picture).into(viewBinding.organizationsPicture)
        viewBinding.objective.text = item?.objective
        viewBinding.organizationsName.text = item?.organizations?.get(0)?.name
        var string = ""
        if (item?.compensation?.visible!!) {
            if (!(item.compensation.data?.minAmount == null))
                string += item.compensation.data.minAmount.toString() + " " + item.compensation.data.currency
            if (!(item.compensation.data?.maxAmount == null))
                string += " - " + item.compensation.data.maxAmount.toString() + " " + item.compensation.data.currency
            if (!(item.compensation.data?.periodicity == null))
                string += " " + item.compensation.data.periodicity
        } else
            string += this.itemView.context.getString(R.string.card_compensation_hidden)
        viewBinding.compensation.text = string
    }
}