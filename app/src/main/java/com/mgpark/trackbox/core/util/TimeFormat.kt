package com.mgpark.trackbox.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val timeOnly  = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREA)
private val monthDay  = DateTimeFormatter.ofPattern("M월 d일 HH:mm", Locale.KOREA)
private val fullDate  = DateTimeFormatter.ofPattern("yyyy.M.d HH:mm", Locale.KOREA)

fun Instant.formatKrShort(
    now: Instant = Instant.now(),
    zone: ZoneId = ZoneId.systemDefault(),
): String {
    val event = atZone(zone)
    val current = now.atZone(zone)
    return when {
        event.toLocalDate() == current.toLocalDate() -> timeOnly.format(event)
        event.year == current.year -> monthDay.format(event)
        else -> fullDate.format(event)
    }
}

fun Instant.formatKrFull(zone: ZoneId = ZoneId.systemDefault()): String =
    fullDate.format(atZone(zone))
