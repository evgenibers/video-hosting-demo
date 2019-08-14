package net.evgenibers.vhd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Video DTO.
 *
 * @author Eugene Bokhanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
	private String name;
	private Long size;
	private String extension;
}
