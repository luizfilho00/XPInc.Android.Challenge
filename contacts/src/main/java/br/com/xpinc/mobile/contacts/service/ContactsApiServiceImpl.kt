package br.com.xpinc.mobile.contacts.service

import android.content.res.Resources
import br.com.xpinc.mobile.contacts.R
import br.com.xpinc.mobile.contacts.model.Contact
import io.reactivex.Single
import kotlinx.coroutines.delay
import org.json.JSONArray
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ContactsApiServiceImpl(
    private val resources: Resources,
    private val sleepTime: Long = 800,
    private val errorLimit: Int = 100,
    private val contactLimitSize: Int = 100,
) : ContactsApiService {
    companion object {
        private const val PAGE_SIZE = 100
    }

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
        return getPage(page, query)
    }

    override fun getSingleContacts(query: String): Single<List<Contact>> =
        Single.fromCallable {
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
            getPage(page, query)
        }
            .delay(sleepTime, TimeUnit.MILLISECONDS)
            .map {
                simulateError()
                it
            }

    private fun loadFromFile(paging: Boolean = false): List<Contact> =
        resources.openRawResource(R.raw.contacts).bufferedReader().use { reader ->
            val json = JSONArray(reader.readText())
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

    private fun List<Contact>.filterByQuery(query: String) =
        filter { contact ->
            contact.name.contains(query, true) ||
                contact.email.contains(query, true)
        }

    private fun getPage(page: Int, query: String): List<Contact> {
        val lastPageIndex = (page - 1) * PAGE_SIZE
        val nextPageIndex = page * PAGE_SIZE
        return try {
            val filtered = memoryCache
                .filterByQuery(query)
            filtered.subList(
                lastPageIndex,
                if (filtered.size > PAGE_SIZE) nextPageIndex else filtered.size
            )
        } catch (ex: Exception) {
            emptyList()
        }
    }

    private fun simulateError() {
        val count = Random(System.currentTimeMillis()).nextInt(errorLimit)
        if (count > errorLimit * 0.85) throw IllegalStateException("Unexpected error occurred")
    }
}