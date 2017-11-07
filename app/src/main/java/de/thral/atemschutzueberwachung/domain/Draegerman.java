package de.thral.atemschutzueberwachung.domain;

public class Draegerman implements Comparable<Draegerman>{

    private final String firstName;
    private final String lastName;
    private final String displayName;

    public Draegerman(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = lastName;
    }

    public Draegerman(String firstName, String lastName, String displayName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Draegerman that = (Draegerman) o;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null)
            return false;
        return lastName != null ? lastName.equals(that.lastName) : that.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Draegerman draegerman) {
        int compareLastName = lastName.compareTo(draegerman.getLastName());
        if(compareLastName == 0){
            return firstName.compareTo(draegerman.getFirstName());
        }
        return compareLastName;
    }
}
