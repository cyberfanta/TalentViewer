package com.cyberfanta.talentviewer.views.cards

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.databinding.CardOpportunityBinding
import com.cyberfanta.talentviewer.models.OpportunityItem
import com.squareup.picasso.Picasso

class OpportunitiesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val viewBinding = CardOpportunityBinding.bind(view)

    fun bind (item: OpportunityItem?){
        Picasso.get().load(item?.organizations?.get(0)?.picture).into(viewBinding.organizationsPicture)
        viewBinding.objective.text = item?.objective
        viewBinding.organizationsName.text = item?.organizations?.get(0)?.name
        val string = item?.compensation?.data?.minAmount.toString() + " " + item?.compensation?.data?.currency + " - " + item?.compensation?.data?.maxAmount.toString() + " " + item?.compensation?.data?.currency + " " + item?.compensation?.data?.periodicity
        viewBinding.compensation.text = string
    }
}