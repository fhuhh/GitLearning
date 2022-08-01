package com.example.server.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@SuppressWarnings({"unchecked"})
public class CommonUtils {
    public static<T> List<T> stringToList(String s, Class<T> toClass){
        String[] strings=s.split(",");
        return Arrays.stream(strings)
                .map(item->{
                    try {
                        return (T)toClass.getMethod("valueOf",String.class).invoke(null,item);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
