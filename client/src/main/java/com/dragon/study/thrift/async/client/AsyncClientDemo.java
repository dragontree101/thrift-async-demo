package com.dragon.study.thrift.async.client;

import com.dragon.study.thrift.async.api.Calculator;
import com.dragon.study.thrift.async.api.InvalidOperation;
import com.dragon.study.thrift.async.api.Operation;
import com.dragon.study.thrift.async.api.Work;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import java.util.concurrent.CountDownLatch;

/**
 * Created by dragon on 16/4/7.
 */
public class AsyncClientDemo {

  public static void main(String[] args) {

    try {
      TNonblockingTransport transport1 = new TNonblockingSocket("localhost", 9090);
      TNonblockingTransport transport2 = new TNonblockingSocket("localhost", 9090);
      TNonblockingTransport transport3 = new TNonblockingSocket("localhost", 9090);
      TNonblockingTransport transport4 = new TNonblockingSocket("localhost", 9090);
      TNonblockingTransport transport5 = new TNonblockingSocket("localhost", 9090);

      TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
      TAsyncClientManager clientManager = new TAsyncClientManager();
      CountDownLatch latch = new CountDownLatch(5);
      System.out.println("Client start .....");

      Calculator.AsyncClient client = new Calculator.AsyncClient(protocolFactory, clientManager, transport1);
      long startTime = System.currentTimeMillis();
      PingAsynCallback pingCallBack = new PingAsynCallback(latch);
      System.out.println("call method ping call start ...");
      client.ping(pingCallBack);
      System.out.println("call method ping call  .... end");
      transport1.close();

      client = new Calculator.AsyncClient(protocolFactory, clientManager, transport2);
      AddAsynCallback addCallBack = new AddAsynCallback(latch);
      System.out.println("add method ping call start ...");
      client.add(1, 1, addCallBack);
      System.out.println("add method ping call  .... end");

      Work work = new Work();
      work.op = Operation.DIVIDE;
      work.num1 = 1;
      work.num2 = 0;
      client = new Calculator.AsyncClient(protocolFactory, clientManager, transport3);
      CalculateAsynCallback calculateCallBack1 = new CalculateAsynCallback(latch);
      System.out.println("calculate method ping call start ...");
      client.calculate(1, work, calculateCallBack1);
      System.out.println("calculate method ping call  .... end");

      work.op = Operation.SUBTRACT;
      work.num1 = 15;
      work.num2 = 10;
      client = new Calculator.AsyncClient(protocolFactory, clientManager, transport4);
      CalculateAsynCallback calculateCallBack2 = new CalculateAsynCallback(latch);
      System.out.println("calculate method ping call start ...");
      client.calculate(1, work, calculateCallBack2);
      System.out.println("calculate method ping call  .... end");

      client = new Calculator.AsyncClient(protocolFactory, clientManager, transport5);
      GetStructAsynCallback getStructAsynCallback = new GetStructAsynCallback(latch);
      System.out.println("get struct method ping call start ...");
      client.getStruct(1, getStructAsynCallback);
      System.out.println("get struct method ping call  .... end");

      latch.await();
      System.out.println("use time " + (System.currentTimeMillis()-startTime) + " ms");

      transport1.close();
      transport2.close();
      transport3.close();
      transport4.close();
      transport5.close();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  private static class PingAsynCallback implements AsyncMethodCallback<Calculator.AsyncClient.ping_call> {
    private CountDownLatch latch;

    public PingAsynCallback(CountDownLatch latch) {
      this.latch = latch;
    }
    public void onComplete(Calculator.AsyncClient.ping_call response) {
      System.out.println("PingAsynCallback onComplete");
      try {
        response.getResult();
        System.out.println("PingAsynCall result is void ");
      } catch (TException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        latch.countDown();
      }
    }
    public void onError(Exception exception) {
      System.out.println("PingAsynCallback onError :" + exception.getMessage());
      latch.countDown();
    }
  }

  private static class AddAsynCallback implements AsyncMethodCallback<Calculator.AsyncClient.add_call> {
    private CountDownLatch latch;

    public AddAsynCallback(CountDownLatch latch) {
      this.latch = latch;
    }
    public void onComplete(Calculator.AsyncClient.add_call response) {
      System.out.println("AddAsynCall onComplete");
      try {
        System.out.println("AddAsynCall result is " + response.getResult());
      } catch (TException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }finally {
        latch.countDown();
      }
    }
    public void onError(Exception exception) {
      System.out.println("AddAsynCall onError :" + exception.getMessage());
      latch.countDown();
    }
  }

  private static class CalculateAsynCallback implements AsyncMethodCallback<Calculator.AsyncClient.calculate_call> {
    private CountDownLatch latch;

    public CalculateAsynCallback(CountDownLatch latch) {
      this.latch = latch;
    }
    public void onComplete(Calculator.AsyncClient.calculate_call response) {
      System.out.println("CalculateAsynCallback onComplete");
      try {
        System.out.println("CalculateAsynCallback result is " + response.getResult());
      } catch (InvalidOperation e) {
        System.out.println("Invalid operation: " + e.why);
      } catch (TException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }finally {
        latch.countDown();
      }
    }
    public void onError(Exception exception) {
      System.out.println("CalculateAsynCallback onError :" + exception.getMessage());
      latch.countDown();
    }
  }


  private static class GetStructAsynCallback implements AsyncMethodCallback<Calculator.AsyncClient.getStruct_call> {
    private CountDownLatch latch;

    public GetStructAsynCallback(CountDownLatch latch) {
      this.latch = latch;
    }
    public void onComplete(Calculator.AsyncClient.getStruct_call response) {
      System.out.println("getStructAsynCallback onComplete");
      try {
        System.out.println("getStructAsynCallback result is " + response.getResult().getValue());
      } catch (TException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }finally {
        latch.countDown();
      }
    }
    public void onError(Exception exception) {
      System.out.println("getStructAsynCallback onError :" + exception.getMessage());
      latch.countDown();
    }
  }
}
