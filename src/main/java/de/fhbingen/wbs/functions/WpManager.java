package de.fhbingen.wbs.functions;

import de.fhbingen.wbs.dbServices.WorkpackageService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.dbaccess.data.WorkEffort;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.globals.Loader;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.wpConflict.ConflictCompat;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import de.fhbingen.wbs.wpComparators.APLevelComparator;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpWorker.User;
import de.fhbingen.wbs.wpWorker.Worker;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Schnittstelle zur Verwaltung wie loeschen/anlegen/aktualisieren von
 * Arbeitspaketen und Vorgaengern/Nachfolgern<br/>
 * synchron auf Datenbank und Java-Ebene<br/>
 *
 * @author Michael Anstatt, Marc-Eric Baumgärtner, Jens Eckes
 * @version 2.0 - 2012-08-20
 */
public class WpManager {

    private static APList list;
    private static Workpackage rootWp;

    /**
     * Gibt das Objekt zum Arbeitspaket mit der uebergebenen ID zurueck
     *
     * @param id
     *            ID der Form x.x.x...
     * @return Workpackage-Objekt mit Werten aus der Datenbank
     */
    public static Workpackage getWorkpackage(String id) {
        return list.getWorkpackage(id);
    }

    public static Workpackage getWorkpackage(int id) {
        return list.getWorkpackage(id);
    }

    /**
     * Liest Arbeitspakete und Bezeihungsstruktur aus Datenbank aus. Laengere
     * Laufzeit, sollte einmalig bei Login aufgerufen werden
     */
    public static void loadDB() {
        Loader.reset();
        Loader.setLoadingText(LocalizedStrings.getStatus().loadWps());
        list = new APList();
        Loader.setLoadingText(LocalizedStrings.getStatus().loadDependencies());
        list.setAncestorsAndFollowers();
    }

