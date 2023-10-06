/**
 * @author Lily Ellison - lbellison
 * CIS175 - Fall 2023
 * Oct 2, 2023
 *
 * @author Adam Reese - amreese3
 * CIS175 - Fall 2023
 * Oct 2, 2023
 */

package model;

import javax.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	// Default constructor
	public Ingredient() {
	}

	// Constructor with name, quantity, and unit
	public Ingredient(String name) {
		this.name = name;
	}

	// Getter for id
	public int getId() {
		return id;
	}

	// Setter for id
	public void setId(int id) {
		this.id = id;
	}

	// Getter for name
	public String getName() {
		return name;
	}

	// Setter for name
	public void setName(String name) {
		this.name = name;
	}

	// Override toString() method
	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name + "]";
	}
}
