package no.ordbokene.model

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import no.ordbokene.model.json.autocomplete.AutocompleteResponse
import no.ordbokene.model.json.lookup.ArticleResponse
import no.ordbokene.model.json.search.SearchResponse

interface ArticleRepository {
  @NativeCoroutines suspend fun fetchAutocomplete(query: String): AutocompleteResponse

  @NativeCoroutines suspend fun search(query: String): List<SearchResponse>

  @NativeCoroutines suspend fun fetchArticle(id: Int): ArticleResponse
}

class ArticleRepositoryImpl(val service: DictionaryApiService) : ArticleRepository {
  override suspend fun fetchAutocomplete(query: String) =
    withContext(Dispatchers.IO) { service.fetchAutocomplete(query) }

  override suspend fun search(query: String): List<SearchResponse> {
    TODO("Not yet implemented")
  }

  override suspend fun fetchArticle(id: Int): ArticleResponse {
    TODO("Not yet implemented")
  }
}
