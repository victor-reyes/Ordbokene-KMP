package no.ordbokene.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import no.ordbokene.model.ArticleRepository
import no.ordbokene.model.json.lookup.ArticleResponse

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(private val savedStateHandle: SavedStateHandle, private val repository: ArticleRepository) :
  ViewModel() {

  val query = savedStateHandle.getStateFlow("query", "hel")
  private val word = savedStateHandle.getStateFlow("word", "hel")
  private val dictionary = savedStateHandle.getStateFlow("dictionary", "bm")

  val suggestions =
    query
      .map(repository::fetchAutocomplete)
      .map { with(it.suggestions) { exact + similar + inflection + freeText }.map { it.word }.distinct() }
      .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  val articleUiState =
    word
      .combine(dictionary, ::Pair)
      .flatMapLatest { (word, dictionary) ->
        flow {
          emit(ArticleUiState.Loading)
          val ids = repository.fetchIds(word, dictionary)
          if (ids.isEmpty()) {
            emit(ArticleUiState.Success(emptyList()))
            return@flow
          }
          repository
            .fetchArticleFlow(ids, dictionary)
            .scan(ArticleUiState.Success(emptyList())) { acc, curr -> ArticleUiState.Success(acc.articles + curr) }
            .drop(1)
            .catch { emit(ArticleUiState.Error(it.message ?: "Unknown error")) }
            .collect { emit(it) }
        }
      }
      .stateIn(viewModelScope, SharingStarted.Eagerly, ArticleUiState.Loading)

  fun setQuery(query: String) {
    savedStateHandle["query"] = query
  }

  fun setWord(word: String) {
    savedStateHandle["word"] = word
    setQuery(word)
  }
}

sealed class ArticleUiState {
  object Loading : ArticleUiState()

  data class Error(val message: String) : ArticleUiState()

  data class Success(val articles: List<ArticleResponse>) : ArticleUiState()
}
