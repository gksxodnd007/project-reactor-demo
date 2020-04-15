package org.codingsquid.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author hubert.squid
 * @since 2020.04.15
 */
public class SimpleFlux extends FluxTestSupport {

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

    @Test
    void parallel() throws InterruptedException {
        Flux<String> flux = Flux.just("hubert.squid", "software engineer", "backend developer", "good boy");

        // when, then
        flux.map(String::toUpperCase)
            .doOnNext(e -> System.out.println(e.toLowerCase() + " >>> " + Thread.currentThread().getName()))
            .publishOn(Schedulers.fromExecutor(executor))
            .doOnNext(e -> System.out.println(">>> " + Thread.currentThread().getName()))
            .publishOn(Schedulers.fromExecutor(executor))
            .parallel(4)
            .runOn(Schedulers.fromExecutor(executor))
            .doOnNext(e -> {
                try {
                    // parallel이 아니라면 4초가 지연되어야 하지만 parallel(4).runOn(Schedulers.fromExecutor(executor))의 영향으로 1초만 기다리게된다.
                    // executor는 20개의 쓰레드를 관리하고 있는 쓰레드 풀 이므로 20개가 넘어가는 data를 parallel하게 sleep주게 되면 1초 이상의 지연시간을 갖게된다.
                    Thread.sleep(1000);
                    System.out.println("sleep >>> " + Thread.currentThread().getName());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            })
            .doOnSubscribe(subscription -> subscription.request(2))
            .subscribe(e -> System.out.println(e + " >>> " + Thread.currentThread().getName()));

        Thread.sleep(1500);
    }
}
