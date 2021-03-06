package me.june.spring.domain;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Table("tacoorders")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id = Uuids.timeBased();

    private Date placedAt = new Date();

    @Column("user")
    private UserUDT user;

    @Column("tacos")
    private List<TacoUDT> tacos = new ArrayList<>();

    public void addDesign(TacoUDT design) {
        this.tacos.add(design);
    }
}
