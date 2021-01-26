package me.june.spring.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "books") // 몽고 DB 의 문서와 매핑 @Entity, @Table 과 동일
public class Book {

    @Id // 문서의 ID 값, String 타입일 경우 Persist 될 때, 몽고 DB 가 ID 값을 자동을 지정해줌
    private String id;
    // @Field @Column 과 동일, 생략가능
    private String title;
    private String author;

    // collection 타입은 카산드라와 유사하게 비정규화된 문서로 직접 저장한다.
    // 사용자 정의타입을 만들 필요 없이 어떤 타입도 사용이 가능하다.
    private List<String> etc;
}
