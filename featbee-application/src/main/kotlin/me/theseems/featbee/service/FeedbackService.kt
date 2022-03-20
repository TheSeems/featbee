package me.theseems.featbee.service

import me.theseems.featbee.dao.FeedbackDao
import me.theseems.featbee.dto.FeedbackDto
import me.theseems.featbee.entity.FeedbackEntityVisibility
import me.theseems.featbee.event.FeedbackChangedEvent
import me.theseems.featbee.event.FeedbackProducedEvent
import me.theseems.featbee.mapper.FeedbackMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManagerFactory

@Service
class FeedbackService {
    @Autowired
    private lateinit var feedbackDao: FeedbackDao

    @Autowired
    private lateinit var feedbackMapper: FeedbackMapper

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Transactional(readOnly = true)
    fun getFeedback(ip: String): FeedbackDto? = feedbackDao.findByIp(ip)?.let(feedbackMapper::map)

    @Transactional(readOnly = true)
    fun getPublicFeedbackEntities(pageable: Pageable): List<FeedbackDto> = feedbackDao
        .findByVisibility(pageable, FeedbackEntityVisibility.PUBLIC)
        .map(feedbackMapper::map)
        .content

    fun saveFeedback(feedbackDto: FeedbackDto, userIp: String): FeedbackDto {
        val entityManager = entityManagerFactory.createEntityManager()
        try {
            var current = feedbackDao.findByIp(userIp)
            if (current != null && feedbackDto == feedbackMapper.map(current)) {
                return feedbackDto
            }

            val previous = feedbackMapper.mapIdentity(current)
            val transaction = entityManager.transaction
            transaction.begin()

            if (current != null) {
                feedbackMapper.update(current, feedbackDto)
            } else {
                current = feedbackMapper
                    .map(feedbackDto)
                    .apply { ip = userIp } // Adding user ip to the entity
            }

            try {
                entityManager.merge(current)
                if (previous == null) {
                    applicationEventPublisher.publishEvent(FeedbackProducedEvent(current))
                } else {
                    applicationEventPublisher.publishEvent(
                        FeedbackChangedEvent(
                            feedbackEntity = current,
                            previousFeedbackEntity = previous
                        )
                    )
                }

                transaction.commit()
            } catch (e: Exception) {
                try {
                    transaction.rollback()
                } catch (exceptionInRollback: Exception) {
                    e.addSuppressed(exceptionInRollback)
                }
                throw Exception("Failed to handle feedback", e)
            }

            return feedbackMapper.map(current)
        } finally {
            entityManager.close()
        }
    }
}