    /**
     * Versucht einen Vorgaenger einzufuegen. Wenn Schleifen auftreten wird der
     * Vorgaenger nicht gesetzt. Treten keine Schleifen auf wird die Beziehung
     * gesetzt und in die Datenbank eingetragen. Wenn eine Schleife auftritt
     * wird ein Dialogfenster angezeigt
     *
     * @param anchestor
     *            zu setzender Vorgaenger
     * @param main
     *            Arbeitspaket zu dem der Vorgaenger gesetzt werden soll
     * @return false wenn Vorgaenger aufgrund von Schleifen nicht gesetzt wurde
     */
    public static boolean
            insertAncestor(Workpackage anchestor, Workpackage main) {
        if (list.setAncestor(anchestor, main)) {
            WorkpackageService.setAncestor(main.getWpId(), anchestor.getWpId());
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_DEPENDENCIES,
                    WPOverview.getUser().getId(), main));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Versucht einen Nachfolger einzufuegen. Wenn Schleifen auftreten wird der
     * Nachfolger nicht gesetzt. Treten keine Schleifen auf wird die Beziehung
     * gesetzt und in die Datenbank eingetragen. Wenn eine Schleife auftritt
     * wird ein Dialogfenster angezeigt
     *
     * @param follower
     *            zu setzender Nachfolger
     * @param main
     *            Arbeitspaket zu dem der Nachfolger gesetzt werden soll
     * @return false wenn Nachfolger aufgrund von Schleifen nicht gesetzt wurde
     */
    public static boolean
            insertFollower(Workpackage follower, Workpackage main) {
        if (list.setFollower(follower, main)) {
            WorkpackageService.setFollower(main.getWpId(), follower.getWpId());
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_DEPENDENCIES,
                    WPOverview.getUser().getId(), main));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Loescht einen Vorgaenger
     *
     * @param ancestor
     *            zu loeschender Vorgaenger
     * @param main
     *            Arbeitspaket dessen Vorgaenger geloescht werden soll
     * @return false, wenn es Probleme beim loeschen in der Datenbank gab und
     *         die Beziehung nicht geloescht werden konnte
     */
    public static boolean
            removeAncestor(Workpackage ancestor, Workpackage main) {
        list.removeAncestor(ancestor, main);
        if (WorkpackageService.deleteAncestor(main.getWpId(),
                ancestor.getWpId())) {
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_DEPENDENCIES,
                    WPOverview.getUser().getId(), main));
            return true;
        } else {
            System.out.println(LocalizedStrings.getErrorMessages()
                    .deleteFromDbError());
            list.setAncestor(ancestor, main);
            return false;
        }
    }

    /**
     * Loescht einen Nachfolger
     *
     * @param follower
     *            zu loeschender Nachfolger
     * @param main
     *            Arbeitspaket dessen Nachfolger geloescht werden soll
     * @return false, wenn es Probleme beim loeschen in der Datenbank gab und
     *         die Beziehung nicht geloescht werden konnte
     */
    public static boolean
            removeFollower(Workpackage follower, Workpackage main) {
        list.removeFollower(follower, main);
        if (WorkpackageService.deleteFollower(main.getWpId(),
                follower.getWpId())) {
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_DEPENDENCIES,
                    WPOverview.getUser().getId(), main));
            return true;
        } else {
            System.out.println(LocalizedStrings.getErrorMessages()
                    .deleteFromDbError());
            list.setFollower(follower, main);
            return false;
        }
    }

    /**
     * Versucht ein Arbeitspaket komplett zu loeschen. Wenn dies moeglich ist
     * wird es aus der Datenbank entfernt. Dies ist nur moeglich wenn keine
     * Unterarbeitspakete vorhanden sind und keine Beziehungen mehr bestehen. Es
     * wird ein Dialogfenster mit Fehlerbeschreibung angezeigt wenn nicht
     * geloescht werden kann
     *
     * @param removeWp
     *            zu loeschendes Arbeitspaket
     * @return false wenn loeschen nicht moeglich
     */
    public static boolean removeAP(Workpackage removeWp) {

        // check input
        if (!(list.getAncestors(removeWp) != null && removeWp != null && !removeWp
                .equals(getRootAp()))) {
            if (removeWp == null) {
                JOptionPane.showMessageDialog(null, LocalizedStrings
                        .getMessages().selectWp());
            } else {
                JOptionPane.showMessageDialog(null, LocalizedStrings
                        .getErrorMessages().deletePackageMainError());
            }

            return false;
        }

        // check dependencies
        if (!(list.getAncestors(removeWp).isEmpty() && list.getFollowers(
                removeWp).isEmpty())) {
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getErrorMessages().deletePackageDependencyError());
            return false;
        }

        // check children
        if (!WpManager.getUAPs(removeWp).isEmpty()) {
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getErrorMessages().deletePackageSubwpError());
            return false;
        }

        // check for efforts
        if ((int) (double) removeWp.getAc() != 0) {
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getErrorMessages().deletePackageEffortError());
            return false;
        }

        // check for conflicts
        List<de.fhbingen.wbs.dbaccess.data.Conflict> conflicts =
                DBModelManager.getConflictsModel().getConflicts();
        int wpId = removeWp.getWpId();
        for (de.fhbingen.wbs.dbaccess.data.Conflict c : conflicts) {
            if (c.getFid_wp() == wpId || c.getFid_wp_affected() == wpId) {
                JOptionPane.showMessageDialog(null, LocalizedStrings
                        .getErrorMessages().deleteConflictsError());
                return false;
            }
        }

        // delete planned_values
        if (!DBModelManager.getPlannedValueModel().deletePlannedValue(wpId)) {
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getErrorMessages().deletePVError());
            return false;
        }

        // delete emp_allocation
        for (Employee actualWorker : removeWp.getWorkers()) {
            removeWp.removeWorker(actualWorker);
        }

        // delete workapcakge
        if (WorkpackageService.deleteWorkpackage(removeWp)) {

            WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.DELETED_WP, WPOverview
                    .getUser().getId(), WpManager.getRootAp()));
            list.removeWp(removeWp);
        } else {
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getErrorMessages().deletePackageFromDbError());
            return false;
        }
        return true;
    }

    /**
     * Fuegt ein Arbeitspaket in die Datenbank ein und legt Datenstruktur zum
     * spaeteren EIntragen von Beziehungen an.
     *
     * @param newWp
     *            das einzufuegende Arbeitspaket
     * @return false wenn es Probleme mit dem EInfuegen in die DB gab
     */
    public static boolean insertAP(Workpackage newWp) {
        if (WorkpackageService.insertWorkpackage(newWp)) {
            list.insertWp(newWp);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gibt alle Vorgaenger zurueck
     *
     * @param workpackage
     *            Arbeitspaket zu dem die Vorgaenger gewuenscht werden
     * @return Set mit Vorgaengern
     */
    public static Set<Workpackage> getAncestors(Workpackage workpackage) {
        return list.getAncestors(workpackage);
    }

    /**
     * Gibt alle Nachfolger zurueck
     *
     * @param workpackage
     *            Arbeitspaket zu dem die Nachfolger gewuenscht werden
     * @return Set mit Nachfolgern
     */
    public static Set<Workpackage> getFollowers(Workpackage workpackage) {
        return list.getFollowers(workpackage);
    }

    /**
     * Aktualisiert Werte eines vorhandenen Arbeitspakets
     *
     * @param wp
     *            zu aktualisierendes Arbeitspaket
     */
    public static void updateAP(Workpackage wp) {
        //System.out.println("updateAP: " + wp.getName());
        list.updateWp(wp);
        WorkpackageService.updateWorkpackage(wp);
    }

    /**
     * Gibt alle Arbeitspakete als Set zurueck
     *
     * @return Set mit allen Arbeitspaketen
     */
    public static Set<Workpackage> getAllAp() {
        return list.getAllAp();
    }

    public static Workpackage getRootAp() {
        if (rootWp == null) {
            for (Workpackage actualWp : list.getAllAp()) {
                if (actualWp.getLvlID(1) == 0) {
                    rootWp = actualWp;
                }
            }
        }

        return rootWp;
    }

    /**
     * Setzt alle Unterarbeitspakete zu einem bestehenden OAP inaktiv, falls das
     * OAP als inaktiv markiert wird Wird von WPShow.istOAPsetChanges()
     * aufgerufen
     */
    public static void setUapsInaktiv(String id, boolean isInak) {
        Set<Workpackage> actualUAPs =
                WpManager.getUAPs(WpManager.getWorkpackage(id));

        for (Workpackage actualUAP : actualUAPs) {
            actualUAP.setIstInaktiv(isInak);
            WpManager.updateAP(actualUAP);
            setUapsInaktiv(actualUAP.getStringID(), isInak);
        }

    }

    /**
     * Gibt die Arbeitspakete zurueck, denen ein bestimmter Mitarbeiter als
     * Leiter oder Zustaendiger eingetragen ist
     *
     * @param user
     * @return Arbeitspakete in denen ein bestimmter Mitarbeiter als Leiter oder
     *         Zustaendiger eingetragen ist
     */
    public static List<Workpackage> getUserWp(Worker user) {
        List<Workpackage> userWp = new ArrayList<Workpackage>();
        if (!user.getProjLeiter()) {
            for (Workpackage actualWp : WpManager.getAllAp()) {
                if (actualWp.getWorkersIds().contains(user.getId())) {
                    userWp.add(actualWp);
                }
            }
        } else {
            userWp = new ArrayList<Workpackage>(WpManager.getAllAp());
        }

        return userWp;
    }

    /**
     * Gibt alle Arbeitspakete ohne Vorganger zurueck, wichtig fuer die
     * Dauer-/PV-Berechnung
     *
     * @return alle AP ohne Vorgaenger
     */
    public static Set<Workpackage> getNoAncestorWps() {
        return list.getNoAncestorWps();
    }

    /**
     * Liefert alle UAP fuer ein gegebenes OAP
     *
     * @param oap
     *            Oberarbeitspaket
     * @return alle Unterarbeitspakete
     */
    public static Set<Workpackage> getUAPs(Workpackage oap) {
        List<Workpackage> wpList =
                new ArrayList<Workpackage>(WpManager.getAllAp());
        Collections.sort(wpList, new APLevelComparator());

        Set<Workpackage> returnSet = new HashSet<Workpackage>();
        boolean found = false;

        int i = 0;

        for (Workpackage actualWP : wpList) {
            if (found) {

                if (oap.getLvlID(oap.getlastRelevantIndex()) != actualWP
                        .getLvlID(oap.getlastRelevantIndex())) {
                    return returnSet;
                } else if (i < actualWP
                        .getLvlID(oap.getlastRelevantIndex() + 1)) {
                    i = actualWP.getLvlID(oap.getlastRelevantIndex() + 1);
                    returnSet.add(actualWP);
                }

            } else {
                if (actualWP.equals(oap)) {
                    found = true;
                }
            }

        }

        return returnSet;

    }

    /**
     * Gibt alle offenen (also noch nicht abgeschlossenen) Arbeitspakete eines
     * Benutzer und deren OAP (fuer Baumansicht) zurueck.
     *
     * @param user
     * @return offene Arbeitspkaete mit OAPs fuer den angegebenen Mitarbeiter
     */
    public static List<Workpackage> getUserWpOpen(User user) {
        List<Workpackage> allUserWp = WpManager.getUserWp(user);
        Set<Workpackage> returnWp = new HashSet<Workpackage>();
        for (Workpackage actualWp : allUserWp) {
            if (WpManager.calcPercentComplete(actualWp.getBac(),
                    actualWp.getEtc(), actualWp.getAc()) < 100) {
                addWpToList(actualWp, returnWp);
            }
        }
        return new ArrayList<Workpackage>(returnWp);
    }

    /**
     * Gibt alle abgeschlossenen Arbeitspakete eines Benutzer und deren OAP
     * (fuer Baumansicht) zurueck.
     *
     * @param user
     * @return abgeschlossene Arbeitspkaete mit OAPs fuer den angegebenen
     *         Mitarbeiter
     */
    public static List<Workpackage> getUserWpFinished(User user) {
        List<Workpackage> allUserWp = WpManager.getUserWp(user);
        Set<Workpackage> returnWp = new HashSet<Workpackage>();
        for (Workpackage actualWp : allUserWp) {
            if (WpManager.calcPercentComplete(actualWp.getBac(),
                    actualWp.getEtc(), actualWp.getAc()) == 100
                    && actualWp.getAc() > 0) {
                addWpToList(actualWp, returnWp);
            }
        }
        return new ArrayList<Workpackage>(returnWp);
    }

    /**
     * Fuegt ein Arbeitspaket und alle OAPs rekursiv zu einer Liste hinzu
     *
     * @param wp
     *            Arbeitspaket
     * @param wps
     *            Liste mit Arbeitspaketen, die um alle OAPs des Arbeitspakets
     *            des ersten Aufrufs ergaenzt wird
     */
    private static void addWpToList(Workpackage wp, Set<Workpackage> wps) {
        wps.add(wp);
        if (wp != null && !wp.equals(WpManager.getRootAp())) {
            addWpToList(WpManager.getWorkpackage(wp.getOAPID()), wps);
        }
    }

    /**
     * Berechnet den Aufwand pro Arbeitspaket Wird aufgerufen durch: - addWp() -
     * getAndShowValues() - setChanges()
     *
     * @return Aufwand des Arbeitspakets als Double
     * @throws SQLException
     *             Falls die Abfrage fehlschägt
     */
    public static double calcAC(Workpackage wp) {
        return DBModelManager.getWorkEffortModel().getWorkEffortSum(
                wp.getWpId());
    }

    /**
     * Errechnet den CPI aus gegebenen Werten
     *
     * @param acCost
     * @param etcCost
     * @param bacCost
     * @return
     */
    public static double calcCPI(double acCost, double etcCost, double bacCost) {
        double cpi = 0.0;
        if (acCost + etcCost == 0.) {
            if (bacCost == 0.) {
                cpi = 1.0;
            }
        } else {
            cpi = bacCost / (acCost + etcCost);
        }
        if (cpi > 10.0)
            cpi = 10.0;
        return cpi;
    }

    /**
     * Berechnet die AC Kosten fuer ein Arbeitspaket mithilfe der in der
     * Datenbank zugewiesenen Mitarbeitern
     *
     * @param wp
     *            Arbeitspaket, dessen AC-Kosten berechnet weerden sollen
     * @return AC Kosten
     */
    public static double calcACKosten(final Workpackage wp) {
        double acCosts = 0.;
        List<WorkEffort> efforts =
                DBModelManager.getWorkEffortModel().getWorkEffort(wp.getWpId());
        Employee employee;
        for (WorkEffort effort : efforts) {
            employee =
                    DBModelManager.getEmployeesModel().getEmployee(
                            effort.getFid_emp());
            if (employee != null) {
                acCosts += effort.getEffort() * employee.getDaily_rate();
            }
        }
        return acCosts;
    }

    /**
     * Berechnet den Fertigstellungsgrad eines Arbeitspakets mithilfe der
     * uebergebenen Werte
     *
     * @param bac
     * @param etc
     * @param ac
     * @return Fertigstellung in Prozent (0-100)
     */
    public static int calcPercentComplete(double bac, double etc, double ac) {
        int percentComplete = 0;
        if (bac > 0 && etc == 0) {
            percentComplete = 100;
        } else {
            if (etc > 0 && ac > 0) {
                percentComplete = (int) (ac * 100 / (ac + etc));
            } else {
                percentComplete = 0;
            }
        }
        return percentComplete;
    }

    /**
     * Errechnet den EV aus gegebenen Werten
     *
     * @param bacCost
     *            BAC-Kosten in Euro
     * @param percentComplete
     * @return
     */
    public static double calcEV(double bacCost, int percentComplete) {
        return bacCost * ((double) percentComplete / 100.);
    }

    /**
     * Berechnet den EAC aus gegebenen Werten
     *
     * @param bacCost
     *            BAC Kosten in Euro
     * @param acCost
     *            AC Kosten in Euro
     * @param etcCost
     *            ETC Kosten in Euro
     * @return
     */
    public static double calcEAC(double bacCost, double acCost, double etcCost) {
        double calcEAC = 0.0;
        if (bacCost > 0) {
            calcEAC = acCost + etcCost;
        }
        return calcEAC;
    }

    /**
     * Errechnet den durschnittlichen Tagessatz der Mitarbeiter in der
     * uebergebenen Liste
     *
     * @param workers
     * @return durschnisttlicher Tagesssatz in EUR
     */
    public static double calcTagessatz(List<String> workers) {
        Double wptagessatz = 0.0;

        for (int j = 0; j < workers.size(); j++) {
            String login = workers.get(j);
            Employee emp =
                    DBModelManager.getEmployeesModel().getEmployee(login);
            if (emp != null) {
                wptagessatz += emp.getDaily_rate();
            }
        }
        wptagessatz /= workers.size();
        if (Double.isNaN(wptagessatz)) {
            wptagessatz = 0.0;
        }

        return wptagessatz;
    }

    /**
     * Errechn et den Trend (Alternative zu CPI)
     *
     * @param ev
     *            EV in EUR
     * @param acCost
     *            AC-Kosten in EUR
     * @return
     */
    public static double calcTrend(double ev, double acCost) {
        if (acCost > 0) {
            return ev / acCost;
        } else {
            return 0;
        }
    }

    /**
     * gets the siblings of a workpackage
     *
     * @param brotherWorkpackage the workpackage to return the siblings for
     * @return list of sibling workpackages
     */
    public static ArrayList<Workpackage> getSiblings(Workpackage brotherWorkpackage) {
        ArrayList<Workpackage> siblings = new ArrayList<>();
        int parentId = brotherWorkpackage.getWp().getParentID();

        for (Workpackage workpackage : getAllAp()) {
            if (workpackage.getWp().getParentID() == parentId
                    && workpackage.getWpId() != brotherWorkpackage.getWpId()) {
                siblings.add(workpackage);
            }
        }

        return siblings;
    }

    /**
     * returns all workpackages which are a direct child to the given workpackage
     *
     * @param parentWorkpackage the workpackage you want to find the children for
     * @return list of child workpackages
     */
    public static ArrayList<Workpackage> getDirectChildren(Workpackage parentWorkpackage) {
        ArrayList<Workpackage> children = new ArrayList<>();
        int parentId = parentWorkpackage.getWpId();

        for (Workpackage workpackage : getAllAp()) {
            if (workpackage.getWp().getParentID() == parentId) {
                children.add(workpackage);
            }
        }

        return children;
    }

    /**
     * returns all children of the given workpackage
     *
     * @param parentWorkpackage the workpackage you want to retrieve the children for
     * @return
     */
    public static ArrayList<Workpackage> getAllChildren(Workpackage parentWorkpackage) {
        ArrayList<Workpackage> children = WpManager.getDirectChildren(parentWorkpackage);

        for (Workpackage child : children) {
            if (child.isIstOAP()) {
                children.addAll(WpManager.getAllChildren(child));
            }
        }

        return children;
    }

    /**
     * updates the stringId of a given workpackage in the DB
     *
     * @param wp workpackage to update
     */
    public static boolean updateStringId(Workpackage wp) {
        return WorkpackageService.updateStringId(wp);
    }
}
