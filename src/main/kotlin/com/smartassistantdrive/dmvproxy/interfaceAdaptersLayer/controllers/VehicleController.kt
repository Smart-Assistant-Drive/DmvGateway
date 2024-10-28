package org.example.com.smartassistantdrive.dmvproxy.interfaceAdaptersLayer.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.com.smartassistantdrive.dmvproxy.businessLayer.adapter.VehicleRequestModel
import org.example.com.smartassistantdrive.dmvproxy.businessLayer.adapter.VehicleUpdateModel
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class VehicleController {

	companion object {
		val objectMapper = ObjectMapper()
		private val restTemplate = RestTemplateBuilder().rootUri("http://localhost:8090")
			.build();
	}

	@PostMapping("/vehicles")
	fun addCar(@RequestBody vehicleRequestModel: VehicleRequestModel): HttpEntity<String> {
		val result = restTemplate.postForEntity("/addCar", vehicleRequestModel, String::class.java)
		return result
	}

	@GetMapping("/carByVin")
	fun getCarByVin(@RequestParam(value = "vin", defaultValue = "") vin: String?): HttpEntity<String> {
		val result = restTemplate.getForEntity("/carByVin?vin=$vin", String::class.java)
		return result
	}

	@GetMapping("/carByPlate")
	fun getCarByPlate(@RequestParam(value = "plate", defaultValue = "") plate: String?): HttpEntity<String> {
		val result = restTemplate.getForEntity("/carByPlate?plate=$plate", String::class.java)
		return result
	}

	@PutMapping("/cars/{vin}")
	fun updateCar(
		@RequestBody car: VehicleUpdateModel,
		@PathVariable vin: String,
	): HttpEntity<String> {
		val json = objectMapper.writeValueAsString(car)

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		val entity = HttpEntity<String>(json, headers)

		val result = restTemplate.exchange("/cars/$vin", HttpMethod.PUT, entity, String::class.java)
		return result
	}
}