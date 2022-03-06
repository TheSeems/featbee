package me.theseems.featbee.service.statistic

import me.theseems.featbee.dao.FeedbackDao
import me.theseems.featbee.dto.statistic.StatisticsDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedbackStatisticsService {
    @Autowired
    private lateinit var feedbackDao: FeedbackDao

    @Transactional(readOnly = true)
    fun getStatistics() = StatisticsDto(
        averageScore = findAverageScore(),
        standardDeviation = findStandardDeviation(),
        count = feedbackDao.count()
    )

    private fun findAverageScore(): Double = feedbackDao.getAverageScore() ?: 0.0

    private fun findStandardDeviation(): Double = feedbackDao.getStandardDeviation()
}
