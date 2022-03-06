package me.theseems.thrut.dao.statistic

interface FeedbackDaoCustom {
    fun getAverageScore(): Double?

    fun getStandardDeviation(): Double
}
