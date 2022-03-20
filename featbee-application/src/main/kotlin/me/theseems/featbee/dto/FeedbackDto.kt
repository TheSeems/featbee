package me.theseems.featbee.dto

import me.theseems.featbee.entity.FeedbackEntity.Companion.CONTENT_MAX_LENGTH
import me.theseems.featbee.entity.FeedbackEntity.Companion.SCORE_MAX_VALUE
import me.theseems.featbee.entity.FeedbackEntity.Companion.SCORE_MIN_VALUE
import me.theseems.featbee.entity.FeedbackEntityVisibility
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class FeedbackDto(
    @field:Size(max = CONTENT_MAX_LENGTH, message = "response must be at most 8000 characters long")
    val content: String?,

    @field:Min(value = SCORE_MIN_VALUE.toLong(), message = "score is below $SCORE_MIN_VALUE")
    @field:Max(value = SCORE_MAX_VALUE.toLong(), message = "score is above $SCORE_MAX_VALUE")
    val score: Int,

    val visibility: FeedbackEntityVisibility = FeedbackEntityVisibility.PRIVATE
)
