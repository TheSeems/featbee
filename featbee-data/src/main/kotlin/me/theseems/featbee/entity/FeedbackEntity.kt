package me.theseems.featbee.entity

import javax.persistence.*

@Entity
class FeedbackEntity(
    @field:Basic(fetch = FetchType.LAZY)
    @field:Column(length = CONTENT_MAX_LENGTH)
    var content: String?,

    @field:Column(nullable = false)
    var score: Int,

    @field:Column(nullable = false, unique = true)
    var ip: String,

    @field:Column(
        nullable = false,
        columnDefinition = "varchar(${FeedbackEntityVisibility.MAX_LENGTH}) default 'PRIVATE'"
    )
    @field:Enumerated(value = EnumType.STRING)
    var visibility: FeedbackEntityVisibility = FeedbackEntityVisibility.PRIVATE
) : BaseEntity<Long>() {
    companion object {
        const val CONTENT_MAX_LENGTH = 8096
        const val SCORE_MIN_VALUE = 1
        const val SCORE_MAX_VALUE = 10
    }
}
