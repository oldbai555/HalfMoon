package com.oldbai.halfmoon.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 写一个注解，用来解决是否重复多次提交
 * 可以学习一下注解的知识
 * @author 老白
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTooFrequentCommit {
}
