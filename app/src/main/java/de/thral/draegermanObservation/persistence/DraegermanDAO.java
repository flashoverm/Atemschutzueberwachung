package de.thral.draegermanObservation.persistence;

import android.content.Context;

import java.util.List;

import de.thral.draegermanObservation.business.Draegerman;

public interface DraegermanDAO {

    /** Gets all draegermen
     *
     * @return list of Draegerman objects
     */
    List<Draegerman> getAll();

    /** Prepares add of draegerman. Set complete name if lastname is already existing
     *
     * @param firstname of draegerman
     * @param lastname of draegerman
     * @return new draegerman object
     */
    Draegerman prepareAdd(String firstname, String lastname);

    /** Adds draegerman to the list
     *
     * @param draegerman to be added
     * @return true of added, false if not
     */
    boolean add(Draegerman draegerman);

    /** Removes draegerman from the list
     *
     * @param draegerman to be removed
     * @return true of removed, false if not
     */
    boolean remove(Draegerman draegerman);
}
