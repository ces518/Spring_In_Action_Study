package me.june.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.stream.Stream;

@SpringBootTest
class ApplicationTests {

    @Test
    @DisplayName("Flux just 로 생성하기")
    public void createAFlux_just() {
        // Flux 혹은 Mono 로 생성하려는 하나 이상의 객체가 있다면, just() 메소드로 사용이 가능하다.
        // 인자가 1개 라면 FluxJust 객체로 생성, 1개 이상이라면 FluxArray 로 생성된다. -> 모두 Flux 타입으로 취급
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");
        // 현재 Flux 를 생성했지만, 구독자 (subscriber) 가 없기 때문에 데이터가 전달되지 않음..
        // 수도꼭지에 호스를 끼운것과 같다.

        // 구독자 추가시 subscribe 메소드를 사용한다.
        // 전달되는 것은 java.util.Consumer -> Subscriber 객체를 생성하기위해 사용됨
        fruitFlux.subscribe(
                f-> System.out.println("Here's some fruit : " + f)
        );

        // 위와 같이 콘솔에 찍는 방식으로 테스트하는것도 좋지만, StepVerifier (reactor test) 를 사용하는것 이더 좋은 방법이다.
        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    @DisplayName("배열로 부터 Flux 생성하기")
    public void createAFlux_fromArray() {
        String[] fruits = new String[] {
                "Apple", "Orange", "Grape", "Banana", "Strawberry"
        };

        // Flux.just 의 인자가 1개 이상이면 fromArray 를 호출해서 생성하게 때문에 동일하다.
        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    @DisplayName("Iterable 로 부터 Flux 생성하기")
    public void createAFlux_fromIterable() {
        List<String> fruits = List.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        // FluxIterable 객체로 생성된다.
        Flux<String> fruitFlux = Flux.fromIterable(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    @DisplayName("Stream 으로 부터 Flux 생성하기")
    public void createAFlux_fromStream() {
        Stream<String> fruitStream = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        // FluxStream 객체로 생성된다.
        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }


    @Test
    @DisplayName("범위로 Flux 생성하기")
    public void createAFlux_range() {
        // 두번째 인자가 1이라면, just 로 생성, 아닐 경우 FluxRange 객체로 생성된다.
        Flux<Integer> intervalFlux = Flux.range(1, 5);

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    @DisplayName("interval 로 Flux 생성하기")
    public void createAFlux_interval() {
        // range 와 비슷하지만, interval 은 방출되는 시간 간격 혹은 주기를 지정한다.
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1))
                .take(5);

        // 이는 0 부터 시작한다점에 유의해야 한다.
        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Flux 결합하기")
    public void mergeFluxes() {
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500)); // 500 밀리초마다 방출되도록 설정

        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250)) // foodFlux 가 250 밀리초 후 스트리밍을 시작하도록 설정
                .delayElements(Duration.ofMillis(500));

        // 2개의 Flux 결합
        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples");
    }

