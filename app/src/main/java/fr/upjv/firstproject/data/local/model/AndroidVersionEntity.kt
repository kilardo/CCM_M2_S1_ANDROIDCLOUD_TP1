package fr.upjv.firstproject.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "android_version")
data class AndroidVersionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "version")
    val version: String,

    @ColumnInfo(name = "year")
    val year: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "lts")
    val lts: Boolean,

    @ColumnInfo(name = "numberOfUser")
    val numberOfUser: Long,
)
