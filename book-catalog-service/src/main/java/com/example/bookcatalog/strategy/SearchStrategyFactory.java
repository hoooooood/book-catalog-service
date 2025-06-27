package com.example.bookcatalog.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchStrategyFactory {

    private final TitleSearchStrategy titleSearchStrategy;
    private final AuthorSearchStrategy authorSearchStrategy;
    private final IsbnSearchStrategy isbnSearchStrategy;

    @Autowired
    public SearchStrategyFactory(TitleSearchStrategy titleSearchStrategy,
                               AuthorSearchStrategy authorSearchStrategy,
                               IsbnSearchStrategy isbnSearchStrategy) {
        this.titleSearchStrategy = titleSearchStrategy;
        this.authorSearchStrategy = authorSearchStrategy;
        this.isbnSearchStrategy = isbnSearchStrategy;
    }

    public SearchStrategy getSearchStrategy(String searchType) {
        switch (searchType.toLowerCase()) {
            case "title":
                return titleSearchStrategy;
            case "author":
                return authorSearchStrategy;
            case "isbn":
                return isbnSearchStrategy;
            default:
                throw new IllegalArgumentException("Unknown search type: " + searchType);
        }
    }
}

