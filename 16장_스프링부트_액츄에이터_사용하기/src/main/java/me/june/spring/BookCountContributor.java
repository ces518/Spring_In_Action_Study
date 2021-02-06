package me.june.spring;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

// /info 엔드포인트에 추가로 제공하고 싶은 정보가 있다면 InfoContributor 인터페이스를 구현
@Component
public class BookCountContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        long count = 1L; // dummy data
        builder.withDetail("book-stats", Map.of("count", count));
    }
}
