package me.theseems.featbee.controller

import me.theseems.featbee.dto.FeedbackDto
import me.theseems.featbee.handler.captcha.CaptchaRequired
import me.theseems.featbee.service.FeedbackService
import me.theseems.featbee.util.HttpUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
class FeedbackController {
    @Autowired
    private lateinit var service: FeedbackService

    @CaptchaRequired
    @PostMapping("api")
    @ResponseBody
    fun sendFeedback(@RequestBody @Valid feedbackDto: FeedbackDto, request: HttpServletRequest) =
        service.saveFeedback(feedbackDto, HttpUtils.getRequestIp(request))

    @GetMapping("api")
    @ResponseBody
    fun getFeedback(request: HttpServletRequest) =
        service.getFeedback(HttpUtils.getRequestIp(request)) ?: throw EntityNotFoundException("no feedback provided")
}
