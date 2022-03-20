package me.theseems.featbee.controller.feed

import me.theseems.featbee.service.FeedbackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FeedController {
    @Autowired
    private lateinit var feedbackService: FeedbackService

    @GetMapping("api/feed")
    fun getFeedbackEntries(
        @SortDefault(sort = ["id"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = feedbackService.getPublicFeedbackEntities(pageable)
}