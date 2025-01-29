import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun readResources(resourceName: String): String {
  // split based on "." and "/". We want to strip the leading ./ and
  // split the extension
  val pathParts = resourceName.split("[.|/]".toRegex())
  // pathParts looks like
  // [, , test_case_input_one, bin]
  val path = NSBundle.mainBundle.pathForResource("resources/${pathParts[2]}", pathParts[3])
  val data = NSString.stringWithContentsOfFile(path!!) as String

  return data
}
