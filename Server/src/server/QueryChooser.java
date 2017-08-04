/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Анатолий
 */
public final class QueryChooser {
    public static void choose(ChannelHandlerContext ctx, Connection mySt, String query) {
        String [] q = query.split("\n");

        
        if (q.length == 3) {
        switch (q[0]) {
            case "connect" :
                boolean res = QueryExecuter.userRegistration(mySt, q[1], q[2]);
                if (res)
                   ctx.write(Unpooled.copiedBuffer("connected\r\n", CharsetUtil.UTF_8));
                else
                    ctx.disconnect();
                return;
               
            default : 
                
            
        }
        
        }
        return ;
    }
}
