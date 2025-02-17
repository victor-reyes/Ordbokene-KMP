package no.ordbokene.model

import android.content.Context
import org.koin.core.context.GlobalContext
import java.io.File

actual fun getCacheFolderPath(): String {
  val context: Context = GlobalContext.get().get()
  val cacheDir: File = context.cacheDir
  return cacheDir.absolutePath
}
