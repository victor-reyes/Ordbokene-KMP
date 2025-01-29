package no.ordbokene.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

  val query = savedStateHandle.getStateFlow("query", "")

  val articles =
    query
      .map { query -> listOf("Article 1", "Article 2").map { "$it by $query" } }
      .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  fun setQuery(query: String) {
    savedStateHandle["query"] = query
  }
}
