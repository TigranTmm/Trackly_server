package com.example.domain.service

import com.example.domain.models.Session
import com.example.domain.repository.SessionRepository
import com.example.domain.repository.SphereRepository
import com.example.dto.analytics.DayPoint
import com.example.dto.analytics.DayStats
import com.example.dto.analytics.SessionPreview
import com.example.dto.analytics.SphereActivityItem
import com.example.dto.analytics.SphereChartLine
import com.example.dto.analytics.WeeklyAnalyticsResponse
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

class AnalyticsService(
    private val sessionRepository: SessionRepository,
    private val sphereRepository: SphereRepository
) {

    private fun getCurrentWeekRange(): Pair <Instant, Instant> {
        val start = LocalDate.now()
            .with(DayOfWeek.MONDAY)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()

        val end = LocalDate.now()
            .with(DayOfWeek.SUNDAY)
            .atTime(LocalTime.MAX)
            .toInstant(ZoneOffset.UTC)

        return start to end
    }

    suspend fun weeklyAnalytics(userId: Long): WeeklyAnalyticsResponse {
        val week = getCurrentWeekRange()

        /** Getting the list of SphereActivityItem **/
        val sessions = sessionRepository.getUserSessionsBetween(userId, week.first, week.second)
        val spheres = sphereRepository.getUserSpheres(userId)

        val sphereActivity = spheres.map { sphere ->
            val totalSeconds = sessions
                .filter { it.sphereId == sphere.id }
                .sumOf { it.factualDurationSeconds ?: 0 }

            SphereActivityItem(
                sphereId = sphere.id,
                sphereTitle = sphere.title,
                sphereColor = sphere.colorKey,
                sphereIcon = sphere.iconKey,
                totalSeconds = totalSeconds
            )
        }

        /** Getting the best and the worst days **/
        val dayStats = sessions.groupBy { it.endedAt!!.atZone(ZoneOffset.UTC).dayOfWeek }

        val dayTotals = dayStats.map { (day, session) ->
            DayStats(
                dayOfWeek = day.name,
                totalSeconds = session.sumOf { it.factualDurationSeconds ?: 0 }
            )
        }

        val bestDay = dayTotals.maxByOrNull { it.totalSeconds }
        val worstDay = dayTotals.minByOrNull { it.totalSeconds }

        /** Getting average day **/
        val totalWeekSeconds = sessions.sumOf { it.factualDurationSeconds ?: 0 }

        val averageDaySeconds = totalWeekSeconds / 7

        /** Getting the longest session **/
        val longest = sessions.maxByOrNull { it.factualDurationSeconds ?: 0 }

        val longestSession = longest?.let { session ->
            val sphere = spheres.firstOrNull { it.id == session.sphereId } ?: return@let null

            SessionPreview(
                sessionId = session.id,
                sphereId = sphere.id,
                sphereTitle = sphere.title,
                sphereIcon = sphere.iconKey,
                title = session.title,
                factualDurationSeconds = session.factualDurationSeconds ?: 0,
                endedAt = session.endedAt!!.epochSecond.toString(),
                comment = session.comment
            )
        }

        /** Getting the streak **/
        val activeDays = sessions.map { it.endedAt!!.atZone(ZoneOffset.UTC).toLocalDate() }.toSet()

        var streak = 0
        var currentDate = LocalDate.now()

        while (activeDays.contains(currentDate)) {
            streak++
            currentDate = currentDate.minusDays(1)
        }

        /** Getting the pie chart data **/
        val weekChart = spheres.map { sphere ->
            val points = DayOfWeek.entries.map { day ->
                val totalSeconds = sessions
                    .filter {
                        it.sphereId == sphere.id
                                && it.endedAt!!.atZone(ZoneOffset.UTC).dayOfWeek == day
                    }
                    .sumOf { it.factualDurationSeconds ?: 0 }

                DayPoint(
                    dayOfWeek = day.name,
                    totalSeconds = totalSeconds
                )
            }

            SphereChartLine(
                sphereId = sphere.id,
                sphereTitle = sphere.title,
                sphereColor = sphere.colorKey,
                points = points
            )
        }

        return WeeklyAnalyticsResponse(
            sphereActivity = sphereActivity,
            bestDay = bestDay,
            worstDay = worstDay,
            averageDaySeconds = averageDaySeconds,
            longestSession = longestSession,
            streakDays = streak,
            weekChart = weekChart
        )
    }
}
