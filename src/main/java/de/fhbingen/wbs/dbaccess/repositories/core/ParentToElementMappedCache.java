package de.fhbingen.wbs.dbaccess.repositories.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParentToElementMappedCache<T> {
    private Map<Integer, Map<Integer, T>> parentPkToElementPkMap;

    public ParentToElementMappedCache() {
        this.parentPkToElementPkMap = this.loadParentPkToElementPkMap();
    }

    private Map<Integer, Map<Integer, T>> loadParentPkToElementPkMap() {
        Map<Integer, Map<Integer, T>> newGetParentPkToElementPkMap = new HashMap<>();
        Map<Integer, T> currentParentPkToElementPkMap;

        for (T element : this.loadAllElements()) {
            if ((currentParentPkToElementPkMap = newGetParentPkToElementPkMap.get(this.getParentId(element))) == null) {
                currentParentPkToElementPkMap = new HashMap<>();
                currentParentPkToElementPkMap.put(this.getId(element), element);
                newGetParentPkToElementPkMap.put(this.getParentId(element), currentParentPkToElementPkMap);
            } else {
                currentParentPkToElementPkMap.put(this.getId(element), element);
            }
        }

        return newGetParentPkToElementPkMap;
    }

    public List<T> getAllByParentElement(Integer parentElementId) {
        return new ArrayList<>(this.parentPkToElementPkMap.get(parentElementId).values());
    }

    public Map<Integer, Map<Integer, T>> getParentPkToElementPkMap() {
        return parentPkToElementPkMap;
    }

    protected abstract List<T> loadAllElements();

    protected abstract Integer getParentId(T element);

    protected abstract Integer getId(T element);
}
