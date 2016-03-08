package jp.mts.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class AspectUtils {

	public static String paramValueOf(
			ProceedingJoinPoint pjp, 
			Class<? extends Annotation> annotationType) {
		
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		Annotation[][] paramsAnnotations = method.getParameterAnnotations();

		int paramIndex = -1;
		for(int i=0; i < paramsAnnotations.length; i++) {
			for(Annotation anno : paramsAnnotations[i]) {
				if (anno.annotationType().equals(annotationType)) {
					paramIndex = i;
				}
			};
		};
		return paramIndex >= 0 ? (String)pjp.getArgs()[paramIndex] : null;
	}
	
}
