package me.theseems.featbee.event

import me.theseems.featbee.entity.FeedbackEntity
import org.springframework.context.ApplicationEvent

class FeedbackProducedEvent(val feedbackEntity: FeedbackEntity) : ApplicationEvent(feedbackEntity)
