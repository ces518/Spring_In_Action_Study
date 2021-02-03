package me.june.spring.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import me.june.spring.dto.BookDto;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BookClient {
    private RestTemplate restTemplate;

    public BookClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 장애 발생시 *베가스의 규칙* 을 적용해야 한다.
    // 마이크로서비스에서 생긴 에러는 다른 곳에 전파하지 않고, 해당 서비스내에 남겨야함
    @HystrixCommand(fallbackMethod = "getDefaultBooks") // fallbackMethod 를 지정하면, 서킷 브레이커 동작시 해당 메소드를 호출한다.
    public List<BookDto> findAll() {
        ResponseEntity<List<BookDto>> responseEntity = restTemplate.exchange("http://book-service/books", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<BookDto>>() {});
        List<BookDto> results = responseEntity.getBody();
        return results;
    }

    // Hystrix 의 기본 타임아웃은 1초이며, 대부분의 경우 적합하다.
    // 다음은 타임아웃을 0.5초로 설정하는 예제이다.
    @HystrixCommand(fallbackMethod = "getDefaultBook",
        commandProperties = {
                @HystrixProperty(
                        name = "execution.isolation.thread.timeoutInMilliseconds", // 서킷브레이커 타임아웃 설정, 기본값은 1초
                        value = "500"
                ),
                @HystrixProperty(
                        name = "execution.timeout.enabled", // 타임아웃이 필요없는 경우 사용하는 설정. 연쇄 지연효과가 발생할 수 있으므로 조심해야 한다.
                        value = "false"
                ),
                @HystrixProperty(
                        name = "circuitBreaker.requestVolumeThreshold", // 지정된 시간내에 메소드가 호출되어야 하는 횟수
                        value = "30"
                ),
                @HystrixProperty(
                        name = "circuitBreaker.errorThresholdPercentage", // 지정된 시간내에 실패한 메소드 호출 비율, 50%
                        value = "25"
                ),
                @HystrixProperty(
                        name = "metrics.rollingStats.timeInMilliseconds", // 요청 횟수와 에러 비율이 고려되는 시간
                        value = "20000"
                ),
                @HystrixProperty(
                        name = "circuitBreaker.sleepWindowInMilliseconds", // 절반-열림 상태로 진입하여 실패한 메소드가 다시 시도되기 전 열림 상태서킷이 유지되는 시간
                        value = "20000"
                )
        }
    )
    // [기본 설정]
    // 10초동안 20번이상 호출되고, 50%이상이 실패한다면 서킷 브레이커가 동작하여 열림 상태가 된다.
    // 5초 후 절반-열림 상태가 되어 기존 메소드 호출이 다시 시도된다.
    // 위 설정은 20초동안 메소드가 30번이상 호출되어 25%이상 실패할 경우 서킷브레이커가 동작하도록 설정한 예제이다.
    public BookDto findById(Long bookId) {
        return restTemplate.getForEntity("http://book-serivce/books/{bookId}", BookDto.class, bookId).getBody();
    }

    // fallbackMethod 에도 서킷브레이커 지정이 가능하다.
    // @HystrixCommand 를 사용하여 연쇄적인 폴백 메소드들을 지정할 수 있다.
    private List<BookDto> getDefaultBooks() {
        return List.of(new BookDto(-1L, "제목", "작성자", "설명"));
    }

    private BookDto getDefaultBook(Long bookId) {
        return new BookDto(-1L, "제목", "작성자", "설명");
    }
}