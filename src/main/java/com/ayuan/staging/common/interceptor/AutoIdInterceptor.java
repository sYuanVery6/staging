package com.ayuan.staging.common.interceptor;


import com.ayuan.staging.common.annonation.mybatis.AutoId;
import com.ayuan.staging.common.util.autoid.IdStrategy;
import com.ayuan.staging.common.util.autoid.MyIdStrategy;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author sYuan
 * @Description autoId插件
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class})})
public class AutoIdInterceptor implements Interceptor {

    private IdStrategy idStrategy = new MyIdStrategy();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args.length == 2 && args[1] != null) {
            Object paramObject = args[1];
            Class<?> aClass = paramObject.getClass();

            AutoId annotation = aClass.getAnnotation(AutoId.class);
            if(annotation!=null){
                String name = annotation.name();
                Field declareField = aClass.getDeclaredField(name);
                declareField.setAccessible(true);
                declareField.set(paramObject,idStrategy.product().toString());

                args[1] = paramObject;
            }

        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

        System.out.println("into mybatis interceptor ...");

        properties.entrySet().forEach(
                p-> System.out.println(p.getKey()+" : "+p.getValue())
        );
    }
}
