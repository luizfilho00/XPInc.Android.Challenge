package br.com.xpinc.mobile.contacts.repository

import br.com.xpinc.mobile.contacts.ContactsApiService
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single
import kotlinx.coroutines.coroutineScope

class ContactsRepository(
    private val service: ContactsApiService,
) {

    suspend fun getContacts(): List<Contact> {
        return try {
            coroutineScope {
                service.getContactsFromResource()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }

    fun getSingleContacts(): Single<List<Contact>> =
        Single.fromCallable {
            service.getContactsFromResource()
        }
}