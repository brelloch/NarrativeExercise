package ch.brello.narrative.utils

object UuidUtils {
  def isUuid(value: String): Boolean = {
    val pattern = """[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}""".r

    val result: Option[String] = pattern findFirstIn value

    result match {
      case Some(x) => true
      case None => false
    }

  }
}
