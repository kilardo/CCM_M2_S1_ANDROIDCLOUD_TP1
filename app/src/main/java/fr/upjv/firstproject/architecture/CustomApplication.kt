package fr.upjv.firstproject.architecture

import android.app.Application
import androidx.room.Room
import firebase.RemoteConfigManager
import fr.upjv.firstproject.data.local.CustomRoomDatabase

class CustomApplication : Application() {

    companion object {
        lateinit var instance: CustomApplication
    }

    val applicationDatabase: CustomRoomDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            CustomRoomDatabase::class.java,
            "AndroidVersionsDatabase"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RemoteConfigManager.init()
    }
}
