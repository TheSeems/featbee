package me.theseems.thrut.dao

import me.theseems.thrut.dao.statistic.FeedbackDaoCustom
import me.theseems.thrut.entity.FeedbackEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackDao : JpaRepository<FeedbackEntity, Long>, FeedbackDaoCustom {
    fun findByIp(ip: String): FeedbackEntity?
}
