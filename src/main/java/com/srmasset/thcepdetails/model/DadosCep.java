/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srmasset.thcepdetails.model;

import lombok.Data;

@Data
public class DadosCep {
    private String estado ; 
    private String cidade ; 
    private String bairro ; 
    private String logradouro ; 
    
}
