package com.marcos.chatapplication

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatApplication : Application() {

    companion object {
        private const val TAG = "ChatApplication"
    }

    override fun onCreate() {
        super.onCreate()

        try {
            initializeFirebaseSafely()
        } catch (e: Exception) {
            Log.e(TAG, "Erro na inicialização do Firebase: ${e.message}", e)
        }
    }

    private fun initializeFirebaseSafely() {
        try {

            FirebaseApp.initializeApp(this)

            val firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build()

            try {
                FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings
                Log.d(TAG, "Firestore configurado com sucesso")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao configurar Firestore: ${e.message}", e)
            }

            try {
                val firebaseAppCheck = FirebaseAppCheck.getInstance()

                if (BuildConfig.DEBUG) {
                    firebaseAppCheck.installAppCheckProviderFactory(
                        DebugAppCheckProviderFactory.getInstance()
                    )
                    Log.d(TAG, "App Check configurado em modo DEBUG")
                } else {
                    firebaseAppCheck.installAppCheckProviderFactory(
                        PlayIntegrityAppCheckProviderFactory.getInstance()
                    )
                    Log.d(TAG, "App Check configurado em modo PRODUÇÃO")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao configurar o App Check: ${e.message}", e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro crítico na inicialização do Firebase: ${e.message}", e)
        }
    }
}