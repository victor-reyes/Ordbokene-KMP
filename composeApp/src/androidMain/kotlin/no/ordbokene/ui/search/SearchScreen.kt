package no.ordbokene.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {

  val query by viewModel.query.collectAsState()
  val articles by viewModel.articles.collectAsState()
  SearchScreen(query, articles, viewModel::setQuery)
}

@Composable
private fun SearchScreen(query: String, articles: List<String>, onQueryChanged: (String) -> Unit) {
  Column(Modifier.fillMaxWidth().padding(16.dp), Arrangement.spacedBy(8.dp), Alignment.CenterHorizontally) {
    OutlinedTextField(query, onQueryChanged)

    LazyColumn { items(articles) { Text(it) } }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppAndroidPreview() {
  var query by remember { mutableStateOf("") }
  val articles = listOf("Article 1", "Article 2").map { "$it by $query" }
  MaterialTheme { SearchScreen(query, articles) { query = it } }
}
