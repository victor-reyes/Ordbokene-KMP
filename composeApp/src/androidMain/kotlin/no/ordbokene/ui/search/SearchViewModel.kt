package no.ordbokene.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import no.ordbokene.model.ArticleRepositoryImpl
import no.ordbokene.model.DictionaryApiService
import no.ordbokene.model.json.lookup.ArticleResponse

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

  val repository = ArticleRepositoryImpl(DictionaryApiService())

  val query = savedStateHandle.getStateFlow("query", "")
  private val word = savedStateHandle.getStateFlow("word", "")
  private val dictionary = savedStateHandle.getStateFlow("dictionary", "bm")

  val suggestions =
    query
      .map(repository::fetchAutocomplete)
      .map { with(it.suggestions) { exact + similar + inflection + freeText }.map { it.word } }
      .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  val articles =
    word
      .combine(dictionary, ::Pair)
      .flatMapLatest { (dictionary, query) ->
        flow {
          emit(emptyList<ArticleResponse>())
          val ids = repository.search(query).articlesIds.bm
          emit(repository.fetchArticles(ids, dictionary))
        }
      }
      .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  fun setQuery(query: String) {
    savedStateHandle["query"] = query
  }

  fun setWord(word: String) {
    savedStateHandle["word"] = word
    setQuery(word)
  }
}
