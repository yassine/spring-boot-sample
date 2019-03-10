package org.github.yassine.samples.core.utils;

import static com.google.common.base.Throwables.throwIfUnchecked;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {

  public static Object instantiate(Class<?> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throwIfUnchecked(e);
      throw new SampleRuntimeException(e);
    }
  }

  public static boolean hasNoArgConstructor(Class clazz) {
    return Arrays.stream(clazz.getDeclaredConstructors())
      .anyMatch( constructor -> constructor.getParameterCount() == 0);
  }

  public static String toByteCodeName(Type type){
    return recursiveToByteCodeName(type, true);
  }

  private static String recursiveToByteCodeName(Type type, boolean first){
    String result;
    if(type instanceof ParameterizedType){
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Class clazz = (Class) parameterizedType.getRawType();
      clazz.getName();
      result = String.format("L%s<%s>;", clazz.getName().replaceAll("\\.","/"),
        Arrays.stream(((ParameterizedType) type).getActualTypeArguments())
          .map(t -> recursiveToByteCodeName(t, false))
          .reduce("", String::concat)
      );
    }else{
      Class clazz = (Class) type;
      result = String.format("L%s;", clazz.getName().replaceAll("\\.","/"));
    }
    if(first){
      result = "Ljava/lang/Object;" + result;
    }
    return result;
  }

}
