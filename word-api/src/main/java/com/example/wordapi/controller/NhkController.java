package com.example.wordapi.controller;

import com.example.wordapi.service.NhkNewsCrawler;
import com.example.wordapi.service.NhkNewsCrawler.NewsItem;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/news")
public class NhkController {

    private final NhkNewsCrawler newsCrawler;

    public NhkController(NhkNewsCrawler newsCrawler) {
        this.newsCrawler = newsCrawler;
    }

    @GetMapping("/top")
    public ResponseEntity<List<NewsItem>> getTopNews() {
        List<NewsItem> newsList = newsCrawler.fetchTopHeadlines();
        return ResponseEntity.ok(newsList);
    }
    
    @GetMapping("/detail")
    public ResponseEntity<String> getArticleDetail(@RequestParam String url) {
        String body = newsCrawler.fetchArticleBody(url);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/sample")
    public String getSampleNews() {
        List<NewsItem> headlines = newsCrawler.fetchTopHeadlines();
        if (!headlines.isEmpty()) {
            return newsCrawler.fetchArticleBody(headlines.get(0).url());
        } else {
            return "뉴스를 가져오지 못했습니다.";
        }
    }
}
