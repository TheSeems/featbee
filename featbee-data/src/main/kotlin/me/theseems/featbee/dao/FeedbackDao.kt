package me.theseems.featbee.dao

import me.theseems.featbee.dao.statistic.FeedbackDaoCustom
import me.theseems.featbee.entity.FeedbackEntity
import me.theseems.featbee.entity.FeedbackEntityVisibility
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackDao : JpaRepository<FeedbackEntity, Long>, FeedbackDaoCustom {
    fun findByIp(ip: String): FeedbackEntity?
    fun findByVisibility(pageable: Pageable, visibility: FeedbackEntityVisibility): Page<FeedbackEntity>
}
