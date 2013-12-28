package interfaces;

import java.util.List;

public interface ConflictsModel {

    public List<?> getConflicts();
    
    public void deleteConflicts();
    
    public void deleteConflict(int id);
    
}
