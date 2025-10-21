package co.edu.udea.compumovil.gr01_20252_lab2.data.local

import androidx.room.*
import co.edu.udea.compumovil.gr01_20252_lab2.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getArticleById(id: String): Article?

    @Query("SELECT * FROM articles WHERE isFavorite = 1 ORDER BY publishedAt DESC")
    fun getFavoriteArticles(): Flow<List<Article>>

    @Query("SELECT id FROM articles WHERE isFavorite = 1")
    suspend fun getFavoriteArticleIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Update
    suspend fun updateArticle(article: Article)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()

    @Query("SELECT DISTINCT category FROM articles")
    fun getAllCategories(): Flow<List<String>>
}