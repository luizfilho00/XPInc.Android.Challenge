package br.com.xpinc.mobile.contacts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.xpinc.mobile.contacts.R
import br.com.xpinc.mobile.contacts.model.Contact

class ContactsAdapter : RecyclerView.Adapter<ContactsViewHolder>() {
    private val items = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contacts_view_holder, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class ContactsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(contact: Contact) {
        view.findViewById<TextView>(R.id.textViewName).text = "Nome: ${contact.name}"
        view.findViewById<TextView>(R.id.textViewEmail).text = "Email: ${contact.email}"
    }
}