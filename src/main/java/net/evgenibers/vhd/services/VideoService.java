package net.evgenibers.vhd.services;

import lombok.extern.log4j.Log4j2;
import net.evgenibers.vhd.annotations.ValidVideoFile;
import net.evgenibers.vhd.annotations.ValidVideoFileName;
import net.evgenibers.vhd.daos.VideoDao;
import net.evgenibers.vhd.domain.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Video service.
 *
 * @author Eugene Bokhanov
 */
@Log4j2
@Service
@Validated
public class VideoService {

	@Value("${video.chunk.size:1000000}")
	private long chunkSize;

	private final VideoDao videoDao;

	@Autowired
	public VideoService(final VideoDao videoDao) {
		this.videoDao = videoDao;
	}

	public ResponseEntity<UrlResource> getFullVideo(@ValidVideoFileName String name) {
		log.debug("getFullVideo: name = {}", name);
		try {
			UrlResource video = videoDao.findResource(name);
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
					.body(video);
		} catch (MalformedURLException mue) {
			throw new RuntimeException("File not found");
		}
	}

	public ResponseEntity<ResourceRegion> getVideo(@ValidVideoFileName String name, HttpHeaders headers) {
		log.debug("getVideo: name = {}", name);
		try {
			UrlResource video = videoDao.findResource(name);
			ResourceRegion region = resourceRegion(video, headers);
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
					.body(region);
		} catch (MalformedURLException mue) {
			throw new RuntimeException("File not found");
		} catch (IOException ioe) {
			throw new RuntimeException("Can't read file");
		}
	}

	private ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) throws IOException {
		long contentLength = video.contentLength();
		HttpRange range = headers.getRange().isEmpty() ? null : headers.getRange().get(0);
		if (range != null) {
			long start = range.getRangeStart(contentLength);
			long end = range.getRangeEnd(contentLength);
			long rangeLength = Math.min(chunkSize, end - start + 1);
			return new ResourceRegion(video, start, rangeLength);
		} else {
			long rangeLength = Math.min(chunkSize, contentLength);
			return new ResourceRegion(video, 0, rangeLength);
		}
	}

	public List<Video> getVideos() {
		log.debug("getVideos:");
		return videoDao.find();
	}

	public Video addVideo(@ValidVideoFile MultipartFile file) {
		log.debug("addVideo: fileName = {}", file.getName());
		return videoDao.add(file);
	}

	public void deleteVideo(@ValidVideoFileName String name) {
		log.debug("deleteVideo: name = {}", name);
		videoDao.delete(name);
	}
}
