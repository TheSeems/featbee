package me.theseems.featbee.dao.statistic

interface FeedbackDaoCustom {
    fun getAverageScore(): Double?

    fun getStandardDeviation(): Double
}
