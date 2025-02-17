package no.ordbokene.model

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.Headers
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import io.ktor.util.toMap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import no.ordbokene.model.CachedResponseDataWrapper.Companion.toCachedResponseData
import no.ordbokene.model.CachedResponseDataWrapper.Companion.toCachedResponseDataWrapper

class SharedCacheStorage : CacheStorage {

  companion object {
    private const val TAG = "SharedCacheStorage"
    private val cacheDir = Path(getCacheFolderPath())

    private fun getCacheFile(url: Url) = Path(cacheDir, "${url.toString().hashCode()}.cache")

    private val json = Json.Default
    private val fileLocks = mutableMapOf<Path, Mutex>()
    private val fileLocksMutex = Mutex()

    private suspend fun obtainFileLock(path: Path): Mutex =
      fileLocksMutex.withLock { fileLocks.getOrPut(path) { Mutex() } }
  }

  init {
    if (!SystemFileSystem.exists(cacheDir)) SystemFileSystem.createDirectories(cacheDir)
  }

  override suspend fun store(url: Url, data: CachedResponseData) {
    val file = getCacheFile(url)
    val jsonRaw = data.toCachedResponseDataWrapper()

    Napier.d("json is for $url is stored now", tag = TAG)
    val lock = obtainFileLock(file)
    lock.withLock {
      with(SystemFileSystem) {
        delete(file, mustExist = false)
        sink(file).buffered().use { it.writeString(json.encodeToString(jsonRaw)) }
      }
    }
  }

  override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
    val file = getCacheFile(url)

    Napier.d("`find` url is $url", tag = TAG)
    if (!SystemFileSystem.exists(file)) return null
    val jsonRaw = SystemFileSystem.source(file).buffered().use { it.readString() }
    val cachedResponseData = json.decodeFromString<CachedResponseDataWrapper>(jsonRaw)
    return cachedResponseData.toCachedResponseData(url)
  }

  override suspend fun findAll(url: Url): Set<CachedResponseData> {
    val prefix = "${url.toString().hashCode()}"

    val matchingFiles = SystemFileSystem.list(cacheDir).filter { it.name.startsWith(prefix) }

    if (matchingFiles.isNotEmpty()) Napier.d("`findAll` there are ${matchingFiles.size} matches for $url", tag = TAG)
    return matchingFiles
      .mapNotNull { file ->
        val jsonRaw = SystemFileSystem.source(file).buffered().use { it.readString() }
        try {
          val cachedResponseData = json.decodeFromString<CachedResponseDataWrapper>(jsonRaw)
          cachedResponseData.toCachedResponseData(url)
        } catch (e: Exception) {
          Napier.d("failed to parse json $jsonRaw for $url with exception $e", tag = TAG)
          null
        }
      }
      .toSet()
  }
}

@Serializable
data class CachedResponseDataWrapper(
  val statusCode: HttpStatusCode,
  val requestTime: GMTDate,
  val responseTime: GMTDate,
  val version: HttpProtocolVersion,
  val expires: GMTDate,
  val headers: Map<String, List<String>>,
  val varyKeys: Map<String, String>,
  val body: ByteArray,
) {
  @Serializable data class HttpProtocolVersion(val name: String, val major: Int, val minor: Int)

  @Serializable data class HttpStatusCode(val value: Int, val description: String)

  companion object {
    fun CachedResponseDataWrapper.toCachedResponseData(url: Url) =
      CachedResponseData(
        url = url,
        statusCode = io.ktor.http.HttpStatusCode(statusCode.value, statusCode.description),
        requestTime = requestTime,
        responseTime = responseTime,
        version = io.ktor.http.HttpProtocolVersion(version.name, version.major, version.minor),
        expires = expires,
        headers = Headers.build { headers.forEach { (key, value) -> appendAll(key, value) } },
        varyKeys = varyKeys,
        body = body,
      )

    fun CachedResponseData.toCachedResponseDataWrapper() =
      CachedResponseDataWrapper(
        statusCode = HttpStatusCode(statusCode.value, statusCode.description),
        requestTime = requestTime,
        responseTime = responseTime,
        version = HttpProtocolVersion(version.name, version.major, version.minor),
        expires = expires,
        headers = headers.toMap(),
        varyKeys = varyKeys,
        body = body,
      )
  }
}

expect fun getCacheFolderPath(): String
