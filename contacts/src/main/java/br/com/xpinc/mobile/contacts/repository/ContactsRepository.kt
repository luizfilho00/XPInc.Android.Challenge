package br.com.xpinc.mobile.contacts.repository

import br.com.xpinc.mobile.contacts.service.ContactsApiService
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single

class ContactsRepository(
    private val service: ContactsApiService,
) {

    suspend fun getContacts(query: String = ""): List<Contact> =
        service.getContacts(query = query)

    suspend fun getContactsByPage(page: Int, query: String = ""): List<Contact> =
        service.getContactsByPage(page = page, query = query)

    fun getSingleContacts(query: String = ""): Single<List<Contact>> =
        service.getSingleContacts(query = query)

    fun getSingleContactsByPage(page: Int, query: String = ""): Single<List<Contact>> =
        service.getSingleContactsByPage(page = page, query = query)
}