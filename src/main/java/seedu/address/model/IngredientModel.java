package seedu.address.model;

import seedu.address.model.ingredient.Ingredient;

/**
 * The API that stores the ingredient side of the model.
 */
public interface IngredientModel {
    /**
     * Returns true if a ingredient with the same identity as {@code ingredient} exists in the restaurant book.
     */
    boolean hasIngredient(Ingredient ingredient);

    /**
     * Deletes the given ingredient.
     * The ingredient must exist in the restaurant book.
     */
    void deleteIngredient(Ingredient target);

    /**
     * Adds the given ingredient.
     * {@code ingredient} must not already exist in the restaurant book.
     */
    void addIngredient(Ingredient ingredient);
}
