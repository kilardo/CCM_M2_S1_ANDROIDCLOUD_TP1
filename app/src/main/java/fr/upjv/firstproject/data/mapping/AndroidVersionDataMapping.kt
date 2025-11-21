package fr.upjv.firstproject.data.mapping

import fr.upjv.firstproject.data.local.model.AndroidVersionEntity
import fr.upjv.firstproject.data.model.MyAndroidModelData

fun MyAndroidModelData.toRoomObject(): AndroidVersionEntity {
    return AndroidVersionEntity(
        name = versionName,
        version = versionNumber,
        year = year,
        image = image,
        lts = lts,
        numberOfUser = numberOfUser,
    )
}


fun List<AndroidVersionEntity>.toDataObject(): List<MyAndroidModelData> {
    return this.map { eachItem ->
        MyAndroidModelData(
            versionName = eachItem.name,
            versionNumber = eachItem.version,
            year = eachItem.year,
            image = eachItem.image,
            lts = eachItem.lts,
            numberOfUser = eachItem.numberOfUser,
        )
    }
}
