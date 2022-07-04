package com.example.test.ui.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ContactListBinding
import com.example.test.model.ContactDetails
import com.example.test.model.Contacts
import com.example.test.ui.adapter.ContactAdapter

class ContactViewHolder(
    private val context: Context,
    private val bind: ContactListBinding,
    private val listener: ContactAdapter.ItemListener
) : RecyclerView.ViewHolder(bind.root) {

    init {

    }

    private lateinit var data: ContactDetails
    fun bind(item: ContactDetails) {
        data = item

        bind.phoneNumber.text = data.detail.phoneNumber
        bind.name.text = data.detail.name

    }


}

