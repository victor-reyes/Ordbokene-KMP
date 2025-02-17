package no.ordbokene.model

import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual fun getCacheFolderPath(): String {
  val fileManager = NSFileManager.defaultManager
  val urls = fileManager.URLsForDirectory(NSCachesDirectory, NSUserDomainMask)
  val cacheDirectory = (urls.firstOrNull() as? NSURL) ?: error("No cache directory found")
  return cacheDirectory.path ?: error("Could not determine cache directory path")
}
