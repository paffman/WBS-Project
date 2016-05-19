package de.fhbingen.wbs.dbServices;

import de.fhbingen.wbs.globals.Workpackage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Dependency;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.dbaccess.data.WorkpackageAllocation;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und
 * Loeschen von Arbeitspaketen.<br/>
 * Zusaetzlich werden hier die Abhaenigkeiten der Arbeitspakete verwaltet.<br/>
 *
 * @author Sven Seckler, Jens Eckes
 * @version 2.0 - 2012-08-15
 */
public class WorkpackageService {

    /**
     * Soll aus der konkatenierten hierarchisch ID ein Objekt mit allen
     * vorhandenen Daten machen.
     *
     * @param stringID
     *            ist eine konkatenierten hierarchisch ID eines Arbeitspaketes.
     * @return Liefert ein bestimmtes Workpackage-Objekt mit allen Daten aus der
     *         Datenbank zurueck.
     */
    public static Workpackage getWorkpackage(final String stringID) {
        de.fhbingen.wbs.dbaccess.data.Workpackage workpackage =
                DBModelManager.getWorkpackageModel().getWorkpackage(stringID);

        if (workpackage != null) {
            ArrayList<Employee> maIds = new ArrayList<Employee>();

            // Konstruktor Aufruf des Workpackage mit Initialisierung aus
            // der Datenbank
            Workpackage erg = new Workpackage(workpackage, maIds);

            // Fuellen der Liste fuer aller Zustaendigen zu dem Workpackage
            List<WorkpackageAllocation> wpAllocations =
                    DBModelManager.getWorkpackageAllocationModel()
                            .getWorkpackageAllocation(workpackage.getId());
            for (WorkpackageAllocation wpAllocation : wpAllocations) {
                maIds.add(DBModelManager.getEmployeesModel().getEmployee(
                        wpAllocation.getFid_emp()));
            }
            return erg;
        }
        return null;
    }

    /**
     * Soll aus einer Set an konkatenierten hierarchisch IDs mehrere Objekt mit
     * allen vorhandenen Daten machen.
     *
     * @param stringIdSet
     *            ist ein Set an konkatenierten hierarchisch ID von
     *            Arbeitspaketen.
     * @return liefert ein Set von Workpackages mit allen Daten aus der
     *         Datenbank.
     */
    public static Set<Workpackage> getWorkpackages(Set<String> stringIdSet) {
        Set<Workpackage> workSet = new HashSet<Workpackage>();

        Iterator<String> iterator = stringIdSet.iterator();
        while (iterator.hasNext()) {
            workSet.add(getWorkpackage(iterator.next()));
        }

        return workSet;
    }

    /**
     * Ein neues Workpackage soll in die Datenbank eingefuegt werden.
     *
     * @param wp
     *            ein neues Workpackage was in die Datenbank eingetragen werde
     *            soll.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean insertWorkpackage(Workpackage wp) {
        wp.setParentID();
        boolean success =
                DBModelManager.getWorkpackageModel().addNewWorkpackage(
                        wp.getWp());
        if (success) {
            wp.reloadFromDB();
        }
        return success;
    }

    /**
     * Ein geaendertes Workpackage soll in die Datenbank uebernommen werden.
     *
     * @param wp
     *            ein Workpackage dessen Aenderungen in der Datenbank
     *            uebernommen werden sollen.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean updateWorkpackage(Workpackage wp) {
        return DBModelManager.getWorkpackageModel().updateWorkpackage(
                wp.getWp());
    }

    /**
     * Ein zu loeschendes Workpackage soll aus der Datenbank entfernt werden.
     *
     * @param wp
     *            ein Workpackage das aus der Datenbank entfernt werden soll.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean deleteWorkpackage(Workpackage wp) {
        return DBModelManager.getWorkpackageModel().deleteWorkpackage(
                wp.getWpId());
    }

    /**
     * Liefert alle Abhaengigkeiten von Arbeitspaketen zu ihren Vorgaenger
     *
     * @return Map<String, Set<String>> Alle Arbeitspaket zu ihren Vorgaenger
     */
    public static Map<Integer, Set<Integer>> getFollowerToAncestorsIdMap() {
        return getConnectionIdMap(true);
    }

    /**
     * Liefert alle Abhaengigkeiten von Arbeitspaketen zu ihren Nachfolgern
     *
     * @return Map<String, Set<String>> Alle Arbeitspaket zu ihren Nachfolgern
     */
    public static Map<Integer, Set<Integer>> getAncestorToFollowersIdMap() {
        return getConnectionIdMap(false);
    }

