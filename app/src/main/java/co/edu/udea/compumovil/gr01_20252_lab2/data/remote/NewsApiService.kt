package co.edu.udea.compumovil.gr01_20252_lab2.data.remote

import co.edu.udea.compumovil.gr01_20252_lab2.data.model.Article
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {

    @GET("articles")
    suspend fun getArticles(): List<Article>

    @GET("articles")
    suspend fun getArticlesByCategory(
        @Query("category") category: String
    ): List<Article>

    @GET("articles/{id}")
    suspend fun getArticleById(
        @Path("id") id: String
    ): Article
}