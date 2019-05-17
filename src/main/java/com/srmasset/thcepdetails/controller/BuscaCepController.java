/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srmasset.thcepdetails.controller;


import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.srmasset.thcepdetails.model.DadosCep;
import com.srmasset.thcepdetails.model.DadosRequestCep;
import com.srmasset.thcepdetails.service.BuscaCepService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author lprates
 */
@RestController
@RequestMapping("/cep")
public class BuscaCepController {
    //modelo requisicao 
    //https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/02440050
    
    String url = "https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/";
    
    Logger log = LoggerFactory.getLogger(HealthcheckController.class);
    
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JedisPool jedisPool ; 
    
    @Autowired
    private BuscaCepService buscaCepService ; 
    

    /*
        Modelo de requisicao 
        http://localhost:9090/cep/unico/02440050
    
    */
    
    @HystrixCommand(fallbackMethod = "tratamento_erro_timeout", commandProperties = {
       @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })    
    @RequestMapping(value = "/unico/{cep}" , method = GET)
    public ResponseEntity<DadosCep> testeBuscaCepUnico(@PathVariable("cep") String  cep  ) throws InterruptedException{
        Thread.sleep(5000);
        DadosCep dados = buscaCepService.buscaCepUnico(cep); 
        return new ResponseEntity<DadosCep>(dados, HttpStatus.OK ); 
    }
    
    
    /* Modelo de requisicao 

            post http://localhost:9090/cep/multiplo 
    
            [
                    {
                            "cep":"02440050" 
                    },
                    {
                            "cep":"04003010"
                    }

            ]    
    
    */
    @RequestMapping(value = "/multiplo" , method = RequestMethod.POST)
    public ResponseEntity<List<DadosCep>> testeBuscaCepMultiplo(@RequestBody List<DadosRequestCep> listaDadosRequestCep){

        List<DadosCep> listaCeps = buscaCepService.buscaCepMultiplo(listaDadosRequestCep);
        return new ResponseEntity<List<DadosCep>>(listaCeps, HttpStatus.OK ); 
    }
    
    
    private ResponseEntity<DadosCep> tratamento_erro_timeout(String  cep ){
        
        DadosCep dadosCep = new DadosCep();
        return new ResponseEntity<DadosCep>(dadosCep, HttpStatus.INTERNAL_SERVER_ERROR ); 
    }

    
}
