package com.example.test.ui.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ContactListBinding
import com.example.test.model.ContactDetails
import com.example.test.model.Contacts
import com.example.test.ui.viewholder.ContactViewHolder

class ContactAdapter(private val context: Context, private val listener: ItemListener) :
    RecyclerView.Adapter<ContactViewHolder>() {


    interface ItemListener {
    }


    private val items = ArrayList<ContactDetails>()

    fun setItems(items: List<ContactDetails>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding: ContactListBinding =
            ContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        Log.i("SANJAY", "getItemCount: " + items.size)
        return items.size
    }

}
