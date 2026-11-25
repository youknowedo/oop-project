package it.chalmers.commit.model;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String id;
  private String name;
  private UserRole role;
  private List<String> foodPreferences;

  public User(String id, String name, UserRole role, List<String> foodPreferences) {
    this.id = id;
    this.name = name;
    this.role = role;
    this.foodPreferences = new ArrayList<>(foodPreferences);
  }

  public void addFoodPreference(String foodPreference) {
    foodPreferences.add(foodPreference);
  }

  public void removeFoodPreference(String foodPreference) {
    foodPreferences.remove(foodPreference);
  }

  public List<String> getFoodPreferences() {
    return foodPreferences;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  public UserRole getRole() {
    return this.role;
  }

  public void updateName(String newName) {
    this.name = newName;
  }
}
