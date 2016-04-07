package com.dragon.study.thrift.async.server;

import com.dragon.study.thrift.async.api.Calculator;
import com.dragon.study.thrift.async.server.impl.CalculatorHandler;

import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

/**
 * Created by dragon on 16/4/7.
 */
public class ServerDemo {

  public static CalculatorHandler handler;

  public static Calculator.Processor processor;

  public static void main(String [] args) {
    try {
      handler = new CalculatorHandler();
      processor = new Calculator.Processor(handler);

      Runnable simple = () -> simple(processor);

      new Thread(simple).start();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public static void simple(Calculator.Processor processor) {
    try {
      TNonblockingServerTransport nonblockingServerTransport = new TNonblockingServerSocket(9090);

      // Use this for a multithreaded server
       TServer server = new THsHaServer(new THsHaServer.Args(nonblockingServerTransport).processor(processor));

      System.out.println("Starting the simple server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
