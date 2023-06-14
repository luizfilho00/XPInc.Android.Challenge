package br.com.xpinc.mobile.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.xpinc.mobile.contacts.repository.ContactsRepository

class ContactsViewModel(
    private val repository: ContactsRepository,
) : ViewModel()

class ContactsViewModelFactory(
    private val repository: ContactsRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactsViewModel(repository) as T
    }
}