    /**
     * Speichert einen Nachfolger fuer ein bestimmtes Arbeitspaket.
     *
     * @param thisAP
     *            StringID des Arbeitspaketes fuer das der Nachfolger gesetzt
     *            werden soll.
     * @param followerAP
     *            StringID des Nachfolgers welcher gespeichert werden soll.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean setFollower(int thisAP, int followerAP) {
        Dependency dep = new Dependency();
        dep.setFid_wp_predecessor(thisAP);
        dep.setFid_wp_successor(followerAP);
        return DBModelManager.getDependenciesModel().addNewDependency(dep);
    }

    /**
     * Speichert einen Vorgaenger fuer ein bestimmtes Arbeitspaket.
     *
     * @param thisAP
     *            StringID des Arbeitspaketes fuer das der Vorgaenger gesetzt
     *            werden soll.
     * @param followerAP
     *            StringID des Vorgaengers welcher gespeichert werden soll.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean setAncestor(int thisAP, int ancestorAP) {
        return setFollower(ancestorAP, thisAP);
    }

    /**
     * Loeschten einer bestimmten Nachfolger-Beziehung zwischen zwei
     * Arbeitspaketen.
     *
     * @param thisAP
     *            ist die StringID eines bestimmten Arbeitspaketes.
     * @param followerAP
     *            ist die StringID eines bestimmten Nachfolgers.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean deleteFollower(int thisAP, int followerAP) {
        return DBModelManager.getDependenciesModel().deleteDependency(thisAP,
                followerAP);
    }

    /**
     * Loeschten einer bestimmten Vorgaenger-Beziehung zwischen zwei
     * Arbeitspaketen.
     *
     * @param thisAP
     *            ist die StringID eines bestimmten Arbeitspaketes.
     * @param followerAP
     *            ist die StringID eines bestimmten Vorgaenger.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean deleteAncestor(int thisAP, int ancestorAP) {
        return deleteFollower(ancestorAP, thisAP);
    }

    /**
     * Soll in der DB.Paketzuweisung einen Arbeiter einem Paket zuweisen.
     *
     * @param wp
     *            ein Workpackage dem ein Arbeiter zugewiesen werden soll.
     * @param workerID
     *            der Login eines neuen Arbeiters.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean addWpWorker(Workpackage wp, int workerID) {
        WorkpackageAllocation wpAlloc = new WorkpackageAllocation();
        wpAlloc.setFid_emp(workerID);
        wpAlloc.setFid_wp(wp.getWpId());
        if (wp.getWorkersIds().contains(workerID)) {
            return true;
        }
        return DBModelManager.getWorkpackageAllocationModel()
                .addNewWorkpackageAllocation(wpAlloc);
    }

    /**
     * Soll einen Arbeiter aus DB.Paketzuweisung entfernen.
     *
     * @param wp
     *            ein Workpackage dem ein Arbeiter entfernt werden soll.
     * @param workerID
     *            der Login eines Arbeiters.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean removeWpWorker(Workpackage wp, int workerID) {
        return DBModelManager.getWorkpackageAllocationModel()
                .deleteWorkpackageAllocation(workerID, wp.getWpId());
    }

    /**
     * Generiert aus einem Workpackage seine StringID
     *
     * @param wp
     *            das betroffen Workpackage.
     * @return Gibt die eindeutige StringID zurueck.
     */
    private static String getStringIdFromWp(Workpackage wp) {
        String stringID = "";
        stringID += wp.getLvl1ID() + ".";
        stringID += wp.getLvl2ID() + ".";
        stringID += wp.getLvl3ID() + ".";
        stringID += wp.getLvlxID();
        return stringID;
    }

    /**
     * Sucht nach allen Abhaengigkeiten von first zu second
     *
     * @param followerFirst
     *            When false returns Predecessor, Successor in Map, when true
     *            Successor, Predecessor
     * @return Set<Workpackage> mit allen Objekten in der mitgegebenen
     *         Abhaengigkeit.
     */
    private static Map<Integer, Set<Integer>> getConnectionIdMap(
            final boolean followerFirst) {
        List<Dependency> dependencies =
                DBModelManager.getDependenciesModel().getDependency();
        Map<Integer, Set<Integer>> ergMap =
                new TreeMap<Integer, Set<Integer>>();
        int followerKey;
        for (Dependency dep : dependencies) {
            if (!followerFirst) {
                followerKey = dep.getFid_wp_predecessor();
                if (!ergMap.containsKey(followerKey)) {
                    ergMap.put(followerKey, new HashSet<Integer>());
                }
                ergMap.get(followerKey).add(dep.getFid_wp_successor());
            } else {
                followerKey = dep.getFid_wp_successor();
                if (!ergMap.containsKey(followerKey)) {
                    ergMap.put(followerKey, new HashSet<Integer>());
                }
                ergMap.get(followerKey).add(dep.getFid_wp_predecessor());
            }

        }
        return ergMap;
    }

