package br.com.xpinc.mobile.contacts.repository

import br.com.xpinc.mobile.contacts.ContactsApiService
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single

class ContactsRepository(
    private val service: ContactsApiService,
) {

    suspend fun getContacts(): List<Contact> =
        service.getContacts()

    suspend fun getContactsByPage(page: Int): List<Contact> =
        service.getContactsByPage(page)

    fun getSingleContacts(): Single<List<Contact>> =
        service.getSingleContacts()

    fun getSingleContactsByPage(page: Int): Single<List<Contact>> =
        service.getSingleContactsByPage(page)
}