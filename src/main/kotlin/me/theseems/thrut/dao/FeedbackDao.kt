package me.theseems.thrut.dao

import me.theseems.thrut.entity.FeedbackEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackDao : JpaRepository<FeedbackEntity, Long> {
    fun findByIp(ip: String): FeedbackEntity?
}
