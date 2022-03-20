package me.theseems.featbee.mapper

import me.theseems.featbee.dto.FeedbackDto
import me.theseems.featbee.entity.FeedbackEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface FeedbackMapper {
    @Mapping(target = "ip", constant = "unknown")
    @Mapping(target = "visibility", source = "visibility", defaultValue = "PRIVATE")
    fun map(feedbackDto: FeedbackDto): FeedbackEntity


    @Mapping(target = "visibility", source = "visibility", defaultValue = "PRIVATE")
    fun update(@MappingTarget entity: FeedbackEntity, feedbackDto: FeedbackDto)

    fun map(feedbackEntity: FeedbackEntity): FeedbackDto

    fun mapIdentity(feedbackEntity: FeedbackEntity?): FeedbackEntity?
}
