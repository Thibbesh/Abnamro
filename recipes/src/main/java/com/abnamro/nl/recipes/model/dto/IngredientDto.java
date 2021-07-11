package com.abnamro.nl.recipes.model.dto;

import lombok.Data;
import com.abnamro.nl.recipes.model.entity.Ingredient;

@Data
public class IngredientDto {

    private int ingredientId;

    private String name;

    private String amount;

    /**
     * Its a static method to convert entity object to presentation dto
     * @param ingredient from Ingredient entity
     * @return ingredientDto
     */
    public static IngredientDto from(Ingredient ingredient){
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setIngredientId(ingredient.getIngredientId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setAmount(ingredient.getAmount());
        return ingredientDto;
    }

}
