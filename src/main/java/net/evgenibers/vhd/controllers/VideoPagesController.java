package net.evgenibers.vhd.controllers;

import lombok.extern.log4j.Log4j2;
import net.evgenibers.vhd.annotations.ValidVideoFile;
import net.evgenibers.vhd.annotations.ValidVideoFileName;
import net.evgenibers.vhd.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Video pages controller.
 *
 * @author Eugene Bokhanov
 */
@Log4j2
@Validated
@Controller
@SuppressWarnings("unused")
@RequestMapping("")
public class VideoPagesController {

	private final VideoService videoService;

	@Autowired
	public VideoPagesController(final VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("")
	public String getVideos(Model model) {
		log.info("getVideos:");
		model.addAttribute("videos", videoService.getVideos());
		return "videos";
	}

	@GetMapping("/{name}")
	public String getVideo(@PathVariable @ValidVideoFileName String name, Model model) {
		log.info("getVideo: name = {}", name);
		model.addAttribute("videoName", name);
		return "video";
	}

	@PostMapping("")
	public String addVideo(@RequestParam("file") @ValidVideoFile MultipartFile file) {
		log.info("addVideo: fileName = {}", file.getOriginalFilename());
		videoService.addVideo(file);
		return "redirect:/";
	}
}
