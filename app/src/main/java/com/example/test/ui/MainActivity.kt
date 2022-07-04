package com.example.test.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.test.WorkManager.mWorkManager
import com.example.test.databinding.ActivityMainBinding
import com.example.test.model.ContactDetails
import com.example.test.model.Contacts
import com.example.test.ui.adapter.ContactAdapter
import com.example.test.utils.mUtils.mLog
import com.example.test.utils.mUtils.mToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ContactAdapter.ItemListener {

    private lateinit var bind: ActivityMainBinding
    var dataDetailList = ArrayList<ContactDetails>()
    var contactList = ArrayList<Contacts>()
    private var contactAdapter: ContactAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initAllComponents()
        mToast(this@MainActivity,"Please wait")
        CoroutineScope(Dispatchers.IO).launch {

            getDataFromServer()
        }


        /*
        * Step 1
        * Get all the permission
        * Step 2
        * Start the workManager to upload the data
        * Step 3
        * schedule it to 3 hour
        * Step 4
        * Fetch all contact to local database
        * Step 5
        * Add that data to recycler view.
        * */

    }

    private fun getDataFromServer() {

        FirebaseDatabase.getInstance().reference.child("Contacts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("name")) {

                    }
                    mLog("SAtA :: " + snapshot.value)
                    dataDetailList.clear()
                    for (data in snapshot.children) {
                        dataDetailList.add(
                            ContactDetails(
                                data.ref.key.toString(),
                                data.getValue(Contacts::class.java)!!
                            )
                        )
                    }
                    mLog("Products :: " + Gson().toJson(dataDetailList))
                    contactAdapter?.setItems(dataDetailList)


                }

                override fun onCancelled(error: DatabaseError) {
                    mToast(this@MainActivity, error.message)
                }
            })


    }


    override fun onResume() {
        super.onResume()
        checkForPermission()
    }

    private fun checkForPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            //Permission is approved now we can get the contacts from server
            mLog("Permission is approved now we can get the contacts from server")
            mWorkManager.startContactSync(this@MainActivity, 30)

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                mToast(
                    this,
                    "Please provide contacts permission for better usage"
                )
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_CONTACTS),
                    100
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.READ_CONTACTS) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        checkForPermission()
                    }
                }
            }
        }
    }

    private fun initAllComponents() {
        bind.progressBar.visibility = View.GONE
        contactAdapter = ContactAdapter(this, this)
        bind.contactRecycler.adapter = contactAdapter
    }
}