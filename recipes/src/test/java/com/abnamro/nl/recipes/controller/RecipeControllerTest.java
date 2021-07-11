package com.abnamro.nl.recipes.controller;

import com.abnamro.nl.recipes.model.dto.IngredientDto;
import com.abnamro.nl.recipes.model.dto.RecipeDto;
import com.abnamro.nl.recipes.model.entity.Recipe;
import com.abnamro.nl.recipes.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void givenRecipeDto_whenSaveRecipe_thenStatus200WithSuccessMessage() throws Exception {
        //Given recipeDto from postman or frontend
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Chicken");
        recipeDto.setVegetarian(true);
        recipeDto.setServings(4);

        //When saveRecipe
        Recipe recipe = new Recipe();
        recipe.setName("Chicken Biriyani");
        recipe.setVegetarian(true);
        recipe.setServings(4);

        when(recipeService.saveRecipe(any(Recipe.class))).thenReturn(recipe);
        ObjectMapper objectMapper = mapperBuilder.build();
        //Then Controller should return Status is ok and Success message
        mockMvc.perform(post("/v1/api/recipe")
                .content(objectMapper.writeValueAsString(recipeDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Recipe saved successfully..")));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void shouldReturn200WhenSendingRequestToControllerWithRoleUser() throws Exception {
        mockMvc.perform(get("/v1/api/recipe")).andExpect(status().isOk());
    }

    @Test
    public void shouldReturn403WhenSendingRequestToControllerWithoutRoleUser() throws Exception {
        mockMvc.perform(get("/v1/api/recipe")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void shouldReturn404WhenSendingRequestToControllerWithoutRoleUser() throws Exception {
        mockMvc.perform(get("/recipe")).andExpect(status().isNotFound());
    }


    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void givenRecipeId_when_GetRecipeById_thenRecipeDto_WithStatus200WithSuccessMessage() throws Exception {
        //Given recipeDto from postman or frontend
        // Given recipeId as input from postman
        int recipeId = 2;

        //When called recipeService.saveOrUpdate
        Recipe recipe = new Recipe();
        recipe.setId(2);
        recipe.setName("Kabab");
        recipe.setVegetarian(true);
        recipe.setServings(5);
        when(recipeService.getRecipeById(any(Integer.class))).thenReturn(recipe);

        ObjectMapper objectMapper = mapperBuilder.build();
        //Then Controller should return Status is ok and Success message
        mockMvc.perform(get("/v1/api/recipe/2")
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.vegetarian").value(recipe.getVegetarian()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()));

    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void when_GetAllRecipe_thenReturnListRecipeDto_WithStatus200WithSuccessMessage() throws Exception {
        //Given recipeDto from postman or frontend
        // Given recipeId as input from postman
        ResponseEntity responseEntity = new ResponseEntity<>(getRecipeDtoList(), HttpStatus.OK);
        when(recipeService.getAllRecipes()).thenReturn(responseEntity);
        ObjectMapper objectMapper = mapperBuilder.build();

        //Then Controller should return Status is ok and Success message
        mockMvc.perform(get("/v1/api/recipe")
                .content(objectMapper.writeValueAsString(getRecipeList()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":0,\"name\":\"Donor Kebab\",\"servings\":5,\"created\":null,\"vegetarian\":false,\"instructions\":\"1. Turn on the gas,2. Chop chicken n onion3. spoons of oil\",\"ingredients\":[{\"ingredientId\":0,\"name\":\"Chicken Kebab\",\"amount\":\"2\"},{\"ingredientId\":0,\"name\":\"Oil\",\"amount\":\"2 spoons\"}]},{\"id\":0,\"name\":\"Chicken Kebab\",\"servings\":5,\"created\":null,\"vegetarian\":false,\"instructions\":\"1. Turn on the gas,2. Chop chicken n onion3. spoons of oil\",\"ingredients\":[{\"ingredientId\":0,\"name\":\"Chicken\",\"amount\":\"2\"},{\"ingredientId\":0,\"name\":\"Oil\",\"amount\":\"2 spoons\"}]},{\"id\":0,\"name\":\"French Fries\",\"servings\":5,\"created\":null,\"vegetarian\":true,\"instructions\":\"1. Turn on the gas,2. Chop chicken n onion3. spoons of oil\",\"ingredients\":[{\"ingredientId\":0,\"name\":\"Chicken\",\"amount\":\"2\"},{\"ingredientId\":0,\"name\":\"Oil\",\"amount\":\"2 spoons\"}]}]"));

    }

    //Test scenario data.
    private List<RecipeDto> getRecipeDtoList() {
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        RecipeDto recipeDto1 = new RecipeDto();
        recipeDto1.setName("Donor Kebab");
        recipeDto1.setVegetarian(false);
        recipeDto1.setServings(5);
        recipeDto1.setInstructions("1. Turn on the gas," +
                "2. Chop chicken n onion" +
                "3. spoons of oil");

        List<IngredientDto> ingredientDtoList = new ArrayList<>();
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setAmount("2");
        ingredientDto1.setName("Chicken Kebab");
        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setAmount("2 spoons");
        ingredientDto2.setName("Oil");
        ingredientDtoList.add(ingredientDto1);
        ingredientDtoList.add(ingredientDto2);
        recipeDto1.setIngredients(ingredientDtoList);

        RecipeDto recipeDto2 = new RecipeDto();
        recipeDto2.setName("Chicken Kebab");
        recipeDto2.setVegetarian(false);
        recipeDto2.setServings(5);
        recipeDto2.setInstructions("1. Turn on the gas," +
                "2. Chop chicken n onion" +
                "3. spoons of oil");

        List<IngredientDto> ingredientDtoList2 = new ArrayList<>();
        IngredientDto ingredientDto3 = new IngredientDto();
        ingredientDto3.setAmount("2");
        ingredientDto3.setName("Chicken");
        IngredientDto ingredientDto4 = new IngredientDto();
        ingredientDto4.setAmount("2 spoons");
        ingredientDto4.setName("Oil");
        ingredientDtoList2.add(ingredientDto3);
        ingredientDtoList2.add(ingredientDto4);
        recipeDto2.setIngredients(ingredientDtoList2);

        RecipeDto recipeDto3 = new RecipeDto();
        recipeDto3.setName("French Fries");
        recipeDto3.setVegetarian(true);
        recipeDto3.setServings(5);
        recipeDto3.setInstructions("1. Turn on the gas," +
                "2. Chop chicken n onion" +
                "3. spoons of oil");

        List<IngredientDto> ingredientDtoList3 = new ArrayList<>();
        IngredientDto ingredientDto5 = new IngredientDto();
        ingredientDto5.setAmount("2");
        ingredientDto5.setName("Chicken");
        IngredientDto ingredientDto6 = new IngredientDto();
        ingredientDto6.setAmount("2 spoons");
        ingredientDto6.setName("Oil");
        ingredientDtoList3.add(ingredientDto5);
        ingredientDtoList3.add(ingredientDto6);
        recipeDto3.setIngredients(ingredientDtoList3);

        recipeDtoList.add(recipeDto1);
        recipeDtoList.add(recipeDto2);
        recipeDtoList.add(recipeDto3);
        return recipeDtoList;
    }

    //Test scenario data.
    private List<Recipe> getRecipeList() {
        List<Recipe> recipeList = new ArrayList<>();
        Clock clock = Clock.fixed(Instant.parse("2021-05-24T10:15:30.00Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);
        Recipe recipe1 = new Recipe();
        recipe1.setId(1);
        recipe1.setName("Donor Kebab");
        recipe1.setCreated(dateTime);
        recipe1.setVegetarian(false);
        recipe1.setServings(5);

        Recipe recipe2 = new Recipe();
        recipe2.setId(2);
        recipe2.setName("Chicken Kebab");
        recipe2.setCreated(dateTime);
        recipe2.setVegetarian(false);
        recipe2.setServings(2);

        Recipe recipe3 = new Recipe();
        recipe3.setId(3);
        recipe3.setName("French Fries");
        recipe3.setCreated(dateTime);
        recipe3.setVegetarian(true);
        recipe3.setServings(3);

        recipeList.add(recipe1);
        recipeList.add(recipe2);
        recipeList.add(recipe3);
        return recipeList;
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void givenRecipeId_RecipeDto_when_update_thenRecipeDto_WithStatus200WithSuccessMessage() throws Exception {
        //Given recipeDto from postman or frontend
        // Given recipeId as input from postman
        int recipeId = 1;
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1);
        recipeDto.setName("Donor Kebab");
        recipeDto.setVegetarian(false);
        recipeDto.setServings(5);

        //When called recipeService.saveOrUpdate
        Recipe recipe = new Recipe();
        recipe.setName("French Fries");
        recipe.setVegetarian(true);
        recipe.setServings(4);
        when(recipeService.updateRecipe((any(Integer.class)), any(Recipe.class))).thenReturn(recipe);

        ObjectMapper objectMapper = mapperBuilder.build();
        //Then Controller should return Status is ok and Success message
        mockMvc.perform(put("/v1/api/recipe/1")
                .content(objectMapper.writeValueAsString(recipeDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.vegetarian").value(recipe.getVegetarian()))
                .andExpect(jsonPath("$.servings").value(recipe.getServings()));

    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    public void givenRecipeId_whenDeleteRecipeById_thenStatus200WithSuccessMessage() throws Exception {
        //Given recipeDto from postman or frontend
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1);
        recipeDto.setName("Chicken");
        recipeDto.setVegetarian(true);
        recipeDto.setServings(4);



        //Then Controller should return Status is ok and Success message
        mockMvc.perform(delete("/v1/api/recipe/1")
                //.content(objectMapper.writeValueAsString(recipeDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Recipe deleted successfully..")));

    }

}
