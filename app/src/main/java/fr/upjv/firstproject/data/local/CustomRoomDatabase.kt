package fr.upjv.firstproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.upjv.firstproject.data.local.dao.AndroidVersionDao
import fr.upjv.firstproject.data.local.model.AndroidVersionEntity

@Database(
    entities = [
        AndroidVersionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CustomRoomDatabase : RoomDatabase() {


    abstract fun androidVersionDao(): AndroidVersionDao
}

