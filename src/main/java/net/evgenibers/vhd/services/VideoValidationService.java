package net.evgenibers.vhd.services;

import net.evgenibers.vhd.daos.VideoDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Video validation service.
 *
 * @author Eugene Bokhanov
 */
@Service
public class VideoValidationService {

	private final VideoDao videoDao;

	@Autowired
	public VideoValidationService(final VideoDao videoDao) {
		this.videoDao = videoDao;
	}

	public boolean isValidFilenameAndExist(String value) {
		return isValidFilename(value)
				&& videoDao.exists(value);
	}

	public boolean isValidFilename(String value) {
		return Objects.nonNull(value)
				&& !value.isEmpty()
				&& value.length() <= 256
				&& !StringUtils.containsAny(value, "\\", "/", "*", "?", "%");
	}

	public boolean isValidContentType(String contentType) {
		return Objects.nonNull(contentType)
				&& "video/mp4".equals(contentType);
	}
}
