package net.evgenibers.vhd.utils;

import java.io.File;
import java.util.Optional;

/**
 * Filename utilities.
 *
 * @author Eugene Bokhanov
 */
public class FilenameUtils {
	private FilenameUtils() {}

	/**
	 * Get file extension.
	 *
	 * @param file File
	 * @return File extension
	 */
	public static Optional<String> extension(File file) {
		return Optional.ofNullable(file)
				.map(File::getName)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(file.getName().lastIndexOf(".") + 1));
	}
}
