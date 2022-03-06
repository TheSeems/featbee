package me.theseems.featbee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeatbeeApplication

fun main(args: Array<String>) {
    runApplication<FeatbeeApplication>(*args)
}
