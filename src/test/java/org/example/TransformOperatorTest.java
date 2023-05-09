package org.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
class TransformOperatorTest {

    @Test
    @DisplayName("flatMap() operator")
    void flatMapOperator() {
        Shop shop = Flux.just(
                Shop.builder().shopId(1).size(0).delay(800).build(),
                Shop.builder().shopId(2).size(5).delay(200).build(),
                Shop.builder().shopId(3).size(5).delay(100).build(),
                Shop.builder().shopId(4).size(0).delay(0).build()
            )
            .log()
            .flatMap(this::doSomethingAsync, 2, 1)
            .takeUntil(Shop::hasSize)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        System.out.println("shop = " + shop);
    }

    @Test
    @DisplayName("flatMapSequential() operator")
    void flatMapSequentialOperator() {
        Shop shop = Flux.just(
                Shop.builder().shopId(1).size(0).delay(800).build(),
                Shop.builder().shopId(2).size(5).delay(200).build(),
                Shop.builder().shopId(3).size(5).delay(100).build(),
                Shop.builder().shopId(4).size(0).delay(0).build()
            )
            .flatMapSequential(this::doSomethingAsync)
            .takeUntil(Shop::hasSize)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        System.out.println("shop = " + shop);
    }

    @Test
    @DisplayName("concatMap() operator")
    void concatMapOperator() {
        Shop shop = Flux.just(
                Shop.builder().shopId(1).size(0).delay(800).build(),
                Shop.builder().shopId(2).size(5).delay(200).build(),
                Shop.builder().shopId(3).size(5).delay(100).build(),
                Shop.builder().shopId(4).size(0).delay(0).build()
            )
            .log()
            .concatMap(this::doSomethingAsync)
            .takeUntil(Shop::hasSize)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        System.out.println("shop = " + shop);
    }

    private Mono<Shop> doSomethingAsync(Shop shop) {
        return Mono.just(shop)
            .doOnNext(element -> log.info("Executing {}", element))
            .delayElement(Duration.ofMillis(shop.getDelay()));
    }
}
