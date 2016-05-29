package net.mikus.assignment.news


interface NewsRepository {

    void add(NewsEntry newsEntry)
    List<NewsEntry> findAll()

}
