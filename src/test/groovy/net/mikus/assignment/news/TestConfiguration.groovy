package net.mikus.assignment.news

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TestConfiguration {

    @Bean
    NewsRepository newsRepository() {
        new InMemoryNewsRepository()
    }
}
