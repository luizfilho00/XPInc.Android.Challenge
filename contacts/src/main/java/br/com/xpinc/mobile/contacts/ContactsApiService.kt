package br.com.xpinc.mobile.contacts

import android.content.res.Resources
import br.com.xpinc.mobile.contacts.model.Contact
import kotlin.random.Random
import org.json.JSONArray

class ContactsApiService(
    private val resources: Resources,
) {
    fun getContactsFromResource(): List<Contact> {
        Thread.sleep(1500)
        val count = Random(System.currentTimeMillis()).nextInt(100)
        if (count > 85) throw IllegalStateException("Unexpected error occurred")
        return resources.openRawResource(R.raw.contacts).bufferedReader().use { reader ->
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
}