package ru.example.androidapp.presentation.booking

data class ParentData(
    val parentTitle: String? = null,
    var type: Int = Constants.PARENT,
    var subList: MutableList<ChildData> = ArrayList(),
    var isExpanded: Boolean = false
)

data class ChildData(val childTitle:String)

object Constants {
    const val PARENT = 0
    const val CHILD = 1
}
