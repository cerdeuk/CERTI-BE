package org.sopt.certi_server.global.valid;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Trie;
import org.sopt.certi_server.global.annotation.NoProfanity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProfanityValidator implements ConstraintValidator<NoProfanity, String> {

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

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // 특수문자 제거
        String purifiedValue = value.replaceAll("[^가-힣a-zA-Z0-9]", "");
        log.info("purifiedValue = {}", purifiedValue);

        return !trie.containsMatch(value);
    }
}
