package me.theseems.thrut.event

import me.theseems.thrut.entity.FeedbackEntity
import org.springframework.context.ApplicationEvent

class FeedbackChangedEvent(
    val feedbackEntity: FeedbackEntity,
    val previousFeedbackEntity: FeedbackEntity
) : ApplicationEvent(feedbackEntity)
