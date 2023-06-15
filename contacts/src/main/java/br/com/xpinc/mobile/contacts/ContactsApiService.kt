package br.com.xpinc.mobile.contacts

import android.content.res.Resources
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlinx.coroutines.delay
import org.json.JSONArray

/**
 * Interface that defines the methods to retrieve contacts from the API service.
 */
interface ContactsApiService {

    /**
     * Retrieves all contacts.
     *
     * @return The list of contacts.
     */
    suspend fun getContacts(): List<Contact>

    /**
     * Retrieves contacts for a specific page.
     *
     * @param page The page number.
     * @return The list of contacts for the specified page.
     */
    suspend fun getContactsByPage(page: Int): List<Contact>

    /**
     * Retrieves all contacts as a single.
     *
     * @return The single containing the list of contacts.
     */
    fun getSingleContacts(): Single<List<Contact>>

    /**
     * Retrieves contacts for a specific page as a single.
     *
     * @param page The page number.
     * @return The single containing the list of contacts for the specified page.
     */
    fun getSingleContactsByPage(page: Int): Single<List<Contact>>
}

/**
 * Implementation of the ContactsApiService interface.
 */
class ContactsApiServiceImpl(
    private val resources: Resources,
    private val sleepTime: Long = 800,
    private val errorLimit: Int = 100,
    private val contactLimitSize: Int = 100,
) : ContactsApiService {

    private val mapPaging: MutableMap<Int, List<Contact>> = hashMapOf()

    /**
     * Retrieves all contacts.
     *
     * @return The list of contacts.
     */
    override suspend fun getContacts(): List<Contact> {
        delay(sleepTime)
        simulateError()
        return loadFromFile(paging = false)
    }

    /**
     * Retrieves contacts for a specific page.
     *
     * @param page The page number.
     * @return The list of contacts for the specified page.
     */
    override suspend fun getContactsByPage(page: Int): List<Contact> {
        if (page == 1 || mapPaging.isEmpty()) {
            loadFromFile(paging = true)
        }
        delay(sleepTime)
        simulateError()
        return mapPaging[page].orEmpty()
    }

    /**
     * Retrieves all contacts as a single.
     *
     * @return The single containing the list of contacts.
     */
    override fun getSingleContacts(): Single<List<Contact>> =
        Single.fromCallable {
            simulateError()
            loadFromFile(paging = false)
        }.delay(sleepTime, TimeUnit.MILLISECONDS)

    /**
     * Retrieves contacts for a specific page as a single.
     *
     * @param page The page number.
     * @return The single containing the list of contacts for the specified page.
     */
    override fun getSingleContactsByPage(page: Int): Single<List<Contact>> =
        Single.fromCallable {
            if (page == 1 || mapPaging.isEmpty()) {
                loadFromFile(paging = true)
            }
            simulateError()
            mapPaging[page].orEmpty()
        }.delay(sleepTime, TimeUnit.MILLISECONDS)

    private fun loadFromFile(paging: Boolean = false): List<Contact> =
        resources.openRawResource(R.raw.contacts).bufferedReader().use { reader ->
            val json = JSONArray(reader.readText())
            val list = mutableListOf<Contact>()
            var page = 1
            var lastIndex = 0
            for (i in 1..if (paging) json.length() else contactLimitSize) {
                val jsonObject = json.getJSONObject(i - 1)
                list.add(
                    Contact(
                        id = jsonObject["id"] as String,
                        name = jsonObject["name"] as String,
                        email = jsonObject["email"] as String,
                    )
                )
                if (i % 100 == 0) {
                    mapPaging[page] = list.toList().subList(lastIndex, i)
                    page++
                    lastIndex = i
                }
            }
            list
        }

    private fun simulateError() {
        val count = Random(System.currentTimeMillis()).nextInt(errorLimit)
        if (count > errorLimit * 0.85) throw IllegalStateException("Unexpected error occurred")
    }
}