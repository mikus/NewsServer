package net.mikus.assignment.news


class InMemoryNewsRepository implements NewsRepository {

    private newsList = []

    @Override
    void add(NewsEntry newsEntry) {
        newsList << newsEntry
    }

    @Override
    List<NewsEntry> findAll() {
        newsList
    }
}
