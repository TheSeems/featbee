package me.theseems.featbee.controller.statistic

import me.theseems.featbee.service.statistic.FeedbackStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StatisticsController {
    @Autowired
    private lateinit var statisticsService: FeedbackStatisticsService

    @GetMapping("api/statistics")
    @ResponseBody
    fun getStatistics() = statisticsService.getStatistics()
}
