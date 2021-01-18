package com.example.finderspring.endpoint;

import com.example.finderspring.service.SingleEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/finder")
public class SingleEmailController {

    @Autowired
    SingleEmailService singleEmailService;

    @GetMapping(value = "/health")
    public String healthCheck() {
        return "Application is Up and running";
    }

    @GetMapping(value = "/verify/{email}",produces = {"application/json"})
    public String singleEmailVerify(@PathVariable("email") String email){
        return singleEmailService.singleEmailVerify(email);
    }

    @GetMapping(value = "/find/{first}/{last}/{domain}",produces = {"application/json"})
    public String singleEmailFinder(@PathVariable("first") String first,@PathVariable("last") String last, @PathVariable("domain") String domain  ){
        return  singleEmailService.singleEmailFinder(first,last,domain);
    }
}
