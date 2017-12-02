package de.thral.atemschutzueberwachung.persistence;

import android.content.Context;

import java.util.List;

import de.thral.atemschutzueberwachung.business.Draegerman;

public interface DraegermanDAO {

    List<Draegerman> getAll();
    Draegerman prepareAdd(String firstname, String lastname);
    boolean add(Draegerman draegerman, Context context);
    boolean remove(Draegerman draegerman);
}
