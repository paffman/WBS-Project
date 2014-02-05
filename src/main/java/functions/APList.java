package functions;

import globals.Workpackage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
import dbServices.WorkpackageService;
import de.fhbingen.wbs.translation.LocalizedStrings;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Liste mit den Arbeitspaketen<br/>
 * Hilfsklasse fuer {@link WpManager}<br/>
 * 
 * @author Marc-Eric Baumgärtner, Michael Anstatt
 * @version 2.0 - 2012-08-02
 */
public class APList {

    private Set<Workpackage> allAP;

    /**
     * Enthaelt Zuordnung Arbeitspaket -> alle Vorgaenger
     */
    private Map<Workpackage, Set<Workpackage>> ancestors;

    /**
     * Enthaelt Zuordnung Arbeitspaket -> alle Nachfolger
     */
    private Map<Workpackage, Set<Workpackage>> followers;

    private HashMap<String, Workpackage> allApMap;
    private HashMap<Integer, String> allApIdToStringIdMap;

    /**
     * Konstruktor, liest Arbeitspakete und Beziehungsstruktur aus Datenbank aus
     */
    protected APList() {
        allApMap = new HashMap<String, Workpackage>();
        allApIdToStringIdMap = new HashMap<Integer, String>();
        allAP = WorkpackageService.getAllAp();
        for (Workpackage actualWp : allAP) {
            allApMap.put(actualWp.getStringID(), actualWp);
            allApIdToStringIdMap
                    .put(actualWp.getWpId(), actualWp.getStringID());
        }
    }

    /**
     * Liefert die Nachfolger (auf JAVA-Ebene)
     * 
     * @param workpackage
     *            Arbeitspaket dessen Nachfolger gewuenscht werden
     * @return Set mit Nachfolgern
     */
    protected Set<Workpackage> getAncestors(Workpackage workpackage) {
        return ancestors.get(workpackage);
    }

    /**
     * Liefert die Vorgaenger (auf JAVA-Ebene)
     * 
     * @param workpackage
     *            Arbeitspaket dessen Vorgaenger gewuenscht werden
     * @return Set mit Vorgaengern
     */
    protected Set<Workpackage> getFollowers(Workpackage workpackage) {
        return followers.get(workpackage);
    }

    /**
     * Vorgaenger aus DB auslesen
     * 
     * @param actualWp
     * @param ancestorIDMap
     * @param workpackage
     *            Arbeitspaket dessen Vorganeger gewuenscht werden
     * @return Set mit Vorgaengern
     */
    private static Set<Workpackage> getDBAncestors(Workpackage actualWp,
            Map<Integer, Set<Integer>> ancestorIDMap) {
        Set<Integer> actualStringAncestors =
                ancestorIDMap.get(actualWp.getWpId());
        Set<Workpackage> actualWPAncestors = new HashSet<Workpackage>();
        if (actualStringAncestors != null) {
            for (Integer actualAncestorID : actualStringAncestors) {
                actualWPAncestors.add(WpManager
                        .getWorkpackage(actualAncestorID));
            }
        }

        return actualWPAncestors;
    }

    /**
     * Nachfolger aus DB auslesen
     * 
     * @param actualWp
     * @param followerIDMap
     * @param workpackage
     *            Arbeitspaket dessen Nachfolger gewuenscht werden
     * @return Set mit Nachfolgern
     */
    private static Set<Workpackage> getDBFollowers(Workpackage actualWp,
            Map<Integer, Set<Integer>> followerIDMap) {
        Set<Integer> actualStringFollowers =
                followerIDMap.get(actualWp.getWpId());
        Set<Workpackage> actualWPFollowers = new HashSet<Workpackage>();
        if (actualStringFollowers != null) {
            for (Integer actualFollowerID : actualStringFollowers) {
                actualWPFollowers.add(WpManager
                        .getWorkpackage(actualFollowerID));
            }
        }

        return actualWPFollowers;
    }

    /**
     * Neues Arbeitspaket anlegen, ohne dass Vorgaenger oder Nachfolger
     * vorhanden sind
     * 
     * @param wp
     *            neues Arbeitspaket
     */
    protected void insertWp(Workpackage wp) {
        ancestors.put(wp, new HashSet<Workpackage>());
        followers.put(wp, new HashSet<Workpackage>());
        allApMap.put(wp.getStringID(), wp);
        allApIdToStringIdMap.put(wp.getWpId(), wp.getStringID());
    }

