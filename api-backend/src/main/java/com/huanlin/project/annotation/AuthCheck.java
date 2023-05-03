package com.huanlin.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 * 定义了一个注解类型，使用 @interface 可以定义一个新的注解类型，该注解可以用于类、方法、变量等代码元素上。
 * 其他方法上标注改注解 传入mustRole参数 admin
 * 再用AuthInterceptor进行环绕通知 从而进行方法拦截 实现传入参数admin逻辑才可以放行方法 不然则进行拦截
 */
//作用范围
@Target(ElementType.METHOD)
//保留策略 注解的生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个角色
     *
     * @return
     */
    String mustRole() default "";

}

