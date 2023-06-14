package br.com.xpinc.mobile.contacts.repository

import android.content.res.Resources
import br.com.xpinc.mobile.contacts.R
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single
import kotlinx.coroutines.coroutineScope
import org.json.JSONArray

class ContactsRepository(
    private val resources: Resources,
) {

    suspend fun getContacts(): List<Contact> {
        return try {
            coroutineScope {
                getContactsFromResource()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }

    fun getSingleContacts(): Single<List<Contact>> =
        Single.fromCallable {
            getContactsFromResource()
        }

    private fun getContactsFromResource() =
        resources.openRawResource(R.raw.contacts).bufferedReader().use { reader ->
            val json = JSONArray(reader.readText())
            val list = mutableListOf<Contact>()
            for (i in 0 until 100) {
                val jsonObject = json.getJSONObject(i)
                list.add(
                    Contact(
                        id = jsonObject["id"] as String,
                        name = jsonObject["name"] as String,
                        email = jsonObject["email"] as String,
                    )
                )
            }
            list
        }
}