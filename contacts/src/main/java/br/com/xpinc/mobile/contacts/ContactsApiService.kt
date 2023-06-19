package br.com.xpinc.mobile.contacts

import android.content.res.Resources
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single
import kotlinx.coroutines.delay
import org.json.JSONArray
import java.util.concurrent.TimeUnit
import kotlin.random.Random

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
     * Retrieves contacts as a single based on a query.
     *
     * @param query The search query.
     * @return The single containing the list of contact responses.
     */
    fun getSingleContacts(query: String): Single<List<Contact>>

    /**
     * Retrieves contacts for a specific page as a single based on a query.
     *
     * @param page The page number.
     * @param query The search query.
     * @return The single containing the list of contact responses for the specified page.
     */
    fun getSingleContactsByPage(page: Int, query: String): Single<List<Contact>>
}

class ContactsApiServiceImpl(
    private val resources: Resources,
    private val sleepTime: Long = 800,
    private val errorLimit: Int = 100,
    private val contactLimitSize: Int = 100,
) : ContactsApiService {
    private var lastPageIndex = 0
    private var previousPageIndex = 0
    private val memoryCache = mutableListOf<Contact>()

    override suspend fun getContacts(query: String): List<Contact> {
        delay(sleepTime)
        simulateError()
        return loadFromFile(paging = false).filterByQuery(query)
    }

    override suspend fun getContactsByPage(page: Int, query: String): List<Contact> {
        if (page == 1 || memoryCache.isEmpty()) {
            loadFromFile(paging = true)
        }
        delay(sleepTime)
        simulateError()
        return getPaginated(page, query)
    }

    override fun getSingleContacts(query: String): Single<List<Contact>> =
        Single
            .fromCallable {
                loadFromFile(paging = false).filterByQuery(query)
            }
            .delay(sleepTime, TimeUnit.MILLISECONDS)
            .map {
                simulateError()
                it
            }

    override fun getSingleContactsByPage(page: Int, query: String): Single<List<Contact>> =
        Single.fromCallable {
            if (page == 1 || memoryCache.isEmpty()) {
                loadFromFile(paging = true)
            }
            getPaginated(page, query)
        }
            .delay(sleepTime, TimeUnit.MILLISECONDS)
            .map {
                simulateError()
                it
            }

    private fun loadFromFile(paging: Boolean = false): List<Contact> =
        resources.openRawResource(R.raw.contacts).bufferedReader().use { reader ->
            val json = JSONArray(reader.readText())
            lastPageIndex = 0
            previousPageIndex = 0
            memoryCache.clear()
            for (i in 1..if (paging) json.length() else contactLimitSize) {
                val jsonObject = json.getJSONObject(i - 1)
                memoryCache.add(
                    Contact(
                        id = jsonObject["id"] as String,
                        name = jsonObject["name"] as String,
                        email = jsonObject["email"] as String,
                    )
                )
            }
            memoryCache
        }

    private fun getPaginated(page: Int, query: String = ""): List<Contact> {
        val listFiltered = memoryCache.filterByQuery(query).toList()
        val pageCutIndex = page * 100
        previousPageIndex = if (page == 1) 0 else lastPageIndex
        lastPageIndex =
            if (listFiltered.size > pageCutIndex) pageCutIndex + 1 else listFiltered.size
        return listFiltered.subList(previousPageIndex, lastPageIndex)
    }

    private fun List<Contact>.filterByQuery(query: String) =
        filter {
            it.name.contains(query, true) ||
                it.email.contains(query, true)
        }

    private fun simulateError() {
        val count = Random(System.currentTimeMillis()).nextInt(errorLimit)
        if (count > errorLimit * 0.85) throw IllegalStateException("Unexpected error occurred")
    }
}