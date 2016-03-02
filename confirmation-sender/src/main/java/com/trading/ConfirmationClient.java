package com.trading;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("CONFIRMATION-SERVICE")
interface ConfirmationClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/confirmation",
            consumes = "application/json")
    void send(Confirmation confirmation);
}
