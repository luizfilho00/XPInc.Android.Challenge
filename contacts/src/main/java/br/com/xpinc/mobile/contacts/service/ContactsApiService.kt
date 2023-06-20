package br.com.xpinc.mobile.contacts.service

import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single

/**
 * Interface that defines the methods to retrieve contacts from the API service.
 */
interface ContactsApiService {

    /**
     * Retrieves contacts based on a query.
     *
     * @param query The search query.
     * @return The list of contact responses.
     */
    suspend fun getContacts(query: String): List<Contact>

    /**
     * Retrieves contacts for a specific page based on a query.
     *
     * @param page The page number.
     * @param query The search query.
     * @return The list of contact responses for the specified page.
     */
    suspend fun getContactsByPage(page: Int, query: String): List<Contact>

    /**
     * Retrieves contacts as a [Single] based on a query.
     *
     * @param query The search query.
     * @return The [Single] containing the list of contact responses.
     */
    fun getSingleContacts(query: String): Single<List<Contact>>

    /**
     * Retrieves contacts for a specific page as a [Single] based on a query.
     *
     * @param page The page number.
     * @param query The search query.
     * @return The [Single] containing the list of contact responses for the specified page.
     */
    fun getSingleContactsByPage(page: Int, query: String): Single<List<Contact>>
}
