package me.june.spring.repository.jpa;

import me.june.spring.domain.Order;
import me.june.spring.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderJpaRepository extends CrudRepository<Order, Long> {
    List<Order> findByDeliveryZip(String deliveryZip);
    List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
    @Query("select o from Order o where o.deliveryCity = 'Seattle'")
    List<Order> readOrdersDeliveredInSeattle();
    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
