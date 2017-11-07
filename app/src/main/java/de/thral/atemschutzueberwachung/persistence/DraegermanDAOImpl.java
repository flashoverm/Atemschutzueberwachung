package de.thral.atemschutzueberwachung.persistence;

import java.util.List;

import de.thral.atemschutzueberwachung.domain.Draegerman;

public class DraegermanDAOImpl implements DraegermanDAO {

    //TODO implement handling for equal lastnames and equal firstnames

    public DraegermanDAOImpl(){

    }

    @Override
    public List<Draegerman> getAll() {
        return null;
    }

    @Override
    public boolean add(Draegerman draegerman) {
        return false;
    }

    @Override
    public boolean remove(Draegerman draegerman) {
        return false;
    }
}
