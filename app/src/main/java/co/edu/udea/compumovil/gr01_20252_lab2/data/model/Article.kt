package co.edu.udea.compumovil.gr01_20252_lab2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("publishedAt")
    val publishedAt: Long,

    @SerializedName("isFavorite")
    val isFavorite: Boolean = false
)

data class Category(
    val id: String,
    val name: String,
    val icon: String
)

// Respuesta de la API
data class ArticlesResponse(
    @SerializedName("articles")
    val articles: List<Article>
)