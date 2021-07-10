package nl.abnamro.com.recipes.repository;

import nl.abnamro.com.recipes.model.entity.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

}
