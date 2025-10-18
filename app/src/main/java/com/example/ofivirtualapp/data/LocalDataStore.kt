package com.example.ofivirtualapp.data

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object LocalDataStore {
    fun create(context: Context) =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(SupervisorJob())
        ) {
            context.preferencesDataStoreFile("settings") // nombre del archivo
        }
}
