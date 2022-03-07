package me.theseems.featbee.mapper

import me.theseems.featbee.dto.FeedbackDto
import me.theseems.featbee.entity.FeedbackEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface FeedbackMapper {
    @Mapping(target = "ip", constant = "unknown")
    fun map(feedbackDto: FeedbackDto): FeedbackEntity

    fun map(feedbackEntity: FeedbackEntity): FeedbackDto

    fun mapIdentity(feedbackEntity: FeedbackEntity?): FeedbackEntity?
}
