package me.theseems.featbee.event

import me.theseems.featbee.entity.FeedbackEntity
import org.springframework.context.ApplicationEvent

class FeedbackChangedEvent(
    val feedbackEntity: FeedbackEntity,
    val previousFeedbackEntity: FeedbackEntity
) : ApplicationEvent(feedbackEntity)
