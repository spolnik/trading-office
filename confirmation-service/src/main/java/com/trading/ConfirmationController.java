package com.trading;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConfirmationController {

    @RequestMapping("confirmation")
    public String getConfirmation(@RequestParam(value="id", required = true) String id) {
        return "Confirmation #" + id;
    }
}
