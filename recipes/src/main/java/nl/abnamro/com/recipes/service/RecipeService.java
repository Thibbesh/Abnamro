package nl.abnamro.com.recipes.service;

import nl.abnamro.com.recipes.exception.RecipeNotFoundException;
import nl.abnamro.com.recipes.model.Recipe;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RecipeService {

    Recipe saveRecipe(Recipe recipe);

    void deleteRecipe(int id) throws RecipeNotFoundException;

    Recipe getRecipeById(int id) throws RecipeNotFoundException;

    ResponseEntity<?> getAllRecipes();

    Recipe updateRecipe(int id, Recipe recipe);
}
