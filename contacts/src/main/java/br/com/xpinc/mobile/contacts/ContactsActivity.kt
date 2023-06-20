package br.com.xpinc.mobile.contacts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import br.com.xpinc.mobile.contacts.adapter.ContactsAdapter
import br.com.xpinc.mobile.contacts.repository.ContactsRepository
import br.com.xpinc.mobile.contacts.service.ContactsApiService
import br.com.xpinc.mobile.contacts.service.ContactsApiServiceImpl

/**
 * O desafio consiste numa listagem de contatos com uma busca por texto,
 * é esperado que a listagem e a busca sejam implementadas de maneira satisfatória em uma
 * arquitetura escalável com testes unitários e injeção de dependências com Dagger 2.
 * Pode-se fazer uso de RxJava, Flow, Coroutines ou qualquer outra tecnologia que você tenha familiaridade.
 *
 * Requisitos:
 * 1 - Exibir a lista de contatos retornada pelo repository
 * 2 - Usar uma arquitetura para a view: MVVM / MVI / MVP
 * 3 - Implementar busca de contatos durante a digitação do texto
 * 4 - Testes unitários
 * 5 - Usar dagger para prover as dependências
 *
 * Bônus: Paginação da lista de contatos (não é eliminatório)
 */
class ContactsActivity : AppCompatActivity() {

    private val service: ContactsApiService by lazy { ContactsApiServiceImpl(resources) }
    private val repository by lazy { ContactsRepository(service) }
    private val viewModel by viewModels<ContactsViewModel> { ContactsViewModelFactory(repository) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private val searchView by lazy { findViewById<SearchView>(R.id.searchView) }
    private val adapter by lazy { ContactsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        recyclerView.adapter = adapter
    }
}