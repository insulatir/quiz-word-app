package com.example.wordapi.service;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class MorphologicalAnalyzer {

    public List<String> extractWords(String text) {
        List<String> tokens = new ArrayList<>();

        try (JapaneseAnalyzer analyzer = new JapaneseAnalyzer()) {
            TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(text));
            CharTermAttribute charTermAttr = tokenStream.addAttribute(CharTermAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                tokens.add(charTermAttr.toString());
            }
            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;
    }
}
