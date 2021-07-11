package com.abnamro.nl.recipes.model.entity;

import com.abnamro.nl.recipes.model.dto.IngredientDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ingredientId;

    @Column(name="name")
    private String name;

    @Column(name="amount")
    private String amount;

    @ManyToOne()
    private Recipe recipe;

    /**
     * Its a static method to convert presentation dto to entity object.
     * @param ingredientDto from presentation layer
     * @return ingredient
     */
    public static Ingredient from(IngredientDto ingredientDto){
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientDto.getIngredientId());
        ingredient.setName(ingredientDto.getName());
        ingredient.setAmount(ingredientDto.getAmount());
        return ingredient;
    }
}
