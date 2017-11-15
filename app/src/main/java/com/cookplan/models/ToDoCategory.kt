package com.cookplan.models

import java.io.Serializable

/**
 * Created by DariaEfimova on 12.04.17.
 */

data class ToDoCategory(var id: String? = null,
                        var userId: String? = null,
                        var name: String? = null,
                        var color: ToDoCategoryColor = ToDoCategoryColor.BLACK) : Serializable {


    constructor(userId: String, name: String, color: ToDoCategoryColor) : this(id = null, userId = userId,
            name = name, color = color)
}