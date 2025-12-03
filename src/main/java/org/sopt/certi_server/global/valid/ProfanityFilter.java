package org.sopt.certi_server.global.valid;

import jakarta.annotation.PostConstruct;
import org.ahocorasick.trie.Trie;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfanityFilter {

    private Trie trie;


    @PostConstruct
    public void init(){
        List<String> badWords = List.of("병신", "시발", "개새끼", "fuck");

        this.trie = Trie.builder()
                .addKeywords(badWords)
                .ignoreCase()
                .ignoreOverlaps()
                .build();
    }

    public boolean containsProfanity(String keyword){
        // 특수문자 제거
        String purifiedValue = keyword.replaceAll("[^가-힣a-zA-Z0-9]", "");
        return trie.containsMatch(purifiedValue);
    }
}
