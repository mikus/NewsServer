package net.mikus.assignment.news

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET


@RestController
@RequestMapping('/news')
class NewsController {

    @Autowired
    private NewsRepository newsRepository

    @RequestMapping(path = '', method = GET, produces = 'application/json; charset=UTF-8')
    List<NewsEntry> list() {
        newsRepository.findAll()
    }

}
