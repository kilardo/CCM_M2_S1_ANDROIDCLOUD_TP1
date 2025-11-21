package fr.upjv.firstproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.upjv.firstproject.data.model.MyAndroidModelData
import fr.upjv.firstproject.data.repository.AndroidVersionRepository
import fr.upjv.firstproject.ui.model.ItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class AndroidVersionViewModel : ViewModel() {

    private val androidVersionRepository: AndroidVersionRepository by lazy { AndroidVersionRepository() }

    private val androidVersionNames = listOf(
        "Cupcake",
        "Donut",
        "Eclair",
        "Froyo",
        "Gingerbread",
        "Honeycomb",
        "Ice Cream Sandwich",
        "Jelly Bean",
        "KitKat",
        "Lollipop",
        "Marshmallow",
        "Nougat",
        "Oreo",
        "Pie",
    )

    val androidVersionList: StateFlow<List<ItemUi>> =
        androidVersionRepository.selectAllAndroidVersion()
            .map { androidObjectEntities: List<MyAndroidModelData> ->
                androidObjectEntities
                    .sortedWith(
                        compareBy<MyAndroidModelData> { it.versionName }
                            .thenBy { it.versionNumber }
                    )
                    .groupBy { androidModelData -> androidModelData.versionName }
                    .flatMap { (versionName, itemsOfGroup) ->
                        buildList {
                            add(
                                ItemUi.Header(
                                    title = versionName,
                                    image = itemsOfGroup.first().image
                                )
                            )

                            addAll(itemsOfGroup.map { each ->
                                ItemUi.Item(
                                    versionName = each.versionName,
                                    versionNumber = each.versionNumber,
                                    year = each.year,
                                    image = each.image,
                                    lts = each.lts,
                                    numberOfUser = each.numberOfUser,
                                )
                            })

                            // Footer : plage d'années
                            val years = itemsOfGroup.map { it.year }.distinct().sorted()
                            val footerText = when {
                                years.isEmpty() -> null
                                years.size == 1 -> "Période de sortie : ${years.first()}"
                                else -> "Période de sortie : ${years.first()} - ${years.last()}"
                            }

                            if (footerText != null) {
                                add(ItemUi.Footer(title = footerText))
                            }
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.Lazily
            )

    fun insertAndroidVersion() {
        viewModelScope.launch(Dispatchers.IO) {
            val randomName = androidVersionNames.random()
            val randomVersion = Random.nextInt(0, 10)
            val randomYear = Random.nextInt(2000, 2023)
            val randomNumberOfUser = Random.nextLong(1000, 1_000_000)
            val randomIsLts = Random.nextBoolean()
            androidVersionRepository.insertAndroidVersion(
                MyAndroidModelData(
                    versionName = randomName,
                    versionNumber = "$randomVersion",
                    year = "$randomYear",
                    image = "https://picsum.photos/200/300",
                    lts = randomIsLts,
                    numberOfUser = randomNumberOfUser,
                )
            )
        }
    }


    fun deleteAllAndroidVersion() {
        viewModelScope.launch(Dispatchers.IO) {
            androidVersionRepository.deleteAllAndroidVersion()
        }
    }

    fun deleteAndroidVersion(item: ItemUi.Item) {
        viewModelScope.launch(Dispatchers.IO) {
            androidVersionRepository.deleteAndroidVersion(
                MyAndroidModelData(
                    versionName = item.versionName,
                    versionNumber = item.versionNumber,
                    year = item.year,
                    image = item.image,
                    lts = item.lts,
                    numberOfUser = item.numberOfUser,
                )
            )
        }
    }

}
