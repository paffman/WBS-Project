package de.fhbingen.wbs.dbaccess.repositories.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParentToElementMappedCache<T> {
    private Map<Integer, Map<Integer, T>> parentPkToElementPkMap;

    public ParentToElementMappedCache() {
        this.loadCache();
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
    
    public T getElement(Integer id) {
        for (Map<Integer, T> pkToElementMap : this.parentPkToElementPkMap.values()) {
            if (pkToElementMap.containsKey(id)) {
                return pkToElementMap.get(id);
            }
        }

        return null;
    }

    public void setElement(T element) {
        Map<Integer, T> parentMap = this.parentPkToElementPkMap.get(this.getParentId(element));
        if (parentMap == null) {
            Map<Integer, T> newElements = new HashMap<>();
            newElements.put(this.getId(element), element);
            this.parentPkToElementPkMap.put(this.getParentId(element), newElements);
        } else {
            parentMap.put(this.getId(element), element);
        }
    }

    public List<T> getAllByParentElement(Integer parentElementId) {
        Map<Integer, T> elements = this.getParentPkToElementPkMap().get(parentElementId);

        if (elements != null) {
            return new ArrayList<>(elements.values());
        }

        return new ArrayList<>();
    }

    public Map<Integer, Map<Integer, T>> getParentPkToElementPkMap() {
        return parentPkToElementPkMap;
    }

    public void loadCache() {
        this.parentPkToElementPkMap = this.loadParentPkToElementPkMap();
    }

    protected abstract List<T> loadAllElements();

    protected abstract Integer getParentId(T element);

    protected abstract Integer getId(T element);
}
