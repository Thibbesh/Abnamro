package nl.abnamro.com.recipes.service;

import nl.abnamro.com.recipes.exception.RecipeNotFoundException;
import nl.abnamro.com.recipes.model.entity.Recipe;
import org.springframework.http.ResponseEntity;

public interface RecipeService {

    Recipe saveRecipe(Recipe recipe);

    void deleteRecipe(int id) throws RecipeNotFoundException;

    Recipe getRecipeById(int id) throws RecipeNotFoundException;

    ResponseEntity<?> getAllRecipes();

    Recipe updateRecipe(int id, Recipe recipe);
}
