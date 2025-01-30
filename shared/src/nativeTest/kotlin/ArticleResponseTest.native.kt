import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun readResources(resourceName: String): String {

  val pathParts = resourceName.trimStart('.', '/').split(".")
  val path = NSBundle.mainBundle.pathForResource("resources/${pathParts[0]}", pathParts[1])
  val data = NSString.stringWithContentsOfFile(path!!) as String

  return data
}
