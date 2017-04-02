package ch.brello.narrative.utils

object StringUtils {

  implicit class StringImprovements(s: String) {
    def isUuid: Boolean = UuidUtils.isUuid(s)
    def isEvent: Boolean = s match {
      case "click" => true
      case "impression" => true
      case _ => false
    }
  }

}
