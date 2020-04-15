package org.codingsquid.flux;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hubert.squid
 * @since 2020.04.15
 */
abstract class FluxTestSupport {

    protected static ExecutorService executor = Executors.newFixedThreadPool(20);
}
