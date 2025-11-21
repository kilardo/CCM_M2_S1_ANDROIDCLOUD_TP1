package fr.upjv.firstproject.data.repository

import fr.upjv.firstproject.architecture.CustomApplication
import fr.upjv.firstproject.data.local.model.AndroidVersionEntity
import fr.upjv.firstproject.data.mapping.toDataObject
import fr.upjv.firstproject.data.mapping.toRoomObject
import fr.upjv.firstproject.data.model.MyAndroidModelData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AndroidVersionRepository {

    private val androidVersionDao =
        CustomApplication.instance.applicationDatabase.androidVersionDao()


    fun selectAllAndroidVersion(): Flow<List<MyAndroidModelData>> {
        return androidVersionDao.selectAll()
            .map { androidVersionEntity: List<AndroidVersionEntity> ->
                androidVersionEntity.toDataObject()
            }
    }


    fun insertAndroidVersion(myAndroidModelData: MyAndroidModelData) {
        androidVersionDao.insert(myAndroidModelData.toRoomObject())
    }


    fun deleteAllAndroidVersion() {
        androidVersionDao.deleteAll()
    }

    fun deleteAndroidVersion(myAndroidModelData: MyAndroidModelData) {
        androidVersionDao.deleteAndroidVersion(
            name = myAndroidModelData.versionName,
            versionNumber = myAndroidModelData.versionNumber,
            year = myAndroidModelData.year
        )
    }

}
