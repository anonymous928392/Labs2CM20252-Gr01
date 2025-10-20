package co.edu.udea.compumovil.gr01_20252_lab2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.edu.udea.compumovil.gr01_20252_lab2.data.local.AppDatabase
import co.edu.udea.compumovil.gr01_20252_lab2.data.model.Article
import co.edu.udea.compumovil.gr01_20252_lab2.data.remote.RetrofitInstance
import co.edu.udea.compumovil.gr01_20252_lab2.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NewsRepository

    private val _article = MutableStateFlow<Article?>(null)
    val article: StateFlow<Article?> = _article.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = NewsRepository(RetrofitInstance.api, database.articleDao())
    }

    fun loadArticle(articleId: String) {
        viewModelScope.launch {
            val art = repository.getArticleById(articleId)
            _article.value = art
        }
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            repository.toggleFavorite(article)
            _article.value = article.copy(isFavorite = !article.isFavorite)
        }
    }
}