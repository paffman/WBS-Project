package dbServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import jdbcConnection.SQLExecuter;
import wpWorker.Worker;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse bietet Datenbank-Zugriffe fuer Anfragen bezueglich Mitarbeitern<br/>
 * 
 * @author Michael Anstatt, Sven Seckler
 * @version 2.0 - 2012-08-21
 */
public class WorkerService {

    /**
     * wird beim initialisieren der Datenelemente aufgerufen speichert in eine
     * ArrayListe alle im Projekt vorhandenen Mitarbeiter
     * 
     * @return ArrayListe mit allen Mitarbeitern
     */
    public static ArrayList<Worker> getAllWorkers() {
        ArrayList<Worker> alleMit = new ArrayList<Worker>();
        List<Employee> employees =
                DBModelManager.getEmployeesModel().getEmployee();
        for (Employee emp : employees) {
            alleMit.add(new Worker(emp.getLogin(), emp.getId(), emp
                    .getFirst_name(), emp.getLast_name(), emp
                    .isProject_leader(), emp.getDaily_rate()));
        }
        return alleMit;
    }

    /**
     * Gibt alle "echten" Mitarbeiter zurueck, also ohne "Leiter"
     * 
     * @return alle Mitarbeiter ohne "Leiter"
     */
    public static ArrayList<Worker> getRealWorkers() {
        ArrayList<Worker> alleMit = new ArrayList<Worker>();
        List<Employee> employees =
                DBModelManager.getEmployeesModel().getEmployee(false);
        for (Employee emp : employees) {
            alleMit.add(new Worker(emp.getLogin(), emp.getId(), emp
                    .getFirst_name(), emp.getLast_name(), emp
                    .isProject_leader(), emp.getDaily_rate()));
        }

        return alleMit;
    }

}
