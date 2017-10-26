package de.thral.atemschutzueberwachung.Draegerman;

/**
 * Created by Markus Thral on 21.10.2017.
 */

public class Draegerman implements Comparable<Draegerman>{

    private final String firstName;
    private final String lastName;

    public Draegerman(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
