package br.com.xpinc.mobile.contacts.di

import br.com.xpinc.mobile.contacts.ContactsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ContactsModule {

    @ContributesAndroidInjector
    fun bindsActivity(): ContactsActivity
}