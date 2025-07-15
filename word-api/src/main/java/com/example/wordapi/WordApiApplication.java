package com.example.wordapi;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.wordapi.model.WordEntry;
import com.example.wordapi.service.NhkNewsCrawler;
import com.example.wordapi.service.WordJsonBuilderService;

@SpringBootApplication
public class WordApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordApiApplication.class, args);
    }

    @Bean
    CommandLineRunner run(NhkNewsCrawler newsService, WordJsonBuilderService builderService) {
        return args -> {
            // (1) 뉴스 URL 설정
            String articleUrl = "https://www3.nhk.or.jp/news/html/20250714/k10014862901000.html";

            // (2) 뉴스 본문 가져오기
            String articleText = newsService.fetchArticleBody(articleUrl);
            if (articleText == null) {
                System.out.println("뉴스 본문을 가져오는 데 실패했습니다.");
                return;
            }

            // (3) 단어 JSON 생성
            List<WordEntry> entries = builderService.buildJson(articleText);

            Path csvPath = Paths.get("/shared/words.csv");
            Files.createDirectories(csvPath.getParent());
            if (!Files.exists(csvPath)) {
                Files.createFile(csvPath);
            }

            // (4) CSV 파일로 저장
            builderService.writeToCsv(entries, "/app/shared/words.csv");

            System.out.println("단어장 생성 완료: words.csv");
        };
    }
}
