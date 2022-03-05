package me.theseems.thrut.mapper

import me.theseems.thrut.dto.FeedbackDto
import me.theseems.thrut.entity.FeedbackEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface FeedbackMapper {
    @Mapping(target = "ip", constant = "unknown")
    fun map(feedbackDto: FeedbackDto): FeedbackEntity

    fun map(feedbackEntity: FeedbackEntity): FeedbackDto
}
