package me.theseems.featbee.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {
    @Value("#{'\${featbee.domains}'.split(',')}")
    private var domains: Array<String>? = null

    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins(*(domains ?: arrayOf("*")))
                    .allowedHeaders("*")
                    .allowedMethods("GET", "POST")
            }
        }
    }
}