    /**
     * Liefert ein Set mit allen Arbeitsparketen des Projekts.
     *
     * @return Set<Workpackage> alle Arbeitsparkete
     */
    public static Set<Workpackage> getAllAp() {
        Set<Workpackage> returnSet = null;
        try {
            returnSet =
                    WorkpackageService.fillWps(new HashSet<Workpackage>(),
                            DBModelManager.getWorkpackageModel()
                                    .getWorkpackage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnSet;
    }

    /**
     * Liefert ein Set mit allen Arbeitsparketen des Projekts in einem
     * bestimmten Zeitraum.
     *
     * @param from
     *            Startdatum des Zeitraums
     * @param to
     *            Enddatum des Zeitraums
     * @return Set<Workpackage> alle Arbeitsparkete
     */
    public static Set<Workpackage> getAllAp(final Date from, final Date to) {
        Set<Workpackage> returnSet = null;
        try {
            returnSet =
                    WorkpackageService.fillWps(new HashSet<Workpackage>(),
                            DBModelManager.getWorkpackageModel()
                                    .getWorkpackagesInDateRange(from, to));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnSet;
    }

    /**
     * Liefert ein Set mit allen Arbeitsparketen, die einem bestimmten
     * Mitarbeiter zugewiesen sind.
     *
     * @param workerID
     *            ID des Mitarbeiters fuer den alle Arbeitsparkete geladen
     *            werden soll.
     * @return Set<Workpackage> alle Arbeitsparkete
     */
    public static Set<Workpackage> getAllWorkerAp(final int workerID) {
        Set<Workpackage> returnSet = null;
        try {
            returnSet =
                    WorkpackageService.fillWps(new HashSet<Workpackage>(),
                            DBModelManager.getWorkpackageAllocationModel()
                                    .getWorkpackageAllocationJoinWP(workerID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnSet;
    }

    /**
     * Liefert ein Set mit allen Arbeitsparketen, die einem bestimmten
     * Mitarbeiter zugewiesen sind und in einem bestimmten Zeitraum liegen.
     *
     * @param workerID
     *            ID des Mitarbeiters fuer den alle Arbeitsparkete geladen
     *            werden soll.
     * @param from
     *            Startdatum des Zeitraums
     * @param to
     *            Enddatum des Zeitraums
     * @return Set<Workpackage> alle Arbeitsparkete
     */
    public static Set<Workpackage> getAllWorkerAp(final int workerID,
            final Date from, final Date to) {
        Set<Workpackage> returnSet = null;
        try {
            returnSet =
                    WorkpackageService.fillWps(
                            new HashSet<Workpackage>(),
                            DBModelManager.getWorkpackageAllocationModel()
                                    .getWorkpackageAllocationJoinWP(workerID,
                                            from, to));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnSet;
    }

    /**
     * Diese Methode fuellt ein Set von Workpackages mit den Daten eines
     * ResultSets
     *
     * @param workSet
     *            das zu fuellende Set vom Typ Workpackage
     * @param workpackages
     *            die Workpackages aus der Datenbank
     * @return ein Set vom Typ Workpackage
     * @throws SQLException
     */
    private static Set<Workpackage> fillWps(final Set<Workpackage> workSet,
            final List<de.fhbingen.wbs.dbaccess.data.Workpackage> workpackages)
            throws SQLException {
        ArrayList<Employee> maIds = null;
        for (de.fhbingen.wbs.dbaccess.data.Workpackage wp : workpackages) {
            maIds = new ArrayList<Employee>();
            List<WorkpackageAllocation> wpAllocations =
                    DBModelManager.getWorkpackageAllocationModel()
                            .getWorkpackageAllocation(wp.getId());
            Employee emp;
            for (WorkpackageAllocation wpAllocation : wpAllocations) {
                emp =
                        DBModelManager.getEmployeesModel().getEmployee(
                                wpAllocation.getFid_emp());
                maIds.add(emp);
            }
            workSet.add(new Workpackage(wp, maIds));
            maIds = null;
        }
        return workSet;
    }

    /**
     * updates the stringId of the given workpackage
     *
     * @param wp workpackage to update
     * @param newStringId new string id
     * @return
     */
    public static boolean updateStringId(Workpackage wp, String newStringId) {
        return DBModelManager.getWorkpackageModel().updateStringId(wp.getWp(), newStringId);
    }
}