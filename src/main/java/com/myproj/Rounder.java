package com.myproj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dumin on HZ koga.
 */

@EnableAutoConfiguration
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.myproj"})
@PropertySource("classpath:rounder.properties")
@RestController
public class Rounder {

    private static final Logger logger = LoggerFactory.getLogger(Rounder.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/round/{amount:.+}", method = {RequestMethod.GET},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public double multipleAndGet(@PathVariable final double amount) {
        logger.info("rounding value - {}", amount);
        BigDecimal v = BigDecimal.valueOf(amount);
        BigDecimal roundOff = v.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return roundOff.doubleValue();
    }

    @RequestMapping("/roundDiscoveryClient/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    public static void main(String... args) throws Exception {
        SpringApplication.run(Rounder.class, args);
    }

}
