package com.cookplan.models

import java.io.Serializable

/**
 * Created by DariaEfimova on 12.04.17.
 */

data class ToDoItem(var id: String? = null,
                    var userId: String? = null,
                    var name: String? = null,
                    var comment: String? = null,
                    var companyId: String? = null,
                    var categoryId: String? = null,
                    var category: ToDoCategory? = null,
                    var toDoStatus: ToDoItemStatus? = ToDoItemStatus.NEED_TO_DO) : Serializable {

    constructor(user: String,
                name: String,
                comment: String? = null,
                categoryId: String? = null) : this(userId = user,
            name = name,
            comment = comment,
            categoryId = categoryId)
}
