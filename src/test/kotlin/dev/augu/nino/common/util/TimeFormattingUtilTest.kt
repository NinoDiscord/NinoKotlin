package dev.augu.nino.common.util

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import java.time.Duration

class TimeFormattingUtilTest : DescribeSpec({
    describe("Time Formatting Unit Tests") {
        it("should parse the durations correctly") {
            val threeSeconds = "3s"

            parseDuration(threeSeconds).shouldBe(Duration.ofSeconds(3))

            val fourtyHours = "40h"

            parseDuration(fourtyHours).shouldBe(Duration.ofHours(40))

            val thirteenHoursAndTwoMinutes = "13h 2m"

            parseDuration(thirteenHoursAndTwoMinutes).shouldBe(Duration.ofHours(13) + Duration.ofMinutes(2))

            val threeDaysFourHoursAndThirtyFiveSeconds = "3d 35s 4h"

            parseDuration(threeDaysFourHoursAndThirtyFiveSeconds).shouldBe(Duration.ofDays(3) + Duration.ofHours(4) + Duration.ofSeconds(35))

            val incorrectDuration = "I'm blue da ba dee da ba die"

            parseDuration(incorrectDuration).shouldBeNull()
        }

        it("should print out the durations correctly - long") {
            val threeSeconds = Duration.ofSeconds(3)

            formatDurationLong(threeSeconds).shouldBe("3 seconds")

            val fourtyHours = Duration.ofHours(40)

            formatDurationLong(fourtyHours).shouldBe("1 day, 16 hours")

            val thirteenHoursAndTwoMinutes = Duration.ofHours(13) + Duration.ofMinutes(2)

            formatDurationLong(thirteenHoursAndTwoMinutes).shouldBe("13 hours, 2 minutes")

            val threeDaysFourHoursAndThirtyFiveSeconds = Duration.ofDays(3) + Duration.ofHours(4) + Duration.ofSeconds(35)

            formatDurationLong(threeDaysFourHoursAndThirtyFiveSeconds).shouldBe("3 days, 4 hours, 35 seconds")

            formatDurationLong(Duration.ZERO).shouldNotBeEmpty()
        }
    }
})
