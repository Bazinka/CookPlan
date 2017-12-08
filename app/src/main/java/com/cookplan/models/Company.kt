package com.cookplan.models

import org.joda.time.LocalDateTime

import java.io.Serializable

/**
 * Created by DariaEfimova on 17.10.16.
 */

data class Company(val userId: String, val name: String, val comment: String? = null, val latitude: Double = 0.toDouble(), val longitude: Double = 0.toDouble(), val addedToGeoFence: Boolean = false) : Serializable {

    var id: String? = null

    private val jodaDateCreated: LocalDateTime = LocalDateTime.now()
    var dateCreatedMillisek: Long = jodaDateCreated.millisOfSecond.toLong()

    var isAddedToGeoFence: Boolean = false

    var photoList: List<String>? = null
}
