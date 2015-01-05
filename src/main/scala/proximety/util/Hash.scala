package proximety.util

object Hash {
  def sha1(content: String) = {
    val md = java.security.MessageDigest.getInstance("SHA-1")
    md.digest(content.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }
}
