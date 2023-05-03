package org.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
class TransformOperatorTest {

    @Test
    void flatMapOperator() {
        Shop shop = Flux.just(
                Shop.builder().shopId(1).size(0).build(), // 800ms
                Shop.builder().shopId(2).size(5).build(), // 200ms
                Shop.builder().shopId(3).size(5).build(), // 100ms
                Shop.builder().shopId(4).size(0).build() // 0ms
            )
            .flatMap(this::doSomethingAsync) // delay 발생시킴
            .takeUntil(Shop::hasSize)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        System.out.println("shop = " + shop);
    }

    @Test
    void flatMapSequentialOperator() {
        Shop shop = Flux.just(
                Shop.builder().shopId(1).size(0).build(),
                Shop.builder().shopId(2).size(5).build(),
                Shop.builder().shopId(3).size(5).build(),
                Shop.builder().shopId(4).size(0).build()
            )
            .flatMapSequential(this::doSomethingAsync)
            .takeUntil(Shop::hasSize)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        System.out.println("shop = " + shop);
    }

    @Test
    void concatMapOperator() {
        Shop shop = Flux.just(
                Shop.builder().shopId(1).size(0).build(),
                Shop.builder().shopId(2).size(5).build(),
                Shop.builder().shopId(3).size(5).build(),
                Shop.builder().shopId(4).size(0).build()
            )
            .concatMap(this::doSomethingAsync)
            .takeUntil(Shop::hasSize)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        System.out.println("shop = " + shop);
    }

    private Mono<Shop> doSomethingAsync(Shop shop) {
        Mono<Shop> mono = Mono.just(shop)
            .doOnNext(element -> log.info("Executing {}", element));

        switch (shop.getShopId()) {
            case 1:
                return mono.delayElement(Duration.ofMillis(800));
            case 2:
                return mono.delayElement(Duration.ofMillis(200));
            case 3:
                return mono.delayElement(Duration.ofMillis(100));
            default:
                return mono;
        }
    }

    @Test
    void concatMapPrefetch() {
        Flux.range(1, 10)
            .concatMap(i -> Flux.just(i).delayElements(Duration.ofMillis(100)), 2)
            .doOnNext(n -> log.info("Executing {}", n))
            .subscribe();
    }
}
