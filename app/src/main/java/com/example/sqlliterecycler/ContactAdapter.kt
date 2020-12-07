package com.example.sqlliterecycler

//import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


class ContactAdapter(context: Context, listContacts: ArrayList<Contacts>, private val contactClickListener: ContactClickListener) :
    RecyclerView.Adapter<ContactViewHolder>(), Filterable {
    private val context: Context
    private var listContacts: ArrayList<Contacts>
    private val mArrayList: ArrayList<Contacts>
    private val mDatabase: SqliteDatabase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_list_layout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contacts = listContacts[position]


    holder.container.setOnClickListener {
        contactClickListener.onContactsClickListener(contacts)
    }
        holder.name.setText(contacts.name)
        holder.ph_no.setText(contacts.phno)

        holder.editContact.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                editTaskDialog(contacts)
            }
        })

        holder.deleteContact.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                //delete row from database
               // mDatabase.deleteContact(contacts.id)
                deliteTaskDialog(contacts)

            }
        })
    }


    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
               // this@ContactAdapter.
                listContacts = if (charString.isEmpty()) {

                    //this@ContactAdapter.
                    mArrayList
                } else {
                    val filteredList: ArrayList<Contacts> = ArrayList()
                    for (contacts in mArrayList) {
                        if (contacts.name.toLowerCase(Locale.ROOT).contains(charString)) {
                            filteredList.add(contacts)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listContacts
                return filterResults
            }

            override fun publishResults(
                    charSequence: CharSequence?,
                    filterResults: FilterResults,
            ) {
                listContacts = filterResults.values as ArrayList<Contacts>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    private fun editTaskDialog(contacts: Contacts?) {
        val inflater = LayoutInflater.from(context)
        val subView: View = inflater.inflate(R.layout.add_contact_layout, null)
        val nameField = subView.findViewById(R.id.enter_name) as EditText
        val contactField = subView.findViewById(R.id.enter_phno) as EditText
        if (contacts != null) {
            nameField.setText(contacts.name)
            contactField.setText(contacts.phno)
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Edit contact")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton("EDIT CONTACT") { dialog, which ->
                val name = nameField.text.toString()
                val ph_no = contactField.text.toString()
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(
                            context,
                            "Something went wrong. Check your input values",
                            Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                            context,
                            "Editing contact...",
                            Toast.LENGTH_LONG
                    ).show()
                    mDatabase.updateContacts(Contacts(contacts!!.id, name, ph_no))
                    //refresh the activity
                    (context as Activity).finish()
                    context.startActivity((context as Activity).intent)
                }
            }
        builder.setNegativeButton("CANCEL") { dialog, which ->
                    Toast.makeText(
                            context,
                            "Task cancelled",
                            Toast.LENGTH_LONG
                    ).show()
                }
        builder.show()
    }



    private fun deliteTaskDialog(contacts: Contacts?) {

        if (contacts == null) {

        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Delite contact")
        //builder.setView(delitesubView)
        builder.create()
        builder.setPositiveButton("DELITE CONTACT") { dialog, which ->

            Toast.makeText(
                    context,
                    "Deliting contact...",
                    Toast.LENGTH_LONG
            ).show()

            mDatabase.deleteContact(contacts?.id!!)

                //refresh the activity
                (context as Activity).finish()
                context.startActivity((context as Activity).intent)
            }

        builder.setNegativeButton("CANCEL") { dialog, which ->
            Toast.makeText(
                    context,
                    "Task cancelled",
                    Toast.LENGTH_LONG
            ).show()
        }
        builder.show()
    }




    init {
        this.context = context
        this.listContacts = listContacts
        mArrayList = listContacts
        mDatabase = SqliteDatabase(context)
    }
}
