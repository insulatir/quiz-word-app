package com.example.wordapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class NhkNewsCrawler {

	public List<NewsItem> fetchTopHeadlines() {
	    List<NewsItem> newsList = new ArrayList<>();
	    try {
	        Document doc = Jsoup.connect("https://www3.nhk.or.jp/news/").get();

	        // Top 뉴스 셀렉터 수정
	        Elements links = doc.select("article.module--news-main.index-main ul.content--list li dl dd a");

	        for (Element link : links) {
	            String title = link.text();
	            String url = "https://www3.nhk.or.jp" + link.attr("href");
	            newsList.add(new NewsItem(title, url));
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return newsList;
	}


    public String fetchArticleBody(String articleUrl) {
        try {
            Document doc = Jsoup.connect(articleUrl).get();
            Elements paragraphs = doc.select("article.module--detail--v3 section > section > div > p");

            StringBuilder bodyText = new StringBuilder();
            for (Element p : paragraphs) {
                bodyText.append(p.text()).append("\n");
            }

            return bodyText.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public record NewsItem(String title, String url) {}
}
