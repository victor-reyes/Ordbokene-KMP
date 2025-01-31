package no.ordbokene.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
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
import no.ordbokene.model.json.lookup.ArticleResponse

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {

  val query by viewModel.query.collectAsState()
  val suggestions by viewModel.suggestions.collectAsState()
  val articles by viewModel.articles.collectAsState()
  SearchScreen(articles, query, viewModel::setWord, suggestions, viewModel::setWord)
}

@Composable
private fun SearchScreen(
  articles: List<ArticleResponse>,
  query: String,
  onQueryChanged: (String) -> Unit,
  suggestions: List<String>,
  onSearch: (String) -> Unit,
) {
  Column(Modifier.fillMaxWidth().padding(16.dp), Arrangement.spacedBy(8.dp), Alignment.CenterHorizontally) {
    AutocompleteSearchField(query, onQueryChanged, suggestions, onSearch)
    LazyColumn { items(articles) { Text(it.lemmas.first().lemma) } }
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

  OutlinedTextField(
    query,
    onQueryChanged,
    Modifier.focusRequester(focusRequester).onFocusChanged { showSuggestions = it.isFocused },
  )
  AnimatedVisibility(showSuggestions) {
    ElevatedCard {
      LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
        itemsIndexed(suggestions) { index, suggestion ->
          Row(
            Modifier.clickable {
              onSearch(suggestion)
              focusManager.clearFocus()
            }
          ) {
            Text(
              suggestion,
              Modifier.padding(vertical = 12.dp),
              fontSize = MaterialTheme.typography.button.fontSize,
              fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.weight(1f))
          }
          if (index < suggestions.lastIndex) HorizontalDivider()
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
  MaterialTheme { SearchScreen(emptyList(), query, { query = it }, suggestions, {}) }
}
