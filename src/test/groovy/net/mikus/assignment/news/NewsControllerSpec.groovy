package net.mikus.assignment.news

import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

@SpringApplicationConfiguration(NewsServerApplication)
@WebIntegrationTest(randomPort = true)
class NewsControllerSpec extends Specification {

    @Value('${local.server.port}')
    private int port

    private RESTClient client

    @Autowired
    NewsRepository newsRepository

    def setup() {
        client = new RESTClient("http://localhost:$port/news/")
    }

    def "GET /news should return empty list when there is no news"() {
        when:
        def response = client.get(path: '')

        then:
        with(response) {
            status == 200
            data == []
        }
    }

    @DirtiesContext
    def "GET /news should return list of one news properly encoded for only one news in repository"() {
        when:
        newsRepository.add(new NewsEntry(title: title, content: content))
        def response = client.get(path: '')

        then:
        with(response) {
            success == true
            contentType == 'application/json'
            data.size() == 1
            data[0].title == title
            data[0].content == content
        }

        where:
             title           |        content
        'Simple ascii title' | 'Simple ascii content'
        'Zażółć gęślą jaźń'  | 'W Szczebrzeszynie chrząsz brzmi w trzcinie'
        '簡單的中國冠軍'        | 'المحتوى العربي البسيط'
    }

    @DirtiesContext
    def "GET /news should return list of news with title and content"() {
        when:
        input.each {
            newsRepository.add(it)
        }
        def response = client.get(path: '')

        then:
        with(response) {
            success == true
            contentType == 'application/json'
            data.size() == input.size()
            data == output
        }

        where:
        input = [
                new NewsEntry(
                        title: 'Real Madrid win the Champions League as Ronaldo decides penalty shootout',
                        content: 'Predictably, details on Cristiano Ronaldo\'s thigh injury were scarce before ' +
                                'kick-off, but there were few glimpses of his explosive self at the San Siro. ' +
                                'Big-name players have been risked with injuries in finals before, but Real\'s ' +
                                'first-half assault on Atletico territory was arguably aided by his personal bluntness.'
                ),
                new NewsEntry(
                        title: 'All scientific papers to be free by 2020 under EU proposals',
                        content: 'All publicly funded scientific papers published in Europe could be made free ' +
                                'to access by 2020, under a “life-changing” reform ordered by the European Union’s ' +
                                'science chief, Carlos Moedas.'
                ),
                new NewsEntry(
                        title: 'Lightning strikes in Europe: One killed and many injured',
                        content: 'Lightning strikes in several European countries have killed one man and caused ' +
                                'serious injuries as summer storms hit across the Continent. The man was killed in ' +
                                'southern Poland as he was hit by lightning while descending a mountain. In Paris, ' +
                                'a birthday party in Parc Monceau was struck, injuring 11 people, eight of them ' +
                                'children. Several are in a life-threatening condition. Three people were seriously ' +
                                'hurt at a youth football match in Germany.'
                )
        ]
        output = [
                [
                        title: 'Real Madrid win the Champions League as Ronaldo decides penalty shootout',
                        content: 'Predictably, details on Cristiano Ronaldo\'s thigh injury were scarce before ' +
                                'kick-off, but there were few glimpses of his explosive self at the San Siro. ' +
                                'Big-name players have been risked with injuries in finals before, but Real\'s ' +
                                'first-half assault on Atletico territory was arguably aided by his personal bluntness.'
                ],
                [
                          title: 'All scientific papers to be free by 2020 under EU proposals',
                          content: 'All publicly funded scientific papers published in Europe could be made free ' +
                                  'to access by 2020, under a “life-changing” reform ordered by the European Union’s ' +
                                  'science chief, Carlos Moedas.'
                ],
                [
                          title: 'Lightning strikes in Europe: One killed and many injured',
                          content: 'Lightning strikes in several European countries have killed one man and caused ' +
                                  'serious injuries as summer storms hit across the Continent. The man was killed in ' +
                                  'southern Poland as he was hit by lightning while descending a mountain. In Paris, ' +
                                  'a birthday party in Parc Monceau was struck, injuring 11 people, eight of them ' +
                                  'children. Several are in a life-threatening condition. Three people were seriously ' +
                                  'hurt at a youth football match in Germany.'
                ]
        ]
    }

}
