package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RequireValidApiKey
@RestController
@RequestMapping("/bundle")
public class BundleController {
	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private BundleDtoMapperImpl mapper;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<BundleMetadataDto> getAll() {
		return bundleDao.getAll().stream().map(mapper::toDto).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody BundleMetadataDto bundle) {
		bundleDao.save(mapper.fromDto(bundle));
	}

	@RequestMapping(value = "{uuid}", method = RequestMethod.GET)
	public BundleMetadataDto get(@RequestParam("uuid") String uuid) {
		return mapper.toDto(bundleDao.get(uuid));
	}

	@RequestMapping(value = "{uuid}", method = RequestMethod.PUT)
	public ResponseEntity put(@RequestParam("uuid") String uuid, @RequestBody BundleMetadataDto bundleMetadataDto) {
		bundleDao.save(mapper.fromDto(bundleMetadataDto));
		if (uuid == null || uuid.isEmpty()) {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(
					ServletUriComponentsBuilder
							.fromCurrentRequest()
							.path("/bundles/{uuid}")
							.buildAndExpand(bundleMetadataDto.getUuid()).toUri()
			);
			return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
		} else {
			return new ResponseEntity(HttpStatus.NO_CONTENT);

		}
	}
}
