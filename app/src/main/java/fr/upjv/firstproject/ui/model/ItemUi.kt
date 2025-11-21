package fr.upjv.firstproject.ui.model

sealed interface ItemUi {

    data class Item(
        val versionName: String,
        val versionNumber: String,
        val year: String,
        val image: String,
        val lts: Boolean,
        val numberOfUser: Long,
    ) : ItemUi

    data class Header(
        val title: String,
        val image: String,
    ) : ItemUi

    data class Footer(
        val title: String,
    ) : ItemUi
}