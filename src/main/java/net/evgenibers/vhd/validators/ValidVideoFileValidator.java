package net.evgenibers.vhd.validators;

import net.evgenibers.vhd.annotations.ValidVideoFile;
import net.evgenibers.vhd.services.VideoValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Valid video file validator.
 *
 * @author Eugene Bokhanov
 */
@Component
public class ValidVideoFileValidator implements ConstraintValidator<ValidVideoFile, MultipartFile> {
	private final VideoValidationService videoValidationService;

	@Autowired
	public ValidVideoFileValidator(final VideoValidationService videoValidationService) {
		this.videoValidationService = videoValidationService;
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		return Objects.nonNull(value)
				&& !value.isEmpty()
				&& videoValidationService.isValidFilename(value.getOriginalFilename()); // TODO check file type
	}
}
