/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srmasset.thcepdetails.service;

import com.google.gson.Gson;
import com.srmasset.thcepdetails.controller.HealthcheckController;
import com.srmasset.thcepdetails.model.DadosCep;
import com.srmasset.thcepdetails.model.DadosRequestCep;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author lprates
 */


@Service
public class BuscaCepService {
    
    Logger log = LoggerFactory.getLogger(HealthcheckController.class);
    
    String url = "https://zuul.trusthub.com.br/orchestrator/v1/obter-endereco-por-cep/";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JedisPool jedisPool ; 

    
    public DadosCep buscaCepUrl( String cep ){
        log.info("Buscando cep : " + cep );
        DadosCep dados = restTemplate.getForObject(url+cep, DadosCep.class); 
        log.info("Retorno : " + dados );
        return dados ; 
    }
    
    
    public DadosCep buscaCepRedis( String cep ) {
        
        Gson gson = new Gson();
        
        try (Jedis jedis = this.jedisPool.getResource()) {
            
            String retornoBusca  = jedis.get(cep) ;
            
            if ( retornoBusca != null ){
                DadosCep dados = gson.fromJson(retornoBusca, DadosCep.class); 
                return dados ; 
            }
            
            DadosCep dados = buscaCepUrl( cep ) ; 
            
            Long retorno = jedis.setnx( cep , gson.toJson(dados) );
            
            if ( retorno == 1 ){
                jedis.expire(cep , 60000 );
            }
            
            return dados ; 
        }
        
    }    
    
    public DadosCep buscaCepUnico ( String cep ){
         return buscaCepRedis( cep ) ; 
    }

    
    public List<DadosCep> buscaCepMultiplo ( List<DadosRequestCep> listaCeps ){
        
        List<DadosCep> listaDadosCeps = new ArrayList<DadosCep>() ; 
        
        for (DadosRequestCep dadosRequestCep :  listaCeps) {
             DadosCep dadosCep = buscaCepRedis(dadosRequestCep.getCep()) ; 
             listaDadosCeps.add( dadosCep) ; 
        }
        return listaDadosCeps ; 
         
    }

    
    
    
}
