package com.abnamro.nl.recipes.service;

import com.abnamro.nl.recipes.exception.RecipeNotFoundException;
import com.abnamro.nl.recipes.model.entity.Recipe;
import org.springframework.http.ResponseEntity;

public interface RecipeService {

    Recipe saveRecipe(Recipe recipe);

    void deleteRecipe(int id) throws RecipeNotFoundException;

    Recipe getRecipeById(int id) throws RecipeNotFoundException;

    ResponseEntity<?> getAllRecipes();

    Recipe updateRecipe(int id, Recipe recipe);
}
