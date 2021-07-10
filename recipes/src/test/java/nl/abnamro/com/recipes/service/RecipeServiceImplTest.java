package nl.abnamro.com.recipes.service;

import nl.abnamro.com.recipes.exception.RecipeNotFoundException;
import nl.abnamro.com.recipes.model.entity.Recipe;
import nl.abnamro.com.recipes.model.dto.RecipeDto;
import nl.abnamro.com.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class RecipeServiceImplTest {

    @InjectMocks
    private RecipeServiceImpl recipeServiceImpl;

    @Mock
    private RecipeRepository recipeRepository;

    @Test
    public void givenRecipeId_whenGetRecipeById_thenReturnRecipe() {
        int recipeId=2;
        //When called recipeService.saveOrUpdate
        Recipe recipe = new Recipe();
        recipe.setId(2);
        recipe.setName("Kabab");
        recipe.setVegetarian(true);
        recipe.setServings(5);
        when(recipeRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(recipe));

        //Then return StatusCode as 200 and Success message..
        Recipe recipeFromDb= recipeServiceImpl.getRecipeById(recipeId);
        verify(recipeRepository, times(1)).findById(anyInt());
        assertEquals("RecipeDto Id: ", recipeFromDb.getId(), recipe.getId());
        assertEquals("Name of Recipe: ", recipeFromDb.getName(), recipe.getName());
        assertTrue("Indicator if the dish is vegetarian: ", recipeFromDb.getVegetarian());
        assertEquals("Number of people the dish is suitable for: ", recipeFromDb.getServings(), recipe.getServings());

    }

    @Test()
    public void givenRecipeId_whenGetRecipeById_thenThrowRecipeNotFoundException() {
        int recipeId=2;
        //When called recipeService.saveOrUpdate
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(any(Integer.class))).thenReturn(recipeOptional);

        Assertions.assertThrows(RecipeNotFoundException.class, () -> {
            recipeServiceImpl.getRecipeById(recipeId);
        });
    }

    @Test
    public void givenRecipe_whenSaveRecipe_thenReturnRecipe() {
        //When called recipeService.saveOrUpdate
        Recipe recipe = new Recipe();
        recipe.setId(2);
        recipe.setName("Kabab");
        recipe.setVegetarian(true);
        recipe.setServings(5);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        //Then return StatusCode as 200 and Success message..
        Recipe recipeFromDb= recipeServiceImpl.saveRecipe(recipe);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        assertEquals("RecipeDto Id: ", recipeFromDb.getId(), recipe.getId());
        assertEquals("Name of Recipe: ", recipeFromDb.getName(), recipe.getName());
        assertTrue("Indicator if the dish is vegetarian: ", recipeFromDb.getVegetarian());
        assertEquals("Number of people the dish is suitable for: ", recipeFromDb.getServings(), recipe.getServings());

    }

    @Test
    public void givenRecipe_whenDeleteRecipe_thenDeleteRecipeFromDatabase() {
        //When called recipeService.saveOrUpdate
        int recipeId=2;
        Recipe recipe = new Recipe();
        recipe.setId(2);
        recipe.setName("Kabab");
        recipe.setVegetarian(true);
        recipe.setServings(5);
        when(recipeRepository.findById(any(Integer.class))).thenReturn(Optional.of(recipe));

        //Then return StatusCode as 200 and Success message..
        recipeServiceImpl.deleteRecipe(recipeId);
        verify(recipeRepository, times(1)).delete(any(Recipe.class));
        verify(recipeRepository, times(1)).findById(anyInt());
    }

    @Test
    public void givenRecipe_whenDeleteRecipe_thenThrowRecipeNotFoundException() {
        int recipeId=2;
        //When called recipeService.saveOrUpdate
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(any(Integer.class))).thenReturn(recipeOptional);

        Assertions.assertThrows(RecipeNotFoundException.class, () -> {
            recipeServiceImpl.deleteRecipe(recipeId);
            verify(recipeRepository, times(1)).delete(any(Recipe.class));
            verify(recipeRepository, times(1)).findById(anyInt());
        });

    }

    @Test
    public void getAllRecipes_whenGetAllRecipes_thenReturnListOfRecipe() {
        //when
        when(recipeRepository.findAll()).thenReturn(getRecipeList());

        //Then return list of recipes
        ResponseEntity<?> recipeReponseEntity = recipeServiceImpl.getAllRecipes();
        List<RecipeDto> recipeDtoList = (List<RecipeDto>) recipeReponseEntity.getBody();
        verify(recipeRepository, times(1)).findAll();
        assertEquals("No of recipe's ",recipeDtoList.size(), 3);


    }

    //Test scenario data.
    private List<Recipe> getRecipeList() {
        List<Recipe> recipeList = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.setId(1);
        recipe1.setName("Donor Kebab");
        recipe1.setVegetarian(false);
        recipe1.setServings(5);

        Recipe recipe2 = new Recipe();
        recipe2.setId(2);
        recipe2.setName("Chicken Kebab");
        recipe2.setVegetarian(false);
        recipe2.setServings(2);

        Recipe recipe3 = new Recipe();
        recipe3.setId(3);
        recipe3.setName("French Fries");
        recipe3.setVegetarian(true);
        recipe3.setServings(3);

        recipeList.add(recipe1);
        recipeList.add(recipe2);
        recipeList.add(recipe3);
        return recipeList;
    }

    @Test
    public void givenRecipeAndID_whenUpdateRecipe_thenUpdatedRecipe() {
        //given
        int recipeId = 3;
        Recipe recipe = new Recipe();
        recipe.setId(3);
        recipe.setName("French Fries");
        recipe.setVegetarian(true);
        recipe.setServings(3);
        //when
        when(recipeRepository.findById(any(Integer.class))).thenReturn(Optional.of(recipe));

        //Then return updatedRecipe
        Recipe updateRecipe = recipeServiceImpl.updateRecipe(recipeId, recipe);
        verify(recipeRepository, times(1)).findById(anyInt());
        assertEquals("Updated Recipe Id ",updateRecipe.getId(), 3);

    }

    @Test
    public void givenRecipeAndID_NotFoundInDB_whenUpdateRecipe_thenThrowRecipeNotFoundException() {
        //given
        int recipeId = 1;
        Recipe recipe = new Recipe();
        recipe.setId(3);
        recipe.setName("French Fries");
        recipe.setVegetarian(true);
        recipe.setServings(3);


        //When called recipeService.saveOrUpdate
        Optional<Recipe> recipeOptional = Optional.empty();
        when(recipeRepository.findById(any(Integer.class))).thenReturn(recipeOptional);

        //then
        Assertions.assertThrows(RecipeNotFoundException.class, () -> {
            recipeServiceImpl.updateRecipe(recipeId, recipe);
            verify(recipeRepository, times(1)).findById(anyInt());
        });

    }
}
