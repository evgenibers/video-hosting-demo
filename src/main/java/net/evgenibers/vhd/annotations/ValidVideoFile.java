package net.evgenibers.vhd.annotations;

import net.evgenibers.vhd.validators.ValidVideoFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valid video file annotation.<br>
 * Used for validation.
 *
 * @author Eugene Bokhanov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidVideoFileValidator.class)
public @interface ValidVideoFile {

	/**
	 * Message string.
	 *
	 * @return Message
	 */
	String message() default "Video file is not valid";

	/**
	 * Groups.
	 *
	 * @return Group classes
	 */
	Class<?>[] groups() default {};

	/**
	 * Payload.
	 *
	 * @return Payload classes
	 */
	Class<? extends Payload>[] payload() default {};
}
