package fr.upjv.firstproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.upjv.firstproject.data.local.model.AndroidVersionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AndroidVersionDao {


    @Query("SELECT * FROM android_version ORDER BY name ASC")
    fun selectAll(): Flow<List<AndroidVersionEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(androidVersion: AndroidVersionEntity)


    @Query("DELETE FROM android_version")
    fun deleteAll()

    @Query(
        "DELETE FROM android_version " +
                "WHERE name = :name AND version = :versionNumber AND year = :year"
    )
    fun deleteAndroidVersion(
        name: String,
        versionNumber: String,
        year: String,
    )

}
