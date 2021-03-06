package com.example.sqlliterecycler

//import android.R
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView


class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
   var container: LinearLayout
    var name: TextView
    var ph_no: TextView
    var deleteContact: ImageView
    var editContact: ImageView

    init {
        container = itemView.findViewById(R.id.item_container)
        name = itemView.findViewById(R.id.contact_name)
        ph_no = itemView.findViewById(R.id.ph_no)
        deleteContact = itemView.findViewById(R.id.delete_contact) as ImageView
        editContact = itemView.findViewById(R.id.edit_contact) as ImageView
    }
}