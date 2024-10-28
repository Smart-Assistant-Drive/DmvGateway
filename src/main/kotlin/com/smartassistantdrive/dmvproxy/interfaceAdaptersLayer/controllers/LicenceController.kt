package org.example.com.smartassistantdrive.dmvproxy.interfaceAdaptersLayer.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.example.com.smartassistantdrive.dmvproxy.businessLayer.adapter.LicenceRequestModel
import org.example.com.smartassistantdrive.dmvproxy.businessLayer.adapter.LicenceUpdateModel
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LicenceController {

	companion object {
		val objectMapper = ObjectMapper()
		private val restTemplate = RestTemplateBuilder().rootUri("http://localhost:8090")
			.build();
	}

	@GetMapping("/hello")
	fun hello(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
		return String.format("Hello %s!", name)
	}

	@CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallback_getLicence")
	@GetMapping("/licence")
	fun getLicence(@RequestParam(value = "id", defaultValue = "") licenceId: String?): HttpEntity<String> {
		val result = restTemplate.getForEntity("/licence?id=$licenceId", String::class.java)
		return result
	}

	fun fallback_getLicence(licenceId: String?, e: Exception): HttpEntity<String> {
		e.printStackTrace()
		return ResponseEntity("The system is currently unavailable", HttpStatus.OK)
	}

	@CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallback_newLicence")
	@PostMapping("/licences")
	fun newLicence(@RequestBody licence: LicenceRequestModel): HttpEntity<String> {
		val result = restTemplate.postForEntity("/licences", licence, String::class.java)
		return result
	}

	fun fallback_newLicence(licence: LicenceRequestModel,  e: Exception): HttpEntity<String> {
		e.printStackTrace()
		return ResponseEntity("The system is currently unavailable", HttpStatus.OK)
	}

	@CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallback_updateLicence")
	@PutMapping("/licences/{id}")
	fun updateLicence(
		@RequestBody licence: LicenceUpdateModel,
		@PathVariable id: String,
	): HttpEntity<String> {
		val json = objectMapper.writeValueAsString(licence)

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		val entity = HttpEntity<String>(json, headers)

		val result = restTemplate.exchange("/licences/$id", HttpMethod.PUT, entity, String::class.java)
		return result
	}

	fun fallback_updateLicence(licence: LicenceUpdateModel, id: String, e: Exception): HttpEntity<String> {
		e.printStackTrace()
		return ResponseEntity("The system is currently unavailable", HttpStatus.OK)
	}
}
