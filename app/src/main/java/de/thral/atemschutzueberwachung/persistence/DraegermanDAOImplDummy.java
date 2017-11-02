package de.thral.atemschutzueberwachung.persistence;

import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.domain.Draegerman;

/**
 * Created by Markus Thral on 28.10.2017.
 */

public class DraegermanDAOImplDummy implements DraegermanDAO {

    List<Draegerman> list;

    public DraegermanDAOImplDummy(){
        list = new ArrayList<>();
        list.add(new Draegerman("Markus", "Thral"));
        list.add(new Draegerman("Sebastian", "Dürschmidt"));
        list.add(new Draegerman("Tobias", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));

        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
        list.add(new Draegerman("Michael", "Wagner"));
    }

    @Override
    public List<Draegerman> getAll() {
        return list;
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
