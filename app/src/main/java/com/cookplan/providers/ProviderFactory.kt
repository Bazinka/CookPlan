package com.cookplan.providers

import com.cookplan.providers.impl.*

/**
 * Created by DariaEfimova on 02.01.2018.
 */
class ProviderFactory {
    companion object {
        var companyProvider: CompanyProvider? = null//CompanyProviderImpl()
        var familyModeProvider: FamilyModeProvider = FamilyModeProviderImpl()
        //        var imageProvider: ImageProvider = ImageProviderImpl()
        var ingredientProvider: IngredientProvider = IngredientProviderImpl()
        var productProvider: ProductProvider = ProductProviderImpl()
        var recipeProvider: RecipeProvider = RecipeProviderImpl()
        var toDoListProvider: ToDoListProvider? = null//ToDoListProviderImpl()
    }
}