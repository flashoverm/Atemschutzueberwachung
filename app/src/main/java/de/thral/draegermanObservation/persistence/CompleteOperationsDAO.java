package de.thral.draegermanObservation.persistence;

import android.app.Activity;

import java.util.List;

import de.thral.draegermanObservation.business.Operation;

public interface CompleteOperationsDAO {

    String LOG_TAG = "PERSISTENCE_COMPLETE";

    /** Returns all completed operations
     *
     * @return list of CompleteOperation objects
     */
    List<CompleteOperation> getAll();

    /** Adds operation to complete operations
     *
     * @param operation to be added
     * @return true if adding succeeds, false if not
     */
    boolean add(Operation operation);

    /** Removes operation
     *
     * @param operation CompleteOperation to be removed
     * @return true if removing succeeded, false if not
     */
    boolean delete(CompleteOperation operation);

    /** Sets up external storage for exporting the completed operations
     *
     * @param activity Activity for access of the storage
     * @return true if storage is ready, false if not
     */
    boolean setupStorage(Activity activity);

    /** Exports completed operation to the storage prepared with setupStorage()
     *
     * @param export CompleteOperation to be exported
     * @return true if export succeeds, false if not
     */
    boolean export(CompleteOperation export);

}
