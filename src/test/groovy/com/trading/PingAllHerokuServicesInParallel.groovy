package com.trading

import groovyx.gpars.GParsPool
import groovyx.net.http.RESTClient
import org.slf4j.LoggerFactory

class PingAllHerokuServicesInParallel {

    static def log = LoggerFactory.getLogger(PingAllHerokuServicesInParallel.class)

    def static void main(String[] args) {

        GParsPool.withPool 16, {
            def services = [
                    new Tuple2<String, String>('eureka-server-staging', 'eureka-server'),
                    new Tuple2<String, String>('confirmation-service-staging', 'confirmation-service'),
                    new Tuple2<String, String>('market-data-service-staging', 'market-data-service'),
                    new Tuple2<String, String>('counterparty-service-staging', 'counterparty-service'),
                    new Tuple2<String, String>('trading-office-api-staging', 'trading-office-api'),
                    new Tuple2<String, String>('allocation-receiver-staging', 'allocation-message-receiver'),
                    new Tuple2<String, String>('allocation-enricher-staging', 'allocation-enricher'),
                    new Tuple2<String, String>('confirmation-sender-staging', 'confirmation-sender')
            ];

            services.each {
                this.&healthCheck.callAsync("http://${it.getFirst()}.herokuapp.com/");
                this.&healthCheck.callAsync("http://${it.getSecond()}.herokuapp.com/");
            }
        }
    }

    static def healthCheck(String url) {

        log.info("$url - checking...")
        def status = new RESTClient(url)
                .get(path: "health")
                .responseData.diskSpace.status.toString()

        log.info("$url - $status")
    }
}
