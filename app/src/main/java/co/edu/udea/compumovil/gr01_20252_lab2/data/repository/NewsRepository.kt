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
            val articles = api.getArticles()
            dao.insertArticles(articles)
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
            val articles = api.getArticles()
            dao.deleteAllArticles()
            dao.insertArticles(articles)
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
