/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Анатолий
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.util.Arrays;
import io.netty.buffer.Unpooled;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.*;


public class Server {
  int port, concurrentClientCount = 0;
  static Connection connection;
  static Statement mySt;
  public Server(int port) {
    this.port = port;
    this.concurrentClientCount = 0;
  }

  public void run() throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
       .channel(NioServerSocketChannel.class)
       .childHandler(new ChannelInitializer<SocketChannel>() {
         @Override
         public void initChannel(SocketChannel ch) throws Exception {
             ch.pipeline().addLast(new ServerHandler());
         }
       })
       .option(ChannelOption.SO_BACKLOG, 128)
       .childOption(ChannelOption.SO_KEEPALIVE, true);

      // Bind and start to accept incoming connections.
      ChannelFuture f = b.bind(port).sync();

      // Wait until the server socket is closed.
      // In this example, this does not happen, but you can do that to gracefully
      // shut down your server.
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }


  public static void main(String[] args) throws Exception {
    
      connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/server", "root", null); 

      
     
      new Server(28563).run();
      
  }


  class ServerHandler extends ChannelInboundHandlerAdapter {
    String dataForProcessing = "";
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
      concurrentClientCount++;
      System.out.print(concurrentClientCount + " concurrent clients are connected\n");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      try {
        String allQueriesInOneString = dataForProcessing + ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        String[] queries = allQueriesInOneString.split("\r\n");
        for (int i =0; i < queries.length; ++i)
         System.out.println(queries[i]);
        if (!allQueriesInOneString.substring(allQueriesInOneString.length()-2).equals("\r\n"))
        {//если последний запрос неполный - запоминаем его для дальнейшего рассмотрения
          dataForProcessing = queries[queries.length-1];
          queries = Arrays.copyOf(queries, queries.length-1);
        }
        else dataForProcessing = "";
        for (String query : queries)
        {
         
          
          try {
          QueryChooser.choose(ctx, connection, query);
    
          
          //answer = "";
         
          }
          catch(Exception e) {
             // answer = "Unknown comand";
          }
          //answer += "\r\n";
         // ctx.write(Unpooled.copiedBuffer(answer, CharsetUtil.UTF_8));
        }
        ctx.flush();
      } finally {
        ((ByteBuf) msg).release();
      }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
        concurrentClientCount--;
        System.out.print("client was disc" + concurrentClientCount + "lost");
    }
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
      concurrentClientCount--;
      System.out.print("client was disc " + concurrentClientCount + " lost");
    }

       
  }
}