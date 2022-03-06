package me.theseems.featbee.dao

import me.theseems.featbee.dao.statistic.FeedbackDaoCustom
import me.theseems.featbee.entity.FeedbackEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackDao : JpaRepository<FeedbackEntity, Long>, FeedbackDaoCustom {
    fun findByIp(ip: String): FeedbackEntity?
}
