/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.util;

import java.sql.Connection;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author jarvis
 */
public class LoadDatasource {
    
    public static Connection getConnection(){
        DataSource ds = null;
        Connection conn = null;
        try{
            InitialContext ctx = new InitialContext();
            ds = (DataSource)ctx.lookup("jdbc/dbuser");
            conn = ds.getConnection();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return conn;
        
    }
}
