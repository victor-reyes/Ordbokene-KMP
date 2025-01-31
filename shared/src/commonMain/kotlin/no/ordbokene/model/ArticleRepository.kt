package no.ordbokene.model

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import no.ordbokene.model.json.autocomplete.AutocompleteResponse
import no.ordbokene.model.json.lookup.ArticleResponse
import no.ordbokene.model.json.search.SearchResponse

interface ArticleRepository {
  @NativeCoroutines suspend fun fetchAutocomplete(query: String): AutocompleteResponse

  @NativeCoroutines suspend fun search(query: String): SearchResponse

  @NativeCoroutines suspend fun fetchArticle(id: Int, dictionary: String): ArticleResponse

  @NativeCoroutines suspend fun fetchArticles(ids: Set<Int>, dictionary: String): List<ArticleResponse>
}

class ArticleRepositoryImpl(val service: DictionaryApiService) : ArticleRepository {
  override suspend fun fetchAutocomplete(query: String) =
    withContext(Dispatchers.IO) { service.fetchAutocomplete(query) }

  override suspend fun search(query: String) = with(Dispatchers.IO) { service.search(query) }

  override suspend fun fetchArticle(id: Int, dictionary: String) =
    withContext(Dispatchers.IO) { service.fetchArticle(id, dictionary) }

  override suspend fun fetchArticles(ids: Set<Int>, dictionary: String) =
    withContext(Dispatchers.IO) { ids.map { id -> async { service.fetchArticle(id, dictionary) } }.awaitAll() }
}
