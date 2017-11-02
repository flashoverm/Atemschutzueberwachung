package de.thral.atemschutzueberwachung.persistence;

import java.util.List;

import de.thral.atemschutzueberwachung.domain.Draegerman;

/**
 * Created by Markus Thral on 28.10.2017.
 */

public interface DraegermanDAO {

    List<Draegerman> getAll();
    boolean add(Draegerman draegerman);
    boolean remove(Draegerman draegerman);
}
