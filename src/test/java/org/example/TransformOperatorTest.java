package org.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
class TransformOperatorTest {

    @Test
    void diffOperator() {
        long startTime = System.currentTimeMillis();
        Flux.just(1, 2, 3, 4)
//            .flatMap(this::doSomethingAsync)
//            .flatMapSequential(this::doSomethingAsync)
            .concatMap(this::doSomethingAsync, 1)
//            .concatMap(this::doSomethingAsync)
            .doOnNext(n -> log.info("Done {}", n))
            .blockLast();

        long endTime = System.currentTimeMillis();
        log.info("Elapsed time: {}", endTime - startTime);
    }

    private Mono<Integer> doSomethingAsync(int number) {
        Mono<Integer> mono = Mono.just(number)
            .doOnNext(n -> log.info("Executing {}", n));

        switch (number) {
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
