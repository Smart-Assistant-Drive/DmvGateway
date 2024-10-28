package org.example.com.smartassistantdrive.dmvproxy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DmvgatewayApplication

fun main(args: Array<String>) {
	runApplication<DmvgatewayApplication>(*args)
}