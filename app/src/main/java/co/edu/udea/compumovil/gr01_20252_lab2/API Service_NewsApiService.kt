package co.edu.udea.compumovil.gr01_20252_lab2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Modelos de datos

data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val publishedAt: String,
    val urlToImage: String?,
    val content: String,
    val category: String
)

// Interface del API
interface NewsApiService {

    @GET("articles")
    suspend fun getArticles(
        @Query("category") category: String? = null
    ): List<Article>

    @GET("articles/trending")
    suspend fun getTrendingArticles(): List<Article>

    @GET("articles/latest")
    suspend fun getLatestArticles(): List<Article>

    companion object {
        private const val BASE_URL = "https://68f50dbcb16eb6f468363f80.mockapi.io/api/v1"

        fun create(): NewsApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(NewsApiService::class.java)
        }
    }
}

// Repositorio

class NewsRepository(private val apiService: NewsApiService) {

    suspend fun getArticles(category: String? = null): Result<List<Article>> {
        return try {
            val articles = apiService.getArticles(category)
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrendingArticles(): Result<List<Article>> {
        return try {
            val articles = apiService.getTrendingArticles()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLatestArticles(): Result<List<Article>> {
        return try {
            val articles = apiService.getLatestArticles()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}