package me.june.spring.domain;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table("tacos")
public class Taco {

    // 파티션 키로 사용한다.
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID id = Uuids.timeBased();

    private String name;

    // 클러스터키로 사용한다. (파티션 내부에서 행의 순서 결정)
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Date createdAt = new Date();

    // Ingredient 대신 IngredientUDT 객체 사용
    // 카산드라는 비정규화 되어 중복 데이터를 포함 가능하다.
    // 데이터 컬렉션을 포함하는 열은, 네이티브 컬렉션 혹은 사용자 정의타입 (User Defined Type, UDT) 여야 한다.
    // UDT 는 관계형 데이터베이스의 외부 키처럼 사용됨, 실제 복사될 수 있는 데이터를 가지고 있다.
    // Ingredient 클래스는 @Table 애노테이션으로 엔티티로 매핑되었기 때문에 재사용할 수 없다.
    // Ingredient 가 Taco 테이블의 ingredients 열에 어떻게 저장되어야 하는지 정의하기 위한 클래스
    @Column("ingredients")
    private List<IngredientUDT> ingredients;
}
