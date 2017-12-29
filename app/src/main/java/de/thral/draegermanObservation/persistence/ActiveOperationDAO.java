package de.thral.draegermanObservation.persistence;

import de.thral.draegermanObservation.business.Operation;

public interface ActiveOperationDAO {

    String LOG_TAG = "PERSISTENCE_ACTIVE";

    /** Gets active operation (run load() once before use)
     *
     * @return active operation
     */
    Operation get();

    /** Loads active operation in memory
     *
     */
    void load();

    /** Creates new operation as active
     *
     * @return true if succeeded, false if not
     */
    boolean add();

    /** Saves operation to the storage
     *
     * @return true if succeeded, false if not
     */
    boolean update();

    /** Deletes active operation from the storage and sets active to null
     *
     * @return true if succeeded, false if not
     */
    boolean delete();
}
