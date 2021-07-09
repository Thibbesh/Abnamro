package nl.abnamro.com.recipes.service;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.com.recipes.exception.RecipeNotFoundException;
import nl.abnamro.com.recipes.model.Recipe;
import nl.abnamro.com.recipes.model.dto.RecipeDto;
import nl.abnamro.com.recipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * RecipeServiceImpl is have business logic of recipe CRUD operations.
 * <p>Create</p>
 * <p>Update</p>
 * <p>Get</p>
 * <p>Delete</p>
 *
 */

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;

    /**
     * Constructor injection of recipeRepository
     * @param recipeRepository
     */
    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     *
     * getRecipeById will get recipe from db else throw RecipeNotFoundException
     * @param id of recipeId
     * @return recipe of entity
     * @throws RecipeNotFoundException
     */
    public Recipe getRecipeById(int id) throws RecipeNotFoundException {
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
    }

    /**
     * SaveRecipe to database
     * @param recipe of entity
     * @return recipe entity
     */
    @Override
    public Recipe saveRecipe(Recipe recipe)  {
        return recipeRepository.save(recipe);
    }

    /**
     * Delete recipe from database.
     * getRecipeById will get recipe from db else throw RecipeNotFoundException
     * @param id of recipeId
     * @throws RecipeNotFoundException
     */
    @Override
    public void deleteRecipe(int id) throws RecipeNotFoundException {
        Recipe recipe = getRecipeById(id);
        recipeRepository.delete(recipe);
    }

    /**
     * getAllRecipes from database.
     * @return list of recipe
     * @throws RecipeNotFoundException
     */
    @Override
    public ResponseEntity<?> getAllRecipes() throws RecipeNotFoundException{

        var recipeList = StreamSupport
                                                .stream(recipeRepository.findAll().spliterator(),false)
                                                .collect(Collectors.toList());
        if(recipeList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Content.");

        }
        var recipeDtoList = recipeList.stream().map(RecipeDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(recipeDtoList, HttpStatus.OK);

    }

    /**
     * Edit or update recipe object
     * getRecipeById will get recipe from db else throw RecipeNotFoundException
     * @param id of recipeId
     * @param recipe recipe transactional object
     * @return editRecipe
     */
    @Transactional
    public Recipe updateRecipe(int id, Recipe recipe) {
        Recipe editRecipe = getRecipeById(id);
        editRecipe.setName(recipe.getName());
        editRecipe.setServings(recipe.getServings());
        editRecipe.setVegetarian(recipe.getVegetarian());
        editRecipe.setCreated(recipe.getCreated());
        editRecipe.setInstructions(recipe.getInstructions());
        editRecipe.setIngredients(recipe.getIngredients());
        return editRecipe;
    }
}
