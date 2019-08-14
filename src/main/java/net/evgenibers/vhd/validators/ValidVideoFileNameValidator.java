package net.evgenibers.vhd.validators;

import net.evgenibers.vhd.annotations.ValidVideoFileName;
import net.evgenibers.vhd.services.VideoValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Valid video file name validator.
 *
 * @author Eugene Bokhanov
 */
@Component
public class ValidVideoFileNameValidator implements ConstraintValidator<ValidVideoFileName, String> {
	private final VideoValidationService videoValidationService;

	@Autowired
	public ValidVideoFileNameValidator(final VideoValidationService videoValidationService) {
		this.videoValidationService = videoValidationService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return videoValidationService.isValidFilenameAndExist(value);
	}
}
