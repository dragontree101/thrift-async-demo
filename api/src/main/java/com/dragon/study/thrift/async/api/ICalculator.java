package com.dragon.study.thrift.async.api;

/**
 * Created by dragon on 16/4/7.
 */
public interface ICalculator extends Calculator.Iface {

  @Override
  void ping();

  @Override
  int add(int num1, int num2);

  @Override
  int calculate(int logid, Work w) throws InvalidOperation;

  @Override
  void zip();

  @Override
  SharedStruct getStruct(int key);

}
