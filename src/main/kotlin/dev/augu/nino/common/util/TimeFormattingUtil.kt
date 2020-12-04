package dev.augu.nino.common.util

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

val FORMAT_REGEX = Regex("""(?:(?:(?:(\d+)d)|(?:(\d+)h)|(?:(\d+)m)|(?:(\d+)s))\s*)+""")

fun parseDuration(text: String): Duration? {
    val matchResult = FORMAT_REGEX.find(text) ?: return null

    val dayGroup = matchResult.groups[1]
    val hourGroup = matchResult.groups[2]
    val minuteGroup = matchResult.groups[3]
    val secondGroup = matchResult.groups[4]

    var duration = Duration.ZERO

    if (dayGroup != null) {
        duration += Duration.of(dayGroup.value.toLongOrNull() ?: 0, ChronoUnit.DAYS)
    }

    if (hourGroup != null) {
        duration += Duration.of(hourGroup.value.toLongOrNull() ?: 0, ChronoUnit.HOURS)
    }

    if (minuteGroup != null) {
        duration += Duration.of(minuteGroup.value.toLongOrNull() ?: 0, ChronoUnit.MINUTES)
    }

    if (secondGroup != null) {
        duration += Duration.of(secondGroup.value.toLongOrNull() ?: 0, ChronoUnit.SECONDS)
    }

    return duration
}

fun formatDurationShort(duration: Duration): String {
    if (duration.isZero) {
        return "0s"
    }

    val days = duration.toDaysPart()
    val hours = duration.toHoursPart()
    val minutes = duration.toMinutesPart()
    val seconds = duration.toSecondsPart()

    val formattedStringBuilder = StringBuffer()

    if (days > 0) {
        formattedStringBuilder.append("${days}d")
    }

    if (hours > 0) {
        formattedStringBuilder.append("${hours}h")
    }

    if (minutes > 0) {
        formattedStringBuilder.append("${minutes}m")
    }

    if (seconds > 0) {
        formattedStringBuilder.append("${seconds}s")
    }

    return formattedStringBuilder.toString()
}

fun formatDurationLong(duration: Duration): String {
    if (duration.isZero) {
        return "0 seconds"
    }

    val days = duration.toDaysPart()
    val hours = duration.toHoursPart()
    val minutes = duration.toMinutesPart()
    val seconds = duration.toSecondsPart()

    val formattedStringBuilder = StringBuffer()

    if (days > 0) {
        formattedStringBuilder.append("$days day")
        if (days > 1) {
            formattedStringBuilder.append('s')
        }
    }

    if (hours > 0) {
        if (formattedStringBuilder.isNotEmpty()) {
            formattedStringBuilder.append(", ")
        }
        formattedStringBuilder.append("$hours hour")
        if (hours > 1) {
            formattedStringBuilder.append('s')
        }
    }

    if (minutes > 0) {
        if (formattedStringBuilder.isNotEmpty()) {
            formattedStringBuilder.append(", ")
        }
        formattedStringBuilder.append("$minutes minute")
        if (minutes > 1) {
            formattedStringBuilder.append('s')
        }
    }

    if (seconds > 0) {
        if (formattedStringBuilder.isNotEmpty()) {
            formattedStringBuilder.append(", ")
        }
        formattedStringBuilder.append("$seconds second")
        if (seconds > 1) {
            formattedStringBuilder.append('s')
        }
    }

    return formattedStringBuilder.toString()
}

fun formatDateLong(dateTime: OffsetDateTime): String {
    return dateTime.format(DateTimeFormatter.ofPattern(SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss 'UTC'").toPattern()))
}
