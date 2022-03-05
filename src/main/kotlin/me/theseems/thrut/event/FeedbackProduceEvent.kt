package me.theseems.thrut.event

import me.theseems.thrut.entity.FeedbackEntity
import org.springframework.context.ApplicationEvent

class FeedbackProduceEvent(val feedbackEntity: FeedbackEntity) : ApplicationEvent(feedbackEntity)
