package com.example.wordapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wordapi.service.MorphologicalAnalyzer;
import com.example.wordapi.service.NhkNewsCrawler;

@RestController
@RequestMapping("/morph")
public class MorphController {

    private final MorphologicalAnalyzer analyzer;
    private final NhkNewsCrawler crawler;

    public MorphController(MorphologicalAnalyzer analyzer, NhkNewsCrawler crawler) {
        this.analyzer = analyzer;
        this.crawler = crawler;
    }

    @GetMapping("/test")
    public List<String> test() {
        // 예시: NHK 뉴스 첫 본문 가져와서 형태소 분석
        List<NhkNewsCrawler.NewsItem> list = crawler.fetchTopHeadlines();
        if (list.isEmpty()) return List.of();

        String url = list.get(0).url();
        String body = crawler.fetchArticleBody(url);

        return analyzer.extractWords(body);
    }
}

