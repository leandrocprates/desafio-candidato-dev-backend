/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srmasset.thcepdetails.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 *
 * @author lprates
 */
@RestController
@RequestMapping("/healthcheck")
public class HealthcheckController {
    
    Logger log = LoggerFactory.getLogger(HealthcheckController.class);
    
    @RequestMapping(value = "/info" , method = GET)
    public ResponseEntity<String> testeExceptionHandler(){
        log.info("Dentro do controller MainRestController antes de lancar excecao .");
        return new ResponseEntity<String>("Servico OK ", HttpStatus.OK ) ; 
    }
 
    
    
}
