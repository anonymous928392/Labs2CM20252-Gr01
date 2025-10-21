package co.edu.udea.compumovil.gr01_20252_lab2.data.repository

import co.edu.udea.compumovil.gr01_20252_lab2.data.local.ArticleDao
import co.edu.udea.compumovil.gr01_20252_lab2.data.model.Article
import co.edu.udea.compumovil.gr01_20252_lab2.data.remote.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(
    private val api: NewsApiService,
    private val dao: ArticleDao
) {

    fun getArticles(): Flow<Result<List<Article>>> = flow {
        try {
            dao.getAllArticles().collect { localArticles ->
                emit(Result.success(localArticles))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun syncArticles(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Guardar IDs de artículos favoritos antes de borrar
            val favoriteIds = dao.getAllArticles().let { flow ->
                val allArticles = mutableListOf<Article>()
                flow.collect { articles -> allArticles.addAll(articles) }
                allArticles.filter { it.isFavorite }.map { it.id }
            }
            
            // Obtener nuevos artículos de la API
            val articles = api.getArticles()
            
            // Borrar artículos antiguos
            dao.deleteAllArticles()
            
            // Insertar nuevos artículos preservando el estado de favorito
            val articlesWithFavorites = articles.map { article ->
                if (article.id in favoriteIds) {
                    article.copy(isFavorite = true)
                } else {
                    article
                }
            }
            dao.insertArticles(articlesWithFavorites)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getArticlesByCategory(category: String): Flow<List<Article>> {
        return dao.getArticlesByCategory(category)
    }

    suspend fun getArticleById(id: String): Article? {
        return withContext(Dispatchers.IO) {
            dao.getArticleById(id)
        }
    }

    fun getFavoriteArticles(): Flow<List<Article>> {
        return dao.getFavoriteArticles()
    }

    suspend fun toggleFavorite(article: Article) {
        withContext(Dispatchers.IO) {
            dao.updateArticle(article.copy(isFavorite = !article.isFavorite))
        }
    }

    fun getCategories(): Flow<List<String>> {
        return dao.getAllCategories()
    }

    suspend fun refreshArticles(): Result<List<Article>> = withContext(Dispatchers.IO) {
        try {
            // Guardar IDs de artículos favoritos antes de borrar
            val favoriteIds = dao.getAllArticles().let { flow ->
                val allArticles = mutableListOf<Article>()
                flow.collect { articles -> allArticles.addAll(articles) }
                allArticles.filter { it.isFavorite }.map { it.id }
            }
            
            // Obtener nuevos artículos de la API
            val articles = api.getArticles()
            
            // Borrar artículos antiguos
            dao.deleteAllArticles()
            
            // Insertar nuevos artículos preservando el estado de favorito
            val articlesWithFavorites = articles.map { article ->
                if (article.id in favoriteIds) {
                    article.copy(isFavorite = true)
                } else {
                    article
                }
            }
            dao.insertArticles(articlesWithFavorites)
            
            Result.success(articlesWithFavorites)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
