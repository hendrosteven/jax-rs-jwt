/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.util;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author jarvis
 */
@ApplicationScoped
public class SimpleKeyGenerator implements KeyGenerator{

    @Override
    public Key generateKey() {
       String keyString = "mysecretkey";
       Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
       return key;
    }
    
}
