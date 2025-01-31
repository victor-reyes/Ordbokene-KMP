package no.ordbokene.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {

  val query by viewModel.query.collectAsState()
  val suggestions by viewModel.suggestions.collectAsState()
  val articleUiState by viewModel.articleUiState.collectAsState()
  SearchScreen(articleUiState, query, viewModel::setQuery, suggestions, viewModel::setWord)
}

@Composable
private fun SearchScreen(
  articleUiState: ArticleUiState,
  query: String,
  onQueryChanged: (String) -> Unit,
  suggestions: List<String>,
  onSearch: (String) -> Unit,
) {
  Column(Modifier.fillMaxWidth().padding(16.dp), Arrangement.spacedBy(8.dp), Alignment.CenterHorizontally) {
    AutocompleteSearchField(query, onQueryChanged, suggestions, onSearch)
    Articles(articleUiState)
  }
}

@Composable
fun Articles(articleUiState: ArticleUiState) {
  when (articleUiState) {
    is ArticleUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

    is ArticleUiState.Error -> Text("Error: ${articleUiState.message}")
    is ArticleUiState.Success -> LazyColumn { items(articleUiState.articles) { Text(it.lemmas.first().lemma) } }
  }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun AutocompleteSearchField(
  query: String,
  onQueryChanged: (String) -> Unit,
  suggestions: List<String>,
  onSearch: (String) -> Unit,
) {
  val focusManager = LocalFocusManager.current
  val focusRequester = remember { FocusRequester() }
  var showSuggestions by remember { mutableStateOf(false) }

  val modifier = if (showSuggestions) Modifier.fillMaxHeight() else Modifier
  Column(modifier) {
    OutlinedTextField(
      query,
      onQueryChanged,
      Modifier.focusRequester(focusRequester).onFocusChanged { showSuggestions = it.isFocused },
    )
    AnimatedVisibility(showSuggestions) {
      ElevatedCard {
        LazyColumn {
          itemsIndexed(suggestions) { index, suggestion ->
            Row(
              Modifier.fillParentMaxWidth().clickable {
                onSearch(suggestion)
                focusManager.clearFocus()
              }
            ) {
              Text(
                suggestion,
                Modifier.padding(12.dp),
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = FontWeight.Bold,
              )
            }
            if (index < suggestions.lastIndex) HorizontalDivider(Modifier.width(IntrinsicSize.Max))
          }
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppAndroidPreview() {
  var query by remember { mutableStateOf("") }
  val suggestions = listOf("Article 1", "Article 2").map { "$it by $query" }
  MaterialTheme { SearchScreen(ArticleUiState.Loading, query, { query = it }, suggestions, {}) }
}