    /**
     * Prueft ob das OAP von wp Vorgaenger hat
     * 
     * @param wp
     *            Workpackage
     * @return boolean ob OAP Vorgaenger hat
     */
    private boolean OAPHasAncestor(Workpackage wp) {
        boolean oapHasAncestor = false;
        Workpackage actualOAP = WpManager.getWorkpackage(wp.getOAPID());
        while (actualOAP != null && !actualOAP.equals(WpManager.getRootAp())) {
            if (actualOAP.getAncestors() != null
                    && !actualOAP.getAncestors().isEmpty()) {
                oapHasAncestor = true;
            }
            actualOAP = WpManager.getWorkpackage(actualOAP.getOAPID());
        }
        return oapHasAncestor;
    }

    /**
     * Arbeitspaket aktualisieren
     * 
     * @param wp
     *            zu aktualisierendes Arbeitspaket
     */
    protected void updateWp(Workpackage newValues) {
        System.out.println(newValues); // %%
        System.out.println(newValues.getStringID()); // %%
        allApMap.get(newValues.getStringID()).update(newValues);
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
    protected boolean setAncestor(Workpackage ancestor, Workpackage main) {
        Set<Workpackage> keys = ancestors.keySet();
        if (keys.contains(main)) {
            ancestors.get(main).add(ancestor); // probeweise setzen
            if (ancestor.equals(WpManager.getRootAp())
                    || loopDetectAncestor(main)) {
                JOptionPane.showMessageDialog(
                        null,
                        LocalizedStrings.getErrorMessages().successorLoop(
                                ancestor.getStringID()), LocalizedStrings
                                .getErrorMessages().successorLoopTitle(),
                        JOptionPane.ERROR_MESSAGE);
                ancestors.get(main).remove(ancestor);
                return false;
            } else {
                followers.get(ancestor).add(main);
                return true;
            }
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
    protected boolean setFollower(Workpackage follower, Workpackage main) {
        Set<Workpackage> keys = followers.keySet();
        if (keys.contains(main)) {
            followers.get(main).add(follower); // probeweise setzen
            if (follower.equals(WpManager.getRootAp())
                    || loopDetectFollower(main)) {
                JOptionPane.showMessageDialog(
                        null,
                        LocalizedStrings.getErrorMessages().successorLoop(
                                follower.getStringID()), LocalizedStrings
                                .getErrorMessages().successorLoopTitle(),
                        JOptionPane.ERROR_MESSAGE);
                followers.get(main).remove(follower);
                return false;
            } else {
                ancestors.get(follower).add(main);
                return true;
            }
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
     */
    protected void removeAncestor(Workpackage ancestor, Workpackage main) {
        followers.get(ancestor).remove(main);
        ancestors.get(main).remove(ancestor);
    }

    /**
     * Loescht einen Nachfolger
     * 
     * @param follower
     *            zu loeschender Nachfolger
     * @param main
     *            Arbeitspaket dessen Nachfolger geloescht werden soll
     */
    protected void removeFollower(Workpackage follower, Workpackage main) {
        ancestors.get(follower).remove(main);
        followers.get(main).remove(follower);
    }

    /**
     * Findet Schleifen in Nachfolgern
     * 
     * @param ap
     *            Arbeitspaket, das selbst (oder eines seiner OAPs) in seinen
     *            eigenen Nachfolgern nicht erneut vorkommen darf
     * @return true wenn Schleife erkannt wurde
     */
    private boolean loopDetectFollower(Workpackage ap) {
        Set<Workpackage> danger = this.getAllOAP(ap);
        danger.add(ap);
        for (Workpackage nextObj : ap.getFollowers()) {
            if (WpManager.getWorkpackage(ap.getOAPID()).equals(
                    WpManager.getWorkpackage(ap.getOAPID()))) {
                return oldLoopDetectFollower(ap);
            } else {
                return loopDetectFollower(nextObj, danger);
            }

        }
        return false;
    }

    /**
     * @param ap
     *            aktuell untersuchtes AP
     * @param danger
     *            Set mit allen Workpackages die nicht erneut gefunden werden
     *            duerfen
     * @return true wenn Schleife erkannt wurde
     */
    private boolean loopDetectFollower(Workpackage ap, Set<Workpackage> danger) {
        if (danger.contains(ap))
            return true;
        for (Workpackage actualOAP : this.getAllOAP(ap)) {
            if (danger.contains(actualOAP)) {
                return true;
            } else {
                for (Workpackage nextObj : actualOAP.getFollowers()) {
                    return loopDetectFollower(nextObj, danger);
                }
                danger.add(actualOAP);
            }
        }
        danger.add(ap);

        for (Workpackage actualUAP : WpManager.getUAPs(ap)) {
            if (danger.contains(actualUAP)) {
                return true;
            } else {
                for (Workpackage nextObj : actualUAP.getFollowers()) {
                    return loopDetectFollower(nextObj, danger);
                }
                danger.add(actualUAP);
            }
        }
        danger.add(ap);
        for (Workpackage nextObj : ap.getFollowers()) {
            return loopDetectFollower(nextObj, danger);
        }
        return false;
    }

    /**
     * Findet Schleifen in Vorgaengern
     * 
     * @param ap
     *            Arbeitspaket, das selbst (oder eines seiner OAPs) in seinen
     *            eigenen Vorgaengern nicht erneut vorkommen darf
     * @return true wenn Schleife erkannt wurde
     */
    private boolean loopDetectAncestor(Workpackage ap) {
        Set<Workpackage> danger = this.getAllOAP(ap);
        danger.add(ap);
        for (Workpackage nextObj : ap.getAncestors()) {
            if (WpManager.getWorkpackage(ap.getOAPID()).equals(
                    WpManager.getWorkpackage(ap.getOAPID()))) {
                return oldLoopDetectAncestor(ap);
            } else {
                return loopDetectAncestor(nextObj, danger);
            }

        }
        return false;
    }

    /**
     * Findet rekursiv Schleifen in Vorgaengern
     * 
     * @param ap
     *            aktuell untersuchtes AP
     * @param danger
     *            Set mit allen Workpackages die nicht erneut gefunden werden
     *            duerfen
     * @return true wenn Schleife erkannt wurde
     */
    private boolean loopDetectAncestor(Workpackage ap, Set<Workpackage> danger) {
        if (danger.contains(ap))
            return true;
        for (Workpackage actualOAP : this.getAllOAP(ap)) {
            if (danger.contains(actualOAP)) {
                return true;
            } else {
                for (Workpackage nextObj : actualOAP.getAncestors()) {
                    return loopDetectAncestor(nextObj, danger);
                }
                danger.add(actualOAP);
            }
        }
        danger.add(ap);

        for (Workpackage actualUAP : WpManager.getUAPs(ap)) {
            if (danger.contains(actualUAP)) {
                return true;
            } else {
                for (Workpackage nextObj : actualUAP.getAncestors()) {
                    return loopDetectAncestor(nextObj, danger);
                }
                danger.add(actualUAP);
            }
        }
        danger.add(ap);
        for (Workpackage nextObj : ap.getAncestors()) {
            return loopDetectAncestor(nextObj, danger);
        }
        return false;
    }

    /**
     * @param wp
     *            Workpackage von dem aus alle OAPs benoetigt werden
     * @return Set<Workpackage> mit allen OAPs von wp aus
     */
    private Set<Workpackage> getAllOAP(Workpackage wp) {
        Set<Workpackage> allOAP = new HashSet<Workpackage>();
        Workpackage actualOAP = WpManager.getWorkpackage(wp.getOAPID());
        while (!actualOAP.equals(WpManager.getRootAp())) {
            allOAP.add(actualOAP);
            actualOAP = WpManager.getWorkpackage(actualOAP.getOAPID());
        }
        return allOAP;
    }

    /**
     * liefert alle APs der APList
     * 
     * @return Set<Workpackage> mit allen APs
     */
    protected Set<Workpackage> getAllAp() {
        return new HashSet<Workpackage>(allApMap.values());
    }

    /**
     * Loescht ein Komplettes Workpackage und seine Abhaengigkeiten
     * 
     * @param removeWp
     *            Zu loeschendes Workpackage
     */
    protected void removeWp(Workpackage removeWp) {
        ancestors.remove(removeWp);
        followers.remove(removeWp);
        allApMap.remove(removeWp.getStringID());
        allApIdToStringIdMap.remove(removeWp.getWpId());
    }

    /**
     * Liefert alle Arbeitspakete ohne Vorgaenger
     * 
     * @return Set von Workpackage, die keine Vorgaenger haben
     */
    protected Set<Workpackage> getNoAncestorWps() {
        Set<Workpackage> noAncestors = new HashSet<Workpackage>();
        Set<String> allAp = allApMap.keySet();
        for (String actualWp : allAp) {
            if (WpManager.getAncestors(WpManager.getWorkpackage(actualWp))
                    .isEmpty()
                    && !OAPHasAncestor(WpManager.getWorkpackage(actualWp))) {
                noAncestors.add(WpManager.getWorkpackage(actualWp));
            }
        }
        return noAncestors;
    }

    /**
     * @param ID
     *            des benoetigten Workpackage
     * @return Workpackage mit der ID id
     */
    protected Workpackage getWorkpackage(String id) {
        return allApMap.get(id);
    }

    /**
     * @param ID
     *            des benoetigten Workpackage
     * @return Workpackage mit der ID id
     */
    protected Workpackage getWorkpackage(int id) {
        return allApMap.get(allApIdToStringIdMap.get(id));
    }

    /**
     * Liest Vorgaenger und Nachfolger aus der Datenbank Einmalig beim init
     * aufgerufen
     */
    protected void setAncestorsAndFollowers() {
        ancestors = new HashMap<Workpackage, Set<Workpackage>>();
        followers = new HashMap<Workpackage, Set<Workpackage>>();

        Map<Integer, Set<Integer>> ancestorToFollowersIDMap =
                WorkpackageService.getAncestorToFollowersIdMap();
        Map<Integer, Set<Integer>> followerToAncestorsIDMap =
                WorkpackageService.getFollowerToAncestorsIdMap();

        for (Workpackage actualWp : allAP) {
            ancestors.put(actualWp,
                    getDBAncestors(actualWp, followerToAncestorsIDMap));
            followers.put(actualWp,
                    getDBFollowers(actualWp, ancestorToFollowersIDMap));
        }
        allAP = null;
    }

    /**
     * Findet Schleifen in Nachfolgern
     * 
     * @param ap
     *            Arbeitspaket, das selbst (oder eines seiner OAPs) in seinen
     *            eigenen Nachfolgern nicht erneut vorkommen darf
     * @return true wenn Schleife erkannt wurde
     */
    private boolean oldLoopDetectFollower(Workpackage ap) {
        Set<Workpackage> allOAP = new HashSet<Workpackage>();
        Workpackage actualOAP = WpManager.getWorkpackage(ap.getOAPID());
        while (!actualOAP.equals(WpManager.getRootAp())) {
            allOAP.add(actualOAP);
            actualOAP = WpManager.getWorkpackage(actualOAP.getOAPID());
        }
        for (Workpackage nextObj : ap.getFollowers()) {
            for (Workpackage actualOAPloop : allOAP) {
                if (actualOAPloop.equals(nextObj)) {
                    return true;
                }
            }
            if (oldLoopDetectFollower(nextObj, ap, allOAP)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Findet rekursiv Schleifen in Nachfolgern
     * 
     * @param ap
     *            aktuell untersuchtes AP
     * @param checkFor
     *            Arbeitspaket, das selbst (oder eines seiner OAPs) in seinen
     *            eigenen Nachfolgern nicht erneut vorkommen darf
     * @return true wenn Schleife erkannt wurde
     */
    private boolean oldLoopDetectFollower(Workpackage ap, Workpackage checkFor,
            Set<Workpackage> allOAP) {
        if (ap.equals(checkFor)) {
            return true;
        }
        for (Workpackage nextObj : ap.getFollowers()) {
            for (Workpackage actualOAP : allOAP) {
                if (actualOAP.equals(nextObj)) {
                    return true;
                }
            }
            if (oldLoopDetectFollower(nextObj, checkFor, allOAP)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Findet Schleifen in Vorgaengern
     * 
     * @param ap
     *            Arbeitspaket, das selbst (oder eines seiner OAPs) in seinen
     *            eigenen Vorgaengern nicht erneut vorkommen darf
     * @return true wenn Schleife erkannt wurde
     */
    private boolean oldLoopDetectAncestor(Workpackage ap) {
        Set<Workpackage> allOAP = new HashSet<Workpackage>();
        Workpackage actualOAP = WpManager.getWorkpackage(ap.getOAPID());
        while (!actualOAP.equals(WpManager.getRootAp())) {
            allOAP.add(actualOAP);
            actualOAP = WpManager.getWorkpackage(actualOAP.getOAPID());
        }
        for (Workpackage nextObj : ap.getAncestors()) {
            for (Workpackage actualOAPloop : allOAP) {
                if (actualOAPloop.equals(nextObj)) {
                    return true;
                }
            }
            if (oldLoopDetectAncestor(nextObj, ap, allOAP)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Findet rekursiv Schleifen in Vorgaengern
     * 
     * @param ap
     *            aktuell untersuchtes AP
     * @param checkFor
     *            Arbeitspaket, das selbst (oder eines seiner OAPs) in seinen
     *            eigenen Vorgaengern nicht erneut vorkommen darf
     * @return true wenn Schleife erkannt wurde
     */
    private boolean oldLoopDetectAncestor(Workpackage ap, Workpackage checkFor,
            Set<Workpackage> allOAP) {
        if (ap.equals(checkFor)) {
            return true;
        }
        for (Workpackage nextObj : ap.getAncestors()) {
            for (Workpackage actualOAP : allOAP) {
                if (actualOAP.equals(nextObj)) {
                    return true;
                }
            }
            if (oldLoopDetectAncestor(nextObj, checkFor, allOAP)) {
                return true;
            }
        }
        return false;
    }

}