    @Test
    @DisplayName("Zip 으로 결합하기")
    public void zipFluxes() {
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa");

        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples");

        // merge 는 두개의 Flux 가 완벽하게 번갈아 방출되는것을 보장할 수 없다.
        // zip 오퍼레이션을 이용하면 (Garfield, Lasagna), (Kojak, Lollipops) 형태로 방출됨을 보장할 수 있다.
        // 기본적으로 Tuple 타입 객체가 사용되며, 3번째 인자로 원하는 객체 생성자를 넣어주면 해당 타입으로 묶인다.
        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p -> p.getT1().equals("Garfield") && p.getT2().equals("Lasagna"))
                .expectNextMatches(p -> p.getT1().equals("Kojak") && p.getT2().equals("Lollipops"))
                .expectNextMatches(p -> p.getT1().equals("Barbossa") && p.getT2().equals("Apples"));

    }

    @Test
    @DisplayName("먼저 방출하는 리액티브 타입 선택 하기")
    public void firstFlux() {
        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));

        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        // 둘중 먼저 방출되는 Flux 의 값을 선택한다.
        Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }

    @Test
    @DisplayName("skip 으로 건너뛰기")
    public void skipAFew() {
        Flux<String> skipFlux = Flux.just("one", "two", "three", "four", "five", "six")
                .skip(3);

        StepVerifier.create(skipFlux)
                .expectNext("four")
                .expectNext("five")
                .expectNext("six")
                .verifyComplete();
    }

    @Test
    @DisplayName("skip 으로 시간동안 건너뛰기")
    public void skipAFewSeconds() {
        Flux<String> skipFlux = Flux.just("one", "two", "three", "four", "five", "six")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));

        StepVerifier.create(skipFlux)
                .expectNext("four")
                .expectNext("five")
                .expectNext("six")
                .verifyComplete();
    }

    @Test
    @DisplayName("take 로 지정한 수만큼 방출")
    public void take() {
        Flux<String> skipFlux = Flux.just("one", "two", "three", "four", "five", "six")
                .take(3);

        StepVerifier.create(skipFlux)
                .expectNext("one")
                .expectNext("two")
                .expectNext("three")
                .verifyComplete();
    }

    @Test
    @DisplayName("take 로 지정한 시간동안 방출")
    public void takeSeconds() {
        Flux<String> skipFlux = Flux.just("one", "two", "three", "four", "five", "six")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500));

        StepVerifier.create(skipFlux)
                .expectNext("one")
                .expectNext("two")
                .expectNext("three")
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 로 특정 조건에 따라 선택적인 발행")
    public void filter() {
        Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter(np -> !np.contains(" "));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Zion")
                .verifyComplete();
    }

    @Test
    @DisplayName("distinct 로 중복 제거")
    public void distinct() {
        Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater");

        StepVerifier.create(animalFlux)
                .expectNext("dog", "cat", "bird", "anteater")
                .verifyComplete();
    }

    class Player {
        String firstname;
        String lastname;

        public Player(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return Objects.equals(firstname, player.firstname) && Objects.equals(lastname, player.lastname);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstname, lastname);
        }
    }

    @Test
    @DisplayName("map 으로 변환하기")
    public void map() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(playerFlux)
                .expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen"))
                .expectNext(new Player("Steve", "Kerr"))
                .verifyComplete();
    }

    @Test
    @DisplayName("flatMap 으로 변환하기")
    public void flatMap() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n ->
                    Mono.just(n).map(
                            p -> {
                                String[] split = n.split("\\s");
                                System.out.println(Arrays.toString(split));
                                System.out.println(Thread.currentThread().getName());
                                return new Player(split[0], split[1]);
                            })
                            .subscribeOn(Schedulers.parallel())
                            // subscribeOn 은 subscribe 와 다름
                );

        // Schedulers.parallel() 은 다른 스레드풀과는 다르게 동작한다.
        // ParallelScheduler 를 사용하고, ScheduledThreadPoolExecutor 를 사용하고 있다..
        // https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ScheduledThreadPoolExecutor.html

        List<Player> players = List.of(new Player("Michael", "Jordan"), new Player("Scottie", "Pippen"), new Player("Steve", "Kerr"));

        StepVerifier.create(playerFlux)
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .verifyComplete();
    }

    @Test
    @DisplayName("buffer 로 버퍼링하기")
    public void buffer() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3); // buffer 는 지정된 숫자만큼 버퍼링 하여 출력한다.

        // 리액티브가 아닌 List 컬렉션으로 버퍼링 되기 때문에 비효율적인것 처럼 보일 수 있지만, flatMap 과 함께 사용하면
        // 각 List 컬렉션을 병행으로 처리가 가능하다.

        fruitFlux.buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                            .map(y -> y.toUpperCase())
                            .subscribeOn(Schedulers.parallel())
                            .log() // log 는 모든 스트림 이벤트를 로깅한다.
                ).subscribe();

        /*
        2021-01-20 21:49:01.822  INFO 707 --- [           main] reactor.Flux.SubscribeOn.1               : onSubscribe(FluxSubscribeOn.SubscribeOnSubscriber)
        2021-01-20 21:49:01.825  INFO 707 --- [           main] reactor.Flux.SubscribeOn.1               : request(32)
        2021-01-20 21:49:01.828  INFO 707 --- [           main] reactor.Flux.SubscribeOn.2               : onSubscribe(FluxSubscribeOn.SubscribeOnSubscriber)
        2021-01-20 21:49:01.828  INFO 707 --- [           main] reactor.Flux.SubscribeOn.2               : request(32)
        2021-01-20 21:49:01.830  INFO 707 --- [     parallel-1] reactor.Flux.SubscribeOn.1               : onNext(APPLE)
        2021-01-20 21:49:01.831  INFO 707 --- [     parallel-1] reactor.Flux.SubscribeOn.1               : onNext(ORANGE)
        2021-01-20 21:49:01.830  INFO 707 --- [     parallel-2] reactor.Flux.SubscribeOn.2               : onNext(KIWI)
        2021-01-20 21:49:01.831  INFO 707 --- [     parallel-1] reactor.Flux.SubscribeOn.1               : onNext(BANANA)
        2021-01-20 21:49:01.831  INFO 707 --- [     parallel-2] reactor.Flux.SubscribeOn.2               : onNext(STRAWBERRY)
        2021-01-20 21:49:01.831  INFO 707 --- [     parallel-2] reactor.Flux.SubscribeOn.2               : onComplete()
        2021-01-20 21:49:01.831  INFO 707 --- [     parallel-1] reactor.Flux.SubscribeOn.1               : onComplete()
         */

        StepVerifier.create(bufferedFlux)
                .expectNext(List.of("apple", "orange", "banana"))
                .expectNext(List.of("kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    @DisplayName("collectList 로 Mono 반환하기")
    public void collectList() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");

        // CollectList 를 호출하면 Mono 를 생성한다.
        Mono<List<String>> fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(List.of("apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    @DisplayName("collectMap 으로 Map 을 포함하는 Mono 반환하기")
    public void collectMap() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

        // Map 을 포함하는 Mono 를 생성한다.
        Mono<Map<Character, String>> animalMapMono = animalFlux.collectMap(a -> a.charAt(0));

        StepVerifier.create(animalMapMono).expectNextMatches(map -> {
            return map.size() == 3 &&
                    map.get('a').equals("aardvark") &&
                    map.get('e').equals("eagle") &&
                    map.get('k').equals("kangaroo");
        })
        .verifyComplete();
    }

    @Test
    @DisplayName("all 로 로직 오퍼레이션 수행")
    public void all() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));

        StepVerifier.create(hasAMono)
                .expectNext(true)
                .verifyComplete();

        Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));

        StepVerifier.create(hasKMono)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("any 로 로직 오퍼레이션 수행")
    public void any() {
        Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Boolean> hasTMono = animalFlux.any(a -> a.contains("t"));

        StepVerifier.create(hasTMono)
                .expectNext(true)
                .verifyComplete();

    }
}
