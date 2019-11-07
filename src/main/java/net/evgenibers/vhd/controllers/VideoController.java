package net.evgenibers.vhd.controllers;

import lombok.extern.log4j.Log4j2;
import net.evgenibers.vhd.domain.Video;
import net.evgenibers.vhd.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Video controller.
 *
 * @author Eugene Bokhanov
 */
@Log4j2
@Validated
@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/videos")
public class VideoController {

	private final VideoService videoService;

	@Autowired
	public VideoController(final VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/{name}")
	public ResponseEntity<ResourceRegion> getVideo(@PathVariable String name, @RequestHeader HttpHeaders headers) {
		log.info("getVideo: name = {}, range = {}", name, headers.getRange());
		return videoService.getVideo(name, headers);
	}

	@GetMapping("/{name}/full")
	public ResponseEntity<UrlResource> getFullVideo(@PathVariable String name) {
		log.info("getFullVideo: name = {}", name);
		return videoService.getFullVideo(name);
	}

	@GetMapping("")
	public List<Video> getVideos() {
		log.info("getVideos:");
		return videoService.getVideos();
	}

	@PostMapping("")
	public Video addVideo(@RequestParam("file") MultipartFile file) {
		log.info("addVideo: fileName = {}", file.getOriginalFilename());
		return videoService.addVideo(file);
	}

	@DeleteMapping("/{name}")
	public void deleteVideo(@PathVariable String name) {
		log.info("deleteVideo: name = {}", name);
		videoService.deleteVideo(name);
	}
}
