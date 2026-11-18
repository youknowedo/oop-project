package it.chalmers.commit;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String id;
    private String name; 
    private UserRole role; 
    private List<String> allergies;


    public User(String id, String name, UserRole role, List<String> allergies)  {
    this.id = id;
    this.name = name;
    this.role = role;
    this.allergies = new ArrayList<>(allergies);
    }


    public void addAllergy(String allergy){
        allergies.add(allergy);
    }

    public void removeAllergy(String allergy){
        allergies.remove(allergy);
    }

    public List<String> getAllergies() {
        return allergies;
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