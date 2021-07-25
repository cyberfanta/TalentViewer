package com.cyberfanta.talentviewer.views.cards

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.databinding.CardPeopleBinding
import com.cyberfanta.talentviewer.models.PeopleItem
import com.squareup.picasso.Picasso

class PeoplesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val viewBinding = CardPeopleBinding.bind(view)

    fun bind (item: PeopleItem?){
        Picasso.get().load(item?.picture).into(viewBinding.picture)
        viewBinding.name.text = item?.name
        viewBinding.professionalHeadline.text = item?.professionalHeadline
    }
}