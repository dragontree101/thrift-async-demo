package com.dragon.study.thrift.async.client;

import com.dragon.study.thrift.async.api.Calculator;
import com.dragon.study.thrift.async.api.InvalidOperation;
import com.dragon.study.thrift.async.api.Operation;
import com.dragon.study.thrift.async.api.SharedStruct;
import com.dragon.study.thrift.async.api.Work;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by dragon on 16/4/7.
 */
public class SyncClientDemo {

  public static void main(String[] args) {

    try {
      TTransport transport = new TFastFramedTransport(new TSocket("localhost", 9090));
      transport.open();

      TProtocol protocol = new TBinaryProtocol(transport);
      Calculator.Client client = new Calculator.Client(protocol);

      long startTime = System.currentTimeMillis();
      perform(client);

      System.out.println("use time " + (System.currentTimeMillis()-startTime) + " ms");
      transport.close();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  private static void perform(Calculator.Client client) throws TException {
    client.ping();
    System.out.println("ping()");

    int sum = client.add(1, 1);
    System.out.println("1+1=" + sum);

    Work work = new Work();

    work.op = Operation.DIVIDE;
    work.num1 = 1;
    work.num2 = 0;
    try {
      int quotient = client.calculate(1, work);
      System.out.println("Whoa we can divide by 0");
    } catch (InvalidOperation io) {
      System.out.println("Invalid operation: " + io.why);
    }

    work.op = Operation.SUBTRACT;
    work.num1 = 15;
    work.num2 = 10;
    try {
      int diff = client.calculate(1, work);
      System.out.println("15-10=" + diff);
    } catch (InvalidOperation io) {
      System.out.println("Invalid operation: " + io.why);
    }

    SharedStruct log = client.getStruct(1);
    System.out.println("Check log: " + log.value);
  }
}
