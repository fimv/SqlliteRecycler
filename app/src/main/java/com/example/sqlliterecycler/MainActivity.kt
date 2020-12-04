package com.example.sqlliterecycler

//import android.R
import android.widget.*
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private var mDatabase: SqliteDatabase? = null
    private var allContacts: ArrayList<Contacts> = ArrayList()
    private var mAdapter: ContactAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fLayout = findViewById<View>(R.id.activity_to_do) as FrameLayout
        val contactView = findViewById<View>(R.id.product_list) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        contactView.layoutManager = linearLayoutManager
        contactView.setHasFixedSize(true)
        mDatabase = SqliteDatabase(this)
        allContacts = mDatabase!!.listContacts()
        if (allContacts.size > 0) {
            contactView.visibility = View.VISIBLE
            mAdapter = ContactAdapter(this, allContacts)
            contactView.adapter = mAdapter
        } else {
            contactView.visibility = View.GONE
            Toast.makeText(
                    this,
                    "There is no contact in the database. Start adding now",
                    Toast.LENGTH_LONG
            ).show()
        }
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener ( object : View.OnClickListener{
            override  fun onClick(view: View?) {
                addTaskDialog()
            }
        })
    }


    private fun addTaskDialog() {
        val inflater = LayoutInflater.from(this)
        val subView: View = inflater.inflate(R.layout.add_contact_layout, null)
        val nameField = subView.findViewById(R.id.enter_name) as EditText
        val noField = subView.findViewById(R.id.enter_phno) as EditText
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add new CONTACT")
        builder.setView(subView)
        builder.create()


        builder.setPositiveButton("ADD CONTACT") { dialog, which ->
            val name = nameField.text.toString()
            val ph_no = noField.text.toString()

            if (name == "") {
                Toast.makeText(
                        this,
                        "You forgot to enter name! Check your input values",
                        Toast.LENGTH_LONG
                ).show()
                addTaskDialog()
            }
            else if (ph_no == "") {
                    Toast.makeText(
                            this,
                            "You forgot to enter phone number! Check your input values",
                            Toast.LENGTH_LONG
                    ).show()
                    addTaskDialog()
            }

            else {

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(
                            this,
                            "Something went wrong. Check your input values",
                            Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(applicationContext, "Ok, we adding the contact.", Toast.LENGTH_SHORT).show()

                    // Change the app background color
                    subView.setBackgroundColor(Color.RED)
                }
                val newContact = Contacts(name, ph_no)
                mDatabase!!.addContacts(newContact)
                finish()
                startActivity(intent)
            }
        }


        builder.setNegativeButton("CANCEL") { dialog, which ->
                    Toast.makeText(
                            this,
                            "Task cancelled",
                            Toast.LENGTH_LONG
                    ).show()
                }
        // Display the alert dialog on app interface
        //builder.show()
        builder.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDatabase != null) {
            mDatabase!!.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = MenuItemCompat.getActionView(search) as SearchView
        search(searchView)

       return super.onCreateOptionsMenu(menu)
                    //return true
    }




    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i(TAG,"Llego al querysubmit")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (mAdapter != null) mAdapter!!.filter.filter(newText)
                Log.i(TAG,"Llego al querytextchange")
                return true
            }
        })
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {

                addTaskDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }



    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}

