package net.evgenibers.vhd.writers;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ResourceRegionEncoder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Resource region message writer.
 *
 * @author Eugene Bokhanov
 */
public class ResourceRegionMessageWriter implements HttpMessageWriter<ResourceRegion> {

	private ResolvableType REGION_TYPE = ResolvableType.forClass(ResourceRegion.class);

	private ResourceRegionEncoder regionEncoder = new ResourceRegionEncoder();

	private List<MediaType> mediaTypes = MediaType.asMediaTypes(regionEncoder.getEncodableMimeTypes());

	@Override
	public boolean canWrite(ResolvableType elementType, MediaType mediaType) {
		return regionEncoder.canEncode(elementType, mediaType);
	}

	@Override
	public List<MediaType> getWritableMediaTypes() {
		return mediaTypes;
	}

	@Override
	public Mono<Void> write(Publisher<? extends ResourceRegion> inputStream, ResolvableType resolvableType,
			MediaType mediaType, ReactiveHttpOutputMessage reactiveHttpOutputMessage, Map<String, Object> map) {
		return null; // TODO impl
	}

	@Override
	public Mono<Void> write(Publisher<? extends ResourceRegion> inputStream, ResolvableType actualType,
			ResolvableType elementType, MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response,
			Map<String, Object> hints) {
		HttpHeaders headers = response.getHeaders();
		headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");

		return Mono.from(inputStream).flatMap(resourceRegion -> {
			response.setStatusCode(HttpStatus.PARTIAL_CONTENT);
			MediaType resourceMediaType = getResourceMediaType(mediaType, resourceRegion.getResource());
			headers.setContentType(resourceMediaType);
			try {
				long contentLength = resourceRegion.getResource().contentLength();
				long start = resourceRegion.getPosition();
				long end = Math.min(start + resourceRegion.getCount() - 1, contentLength - 1);
				headers.add("Content-Range", "bytes " + start + "-" + end + "/" + contentLength);
				headers.setContentLength(end - start + 1);
			} catch (IOException e) {
				// TODO handle
			}

			return zeroCopy(resourceRegion.getResource(), resourceRegion, response)
					.orElseGet(() -> {
							Mono input = Mono.just(resourceRegion);
							Flux body = this.regionEncoder.encode(input, response.bufferFactory(),
									REGION_TYPE, resourceMediaType, Collections.emptyMap());
							return response.writeWith(body);
					});
		});
	}

	private MediaType getResourceMediaType(MediaType mediaType, Resource resource) {
		if(Objects.nonNull(mediaType) && mediaType.isConcrete()
				&& !mediaType.equals(MediaType.APPLICATION_OCTET_STREAM)) {
			return mediaType;
		} else {
			return MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
		}
	}

	private Optional<Mono<Void>> zeroCopy(Resource resource, ResourceRegion resourceRegion,
			ReactiveHttpOutputMessage message) {
		if (message instanceof ZeroCopyHttpOutputMessage && resource.isFile()) {
			try {
				File file = resource.getFile();
				long position = resourceRegion.getPosition();
				long count = resourceRegion.getCount();
				return Optional.of(((ZeroCopyHttpOutputMessage) message).writeWith(file, position, count));
			} catch (IOException e) {
				// TODO handle
			}
		}
		return Optional.empty();
	}
}
