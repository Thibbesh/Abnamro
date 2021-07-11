package com.abnamro.nl.recipes.controller;

import com.abnamro.nl.recipes.model.entity.Recipe;
import com.abnamro.nl.recipes.model.dto.RecipeDto;
import com.abnamro.nl.recipes.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * RecipeController is REST API and have below endpoints
 * <p>Create</p> to create Recipe
 * <p>Update</p> to edit or update recipe
 * <p>Delete</p> to delete recipe
 * <p>Get</p> to get all available recipes
 *
 */
@RestController
@RequestMapping(path = "/v1/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Construction injection bean creation.
     * @param recipeService bean
     */
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * getRecipeById endpoint is to get the recipe by id.
     * logged in user must have Role of ADMIN or USER to perform
     *
     * @param id of recipe
     * @return RecipeDto
     *
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable("id") @NotNull final int id) {
        return new ResponseEntity<>(RecipeDto.from(recipeService.getRecipeById(id)), HttpStatus.OK);
    }

    /**
     * getAllRecipe end point to get all recipes from database.
     * if list is empty it will send Http status is 404 and message.
     *
     * @return list<RecipeDto>
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAllRecipe() {
        return recipeService.getAllRecipes();
    }

    /**
     * saveRecipe endpoint is to save the recipe entity in database.
     *
     * @param recipeDto of recipe
     * @return String message for recipe saved successfully.
     */
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> saveRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        recipeService.saveRecipe(Recipe.from(recipeDto));
        return ResponseEntity.status(HttpStatus.CREATED).body("Recipe saved successfully..");
    }

    /**
     * update endpoint is to update/edit the existing recipe object.
     *
     * @param recipeDto recipeDto from frontend
     * @param id recipeId
     * @return recipeDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<RecipeDto> update(@Valid @RequestBody RecipeDto recipeDto, @PathVariable("id") int id) {
        return new ResponseEntity<>(RecipeDto.from(recipeService.updateRecipe(id, Recipe.from(recipeDto))), HttpStatus.OK);
    }

    /**
     * deleteRecipeById to delete existing recipe entity.
     * @param id of recipe
     * @return successMessage of string
     *
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> deleteRecipeById(@PathVariable("id") int id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.OK).body("Recipe deleted successfully..");
    }
}
