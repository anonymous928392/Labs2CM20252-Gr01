package co.edu.udea.compumovil.gr01_20252_lab2.util

import android.content.Context
import co.edu.udea.compumovil.gr01_20252_lab2.R

object CategoryMapper {

    fun getCategoryTranslation(context: Context, category: String): String {
        return when (category.lowercase()) {
            "technology" -> context.getString(R.string.category_technology)
            "sports" -> context.getString(R.string.category_sports)
            "business" -> context.getString(R.string.category_business)
            "health" -> context.getString(R.string.category_health)
            "entertainment" -> context.getString(R.string.category_entertainment)
            "all" -> context.getString(R.string.all_categories)
            else -> category
        }
    }
}