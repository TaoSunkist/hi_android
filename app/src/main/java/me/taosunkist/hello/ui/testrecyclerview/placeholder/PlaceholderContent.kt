package me.taosunkist.hello.ui.testrecyclerview.placeholder

import com.mooveit.library.Fakeit
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEM_UI_MODELS: MutableList<PlaceholderItemUIModel> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    private val ITEM_UI_MODEL_MAP: MutableMap<Long, PlaceholderItemUIModel> = HashMap()

    private const val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(PlaceholderItemUIModel.fake())
        }
    }

    private fun addItem(itemUIModel: PlaceholderItemUIModel) {
        ITEM_UI_MODELS.add(itemUIModel)
        ITEM_UI_MODEL_MAP[itemUIModel.id] = itemUIModel
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItemUIModel(val id: Long, val title: String, val author: String) {

        companion object {

            fun fake(): PlaceholderItemUIModel {
                val book = Fakeit.book()
                return PlaceholderItemUIModel(id = System.nanoTime(), title = book.title(), author = book.author())
            }
        }

        override fun toString(): String = title
    }
}