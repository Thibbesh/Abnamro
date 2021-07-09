package nl.abnamro.com.recipes.model;

import lombok.Data;
import nl.abnamro.com.recipes.model.dto.RecipeDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="servings")
    private int servings;

    @Column(name="created")
    private LocalDateTime created = LocalDateTime.now();

    @Column(name="vegetarian")
    private Boolean vegetarian;

    @Column(name="instructions")
    @Lob
    private String instructions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private List<Ingredient> ingredients = new ArrayList<>();


    /**
     * its a static method to convert dto object to entity.
     * @param recipeDto from presentation layer
     * @return recipe entity
     */
    public static Recipe from(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDto.getId());
        recipe.setName(recipeDto.getName());
        recipe.setServings(recipeDto.getServings());
        recipe.setVegetarian(recipeDto.getVegetarian());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setIngredients(recipeDto.getIngredients().stream().map(Ingredient::from).collect(Collectors.toList()));
        return recipe;
    }

}
