/**
 * @author Lily Ellison - lbellison
 * CIS175 - Fall 2023
 * Oct 5, 2023
 *
 * @author Adam Reese - amreese3
 * CIS175 - Fall 2023
 * Oct 5, 2023
 */

package controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import exceptions.DatabaseAccessException;
import model.Ingredient;
import model.Recipe;

public class IngredientHelper {

	private final EntityManager em;

	// Constructor that takes an EntityManager as a parameter
	public IngredientHelper(EntityManager em) {
		this.em = em;
	}

	// Inserts a new ingredient into the database
	public void insertIngredient(Ingredient toAdd) throws DatabaseAccessException {
		try {
			// Check if the ingredient already exists
			Ingredient existingIngredient = getIngredientByName(toAdd.getName());

			// If it already exists, you can choose to update it or skip adding
			if (existingIngredient != null) {
				System.out.println("Ingredient already exists: " + existingIngredient.getName());
				return; // Skip adding
			}

			em.getTransaction().begin();
			em.persist(toAdd);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DatabaseAccessException("Error inserting ingredient: " + e.getMessage());
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}
	}

	// Deletes an ingredient from the database
	public void deleteIngredient(Ingredient toDelete) throws DatabaseAccessException {
		try {
			// Check if the ingredient is used in any recipe
			if (isIngredientUsedInRecipes(toDelete)) {
				System.out.println("Ingredient is used in one or more recipes and cannot be deleted.");
			} else {
				em.getTransaction().begin();
				Ingredient result = em.find(Ingredient.class, toDelete.getId());
				em.remove(result);
				em.getTransaction().commit();
				System.out.println("Ingredient deleted successfully.");
			}
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new DatabaseAccessException("Error deleting ingredient: " + e.getMessage());
		}
	}

	// Check if an ingredient is used in any recipes
	private boolean isIngredientUsedInRecipes(Ingredient ingredient) {
		try {
			TypedQuery<Recipe> typedQuery = em
					.createQuery("SELECT r FROM Recipe r WHERE :ingredient MEMBER OF r.ingredients", Recipe.class);
			typedQuery.setParameter("ingredient", ingredient);

			List<Recipe> result = typedQuery.getResultList();
			return !result.isEmpty(); // Return true if used in any recipes, false otherwise
		} catch (Exception e) {
			// Handle any exceptions that may occur during the query
			e.printStackTrace();
			return false; // Return false on error (you can handle this differently if needed)
		}
	}

	// Retrieves a list of all ingredients from the database
	public List<Ingredient> showAllIngredients() throws DatabaseAccessException {
		try {
			TypedQuery<Ingredient> typedQuery = em.createQuery("SELECT i FROM Ingredient i", Ingredient.class);
			return typedQuery.getResultList();
		} catch (Exception e) {
			throw new DatabaseAccessException("Error retrieving ingredients: " + e.getMessage());
		}
	}

	// Searches for an ingredient by its name
	public Ingredient getIngredientByName(String ingredientName) throws DatabaseAccessException {
		try {
			TypedQuery<Ingredient> typedQuery = em
					.createQuery("SELECT i FROM Ingredient i WHERE i.name = :selectedName", Ingredient.class);
			typedQuery.setParameter("selectedName", ingredientName);

			List<Ingredient> result = typedQuery.getResultList();
			if (!result.isEmpty()) {
				return result.get(0);
			} else {
				// Debug print
				System.out.println("Ingredient not found in the database.");
				return null; // Return null if not found
			}
		} catch (Exception e) {
			throw new DatabaseAccessException("Error searching for ingredient by name: " + e.getMessage());
		}
	}
}
