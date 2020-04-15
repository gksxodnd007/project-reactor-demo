package org.codingsquid.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * @author hubert.squid
 * @since 2020.04.15
 */
public class SimpleFlux {

    @Test
    void printWord() {
        // given
        Flux<String> flux = Flux.just("hubert.squid", "software engineer", "backend developer", "good boy");

        // when, then
        flux.map(String::toUpperCase)
            .doOnNext(e -> System.out.println(e.toLowerCase()))
            .doOnNext(e -> System.out.println(Thread.currentThread().getName()))
            .doOnSubscribe(subscription -> {
                System.out.println("data stream start");
                subscription.request(2);
            })
            .subscribe(System.out::println);
    }

    @Test
    void cancel() {
        // given
        Flux<String> flux = Flux.just("hubert.squid", "software engineer", "backend developer", "good boy");

        // when, then
        flux.map(String::toUpperCase)
            .doOnNext(e -> System.out.println(e.toLowerCase()))
            .doOnNext(e -> System.out.println(Thread.currentThread().getName()))
            .doOnCancel(() -> System.out.println(">>> called cancel"))
            .doOnSubscribe(subscription -> {
                System.out.println("data stream start");
                subscription.cancel();
            })
            .subscribe(System.out::println);
    }
}
