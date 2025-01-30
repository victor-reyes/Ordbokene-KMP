package no.ordbokene.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import no.ordbokene.model.ArticleRepositoryImpl
import no.ordbokene.model.DictionaryApiService

class SearchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

  val repository = ArticleRepositoryImpl(DictionaryApiService())

  val query = savedStateHandle.getStateFlow("query", "")

  val articles =
    query
      .map(repository::fetchAutocomplete)
      .map { with(it.suggestions) { exact + similar + inflection + freeText }.map { it.word } }
      .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  fun setQuery(query: String) {
    savedStateHandle["query"] = query
  }
}
