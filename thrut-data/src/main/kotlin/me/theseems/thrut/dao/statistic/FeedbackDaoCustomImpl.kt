package me.theseems.thrut.dao.statistic

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class FeedbackDaoCustomImpl : FeedbackDaoCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getAverageScore(): Double? {
        return entityManager
            .createQuery("select avg(feedback.score + 0.0) from FeedbackEntity feedback")
            .singleResult as? Double?
    }

    override fun getStandardDeviation(): Double {
        val squareDifferences = entityManager
            .createQuery(
                """
                        select
                            sqrt(sum((feedback.score + 0.0 - :avgScore) * (feedback.score + 0.0 - :avgScore)))
                        from FeedbackEntity feedback
                    """
            )
            .setParameter("avgScore", getAverageScore())
            .singleResult as? Double?
            ?: return 0.0

        val squareCount = findSquareRootOfCount() ?: return 0.0
        return squareDifferences / squareCount
    }

    private fun findSquareRootOfCount(): Double? {
        return entityManager
            .createQuery("select sqrt(count(feedback) + 0.0) from FeedbackEntity feedback")
            .singleResult as? Double?
    }
}
