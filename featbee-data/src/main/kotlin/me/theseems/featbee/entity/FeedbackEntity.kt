package me.theseems.featbee.entity

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType

@Entity
class FeedbackEntity(
    @Basic(fetch = FetchType.LAZY)
    @Column(length = CONTENT_MAX_LENGTH)
    var content: String?,

    @Column(nullable = false)
    var score: Int,

    @Column(nullable = false, unique = true)
    var ip: String
) : BaseEntity<Long>() {
    companion object {
        const val CONTENT_MAX_LENGTH = 8096
        const val SCORE_MIN_VALUE = 0
        const val SCORE_MAX_VALUE = 10
    }
}
