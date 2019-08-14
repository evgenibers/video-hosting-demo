package net.evgenibers.vhd.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception controller adviser.
 *
 * @author Eugene Bokhanov
 */
@Log4j2
@SuppressWarnings("unused")
@ControllerAdvice
public class ExceptionAdviser {
	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	public ErrorDto runtimeExceptionHandler(HttpServletResponse response, RuntimeException ex) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return new ErrorDto(ex.getMessage());
	}
}
