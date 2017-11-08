package de.thral.atemschutzueberwachung.persistence;

import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.domain.Draegerman;

public class DraegermanDAOImplDummy implements DraegermanDAO {

    List<Draegerman> list;

    public DraegermanDAOImplDummy(){
        list = new ArrayList<>();
        list.add(new Draegerman("träger1", "Atemschutz 1"));
        list.add(new Draegerman("träger2", "Atemschutz 2"));
        list.add(new Draegerman("träger3", "Atemschutz 3"));
        list.add(new Draegerman("träger4", "Atemschutz 4"));
        list.add(new Draegerman("träger5", "Atemschutz 5"));
        list.add(new Draegerman("träger6", "Atemschutz 6"));
        list.add(new Draegerman("träger7", "Atemschutz 7"));
        list.add(new Draegerman("träger8", "Atemschutz 8"));
        list.add(new Draegerman("träger9", "Atemschutz 9"));
        list.add(new Draegerman("träger10", "Atemschutz 10"));
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
