package service

import play.api.libs.json.{Json, OFormat}

case class TimedItem (
    id: String,
    project: String,
    startTime: Long,
    endTime: Long,
    note: String
)

object TimedItem {
    implicit val format: OFormat[TimedItem] = Json.format[TimedItem]
}