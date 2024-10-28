package org.example.com.smartassistantdrive.dmvproxy.businessLayer.adapter

import java.time.LocalDate

private const val NA = "NA"

data class LicenceUpdateModel(
	val expireDate: String = NA,
	val releaseDate: String = NA,
	val residence: String = NA,
)
