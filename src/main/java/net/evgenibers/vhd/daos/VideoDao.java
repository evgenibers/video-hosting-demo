package net.evgenibers.vhd.daos;

import net.evgenibers.vhd.domain.Video;
import net.evgenibers.vhd.utils.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Video DAO.
 *
 * @author Eugene Bokhanov
 */
@Service
public class VideoDao { // TODO use paths instead of string concat

	@Value("${video.location}")
	private String videoLocation;

	public UrlResource findResource(String name) throws MalformedURLException {
		return new UrlResource("file:" + videoLocation + "/" + name);
	}

	public void delete(String name) {
		new File(videoLocation + "/" + name).delete();
	}

	public List<Video> find() {
		File[] files = Optional.ofNullable(new File(videoLocation).listFiles())
				.orElseThrow(() -> new RuntimeException("Can't find files"));
		return Arrays.stream(files)
				.map(f -> new Video(f.getName(), f.length(), FilenameUtils.extension(f).orElse("unknown")))
				.collect(Collectors.toList());
	}

	public Optional<Video> find(String name) {
		File file = new File(videoLocation + "/" + name);
		if (!file.isFile()) {
			return Optional.empty();
		}
		return Optional.of(new Video(file.getName(), file.length(), FilenameUtils.extension(file)
				.orElse("unknown")));
	}

	public Video add(MultipartFile file) {
		File discFile = new File(videoLocation + "/" + file.getOriginalFilename());
		try {
			Files.copy(file.getInputStream(), discFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new RuntimeException("Can't save file");
		}
		discFile.setReadable(true, false);
		discFile.setExecutable(true, false);
		discFile.setWritable(true, false);
		return find(file.getOriginalFilename())
				.orElseThrow(() -> new RuntimeException("File not found"));
	}

	public boolean exists(String name) {
		return new File(videoLocation + "/" + name).isFile();
	}
}
