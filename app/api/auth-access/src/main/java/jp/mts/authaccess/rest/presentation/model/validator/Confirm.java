package jp.mts.authaccess.rest.presentation.model.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

@Documented
@Constraint(validatedBy = { Confirm.Validator.class })
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Confirm {

	String message() default "{jp.mts.authaccess.rest.presentation.model.validator.Confirm}";
	String field();
	String confirmField();
	
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};	
	
	public static class Validator implements ConstraintValidator<Confirm, Object>{

		private Confirm annotation;

		@Override
		public void initialize(Confirm constraintAnnotation) {
			this.annotation = constraintAnnotation;
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			boolean matched = ObjectUtils.nullSafeEquals(
					getFieldValue(value, annotation.field()), 
					getFieldValue(value, annotation.confirmField()));
			if (matched) return true;
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(annotation.message())
				.addPropertyNode(annotation.confirmField())
				.addConstraintViolation();
			
			return false;
		}
		
		private Object getFieldValue(Object obj, String fieldName) {
			try {
				Field field = obj.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				return field.get(obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}
}
