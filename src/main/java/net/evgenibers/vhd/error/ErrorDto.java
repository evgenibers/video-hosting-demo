package net.evgenibers.vhd.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error DTO.
 *
 * @author Eugene Bokhanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
	private String message;
}
