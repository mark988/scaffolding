package com.myy.common.function;

import com.myy.common.exception.RedisConnectException;


@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
