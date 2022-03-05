package me.theseems.thrut.dto

import me.theseems.thrut.entity.CONTENT_MAX_LENGTH
import me.theseems.thrut.entity.SCORE_MAX_VALUE
import me.theseems.thrut.entity.SCORE_MIN_VALUE
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class FeedbackDto(
    @field:Size(max = CONTENT_MAX_LENGTH, message = "response must be at most 8000 characters long")
    val content: String?,

    @field:Min(value = SCORE_MIN_VALUE.toLong(), message = "score must be at least 0")
    @field:Max(value = SCORE_MAX_VALUE.toLong(), message = "score must be at most 10")
    val score: Int
)