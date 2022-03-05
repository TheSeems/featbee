package me.theseems.thrut.service

import me.theseems.thrut.dao.FeedbackDao
import me.theseems.thrut.dto.FeedbackDto
import me.theseems.thrut.event.FeedbackChangedEvent
import me.theseems.thrut.event.FeedbackProduceEvent
import me.theseems.thrut.mapper.FeedbackMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
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

    fun saveFeedback(feedbackDto: FeedbackDto, userIp: String): FeedbackDto {
        val entityManager = entityManagerFactory.createEntityManager()
        try {
            val transaction = entityManager.transaction
            transaction.begin()

            var current = feedbackDao.findByIp(userIp)
            val previous = current

            if (current != null) {
                current.content = feedbackDto.content
                current.score = feedbackDto.score
            } else {
                current = feedbackMapper
                    .map(feedbackDto)
                    .apply { ip = userIp } // Adding user ip to the entity
            }

            try {
                entityManager.merge(current)
                if (previous == null) {
                    applicationEventPublisher.publishEvent(FeedbackProduceEvent(current))
                } else {
                    applicationEventPublisher.publishEvent(FeedbackChangedEvent(current, previous))
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
