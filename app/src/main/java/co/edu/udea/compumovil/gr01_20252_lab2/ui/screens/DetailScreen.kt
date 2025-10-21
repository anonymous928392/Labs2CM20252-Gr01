package co.edu.udea.compumovil.gr01_20252_lab2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.udea.compumovil.gr01_20252_lab2.R
import co.edu.udea.compumovil.gr01_20252_lab2.data.model.Article
import co.edu.udea.compumovil.gr01_20252_lab2.ui.components.TooltipIconButton
import co.edu.udea.compumovil.gr01_20252_lab2.ui.viewmodel.DetailViewModel
import co.edu.udea.compumovil.gr01_20252_lab2.util.CategoryMapper
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    articleId: String,
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val article by viewModel.article.collectAsState()

    LaunchedEffect(articleId) {
        viewModel.loadArticle(articleId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.article_detail)) },
                navigationIcon = {
                    TooltipIconButton(
                        onClick = onNavigateBack,
                        icon = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tooltipText = stringResource(R.string.back_tooltip)
                    )
                },
                actions = {
                    article?.let { art ->
                        TooltipIconButton(
                            onClick = { viewModel.toggleFavorite(art) },
                            icon = if (art.isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.favorite),
                            tooltipText = if (art.isFavorite)
                                stringResource(R.string.remove_favorite)
                            else
                                stringResource(R.string.add_favorite),
                            tint = if (art.isFavorite)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    TooltipIconButton(
                        onClick = { /* Share */ },
                        icon = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share),
                        tooltipText = stringResource(R.string.share_article)
                    )
                }
            )
        }
    ) { paddingValues ->
        article?.let { art ->
            ArticleDetailContent(
                article = art,
                modifier = Modifier.padding(paddingValues)
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ArticleDetailContent(
    article: Article,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen principal
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Categoría (Traducida)
            AssistChip(
                onClick = {},
                label = {
                    Text(CategoryMapper.getCategoryTranslation(context, article.category))
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Título
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Autor y fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = article.author,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatDate(article.publishedAt),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Descripción
            Text(
                text = article.description,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Contenido completo
            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}