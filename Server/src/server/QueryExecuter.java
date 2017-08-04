/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import io.netty.channel.ChannelHandlerContext;
import java.sql.*;

/**
 *
 * @author Анатолий
 */
public final class QueryExecuter {
    public static boolean userRegistration(Connection connect, String user, String password) {
        CallableStatement callableStatement = null;
        boolean wasConnected = false;

       try{
         callableStatement = connect.prepareCall("{ ? = call LogInOperation(?, ?) }");
     
         callableStatement.registerOutParameter(1, Types.BOOLEAN);

         callableStatement.setString(2, user);
 
         callableStatement.setString(3, password);
         
         callableStatement.execute();


         wasConnected = callableStatement.getBoolean(1);
         System.out.println(user + " tried to connect");

      
      } catch(SQLException e) {
          e.printStackTrace();
      }
        return wasConnected;
    }
}
