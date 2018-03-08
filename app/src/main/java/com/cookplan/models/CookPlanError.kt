package com.cookplan.models

import com.google.firebase.database.DatabaseError

/**
 * Created by DariaEfimova on 24.04.17.
 */

data class CookPlanError(private val errorMessage: String? = null, val code: Int = 0) : Exception(errorMessage) {

    constructor(databaseError: DatabaseError) : this(databaseError.message, databaseError.code)
}