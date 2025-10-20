package co.edu.udea.compumovil.gr01_20252_lab2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.edu.udea.compumovil.gr01_20252_lab2.data.local.AppDatabase
import co.edu.udea.compumovil.gr01_20252_lab2.data.model.Article
import co.edu.udea.compumovil.gr01_20252_lab2.data.remote.RetrofitInstance
import co.edu.udea.compumovil.gr01_20252_lab2.data.repository.NewsRepository
import co.edu.udea.compumovil.gr01_20252_lab2.worker.SyncArticlesWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NewsUiState(
    val articles: List<Article> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NewsRepository

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = NewsRepository(RetrofitInstance.api, database.articleDao())

        SyncArticlesWorker.setupPeriodicSync(application)

        loadArticles()
        loadCategories()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getArticles()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { articles ->
                            _uiState.update {
                                it.copy(
                                    articles = articles,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        },
                        onFailure = { e ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message
                                )
                            }
                        }
                    )
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .collect { categories ->
                    _uiState.update {
                        it.copy(categories = listOf("All") + categories)
                    }
                }
        }
    }

    fun selectCategory(category: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedCategory = category, isLoading = true) }

            if (category == "All") {
                loadArticles()
            } else {
                repository.getArticlesByCategory(category)
                    .catch { e ->
                        _uiState.update {
                            it.copy(isLoading = false, error = e.message)
                        }
                    }
                    .collect { articles ->
                        _uiState.update {
                            it.copy(articles = articles, isLoading = false)
                        }
                    }
            }
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val result = repository.refreshArticles()

            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            repository.toggleFavorite(article)
        }
    }

    fun syncNow() {
        SyncArticlesWorker.syncNow(getApplication())
    }
}