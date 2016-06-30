package de.fhbingen.wbs.functions;

import de.fhbingen.wbs.calendar.Day;
import de.fhbingen.wbs.calendar.TimeCalc;
import de.fhbingen.wbs.dbServices.ValuesService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.AnalyseData;
import de.fhbingen.wbs.dbaccess.data.Baseline;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.globals.Loader;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.wpConflict.ConflictCompat;
import de.fhbingen.wbs.wpOverview.WPOverview;

import java.util.*;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse wird benutzt um die Dauer von Oberarbeitspaketen zuberechen<br/>
 *
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-20
 */
public class CalcOAPBaseline {
    /**
     * Berechnung unter Beruecksichtigung der PV-Berechnung, wenn true dauert
     * die BErechnung deutlich laenger
     */
    private boolean withTime;

    /**
     * Konstruktor
     *
     * @param changedWp
     *            Arbeitspaket, dessen OAP aktualisiert werden muessen
     * @param wpOverview
     *            fuer den Reload wenn fertig
     */
    public CalcOAPBaseline(Workpackage changedWp, WPOverview wpOverview) {
        Workpackage actualOAP = changedWp;
        do {
            Loader.setLoadingText(LocalizedStrings.getStatus().calculateLevel(
                    actualOAP.getlastRelevantIndex()));
            actualOAP = WpManager.getWorkpackage(actualOAP.getOAPID());
            //calculate(actualOAP);
        } while (!actualOAP.equals(WpManager.getRootAp()));
        wpOverview.reload();
    }

    /**
     * Konstruktor
     *
     * @param changedWp
     *            Arbeitspaket, dessen OAP aktualisiert werden muessen
     * @param withTime
     *            falls die Zeiten mit berechnet werden sollen
     * @param calcInitialWp
     *            also calculates the changedWp
     */
    public CalcOAPBaseline(Workpackage changedWp, boolean withTime, boolean calcInitialWp) {
        this.withTime = withTime;
        Workpackage actualOAP = changedWp;

        if (calcInitialWp) {
            calculate(changedWp);
        }

        do {
            Loader.setLoadingText(LocalizedStrings.getStatus().calculateLevel(
                    actualOAP.getlastRelevantIndex()));
            actualOAP = WpManager.getWorkpackage(actualOAP.getOAPID());
            //calculate(actualOAP);
        } while (!actualOAP.equals(WpManager.getRootAp()));
    }

    /**
     * Konstruktor
     *
     * @param wpOverview
     *            fuer den Reload wenn fertig
     */
    public CalcOAPBaseline(WPOverview wpOverview) {
        this(false, wpOverview);
    }

    /**
     * Konstruktor, withTime = false für Dauer, withTime = true für Dauer und PV
     *
     * @param withTime
     *            Berechnung unter Beruecksichtigung der PV-Berechnung, wenn
     *            true dauert die Berechnung deutlich laenger
     * @param wpOverview
     *            fuer den Reload wenn fertig
     */
    public CalcOAPBaseline(boolean withTime, WPOverview wpOverview) {
        this.withTime = withTime;

        Map<Integer, List<Workpackage>> oapLevels = new HashMap<Integer, List<Workpackage>>();


        for (int i = WpManager.getRootAp().getLvlIDs().length; i >= 0; i--) {
            oapLevels.put(i, new ArrayList<Workpackage>());
        }

        //Je Level eine Liste an OAPs/UAPs für dieses Level eintragen
        for (Workpackage actualWp : WpManager.getAllAp()) {
            if (actualWp.isIstOAP()) {
                oapLevels.get(actualWp.getlastRelevantIndex()).add(actualWp);
            }
        }

        //Sollte Text setzen zur Anzeige des aktuell bearbeiteten Levels
        for (int i = WpManager.getRootAp().getLvlIDs().length; i >= 0; i--) {
            for (Workpackage actualWp : oapLevels.get(i)) {
                Loader.setLoadingText(LocalizedStrings.getStatus().calculateLevel(1));
            }
        }

        //wenn True, dann PV ebenfalls neu berechnen
        if (withTime) {
            new TimeCalc();

        }

        for (int i = WpManager.getRootAp().getLvlIDs().length; i >= 0; i--) {
            for(Workpackage oap : oapLevels.get(i)){
                System.out.println("will calc OAP: " + oap.getName());
                calculate(oap);
            }
        }



        wpOverview.reload();
    }

    /**
     * Konstruktor fuer BErechnung mit erstllen einer Baseline
     *
     * @param baselineID
     *            ID einer Baseline, die bereits (ohne Analysedaten) in die
     *            Datenbank eingetragen wurde
     * @param wpOverview
     *            fuer den Reload wenn fertig
     */
    private CalcOAPBaseline(int baselineID, WPOverview wpOverview) {

        Map<Integer, List<Workpackage>> oapLevels =
                new HashMap<Integer, List<Workpackage>>();
        for (int i = WpManager.getRootAp().getLvlIDs().length; i >= 0; i--) {
            oapLevels.put(i, new ArrayList<Workpackage>());
        }
        for (Workpackage actualWp : WpManager.getAllAp()) {
            if (actualWp.isIstOAP() || WpManager.getUAPs(actualWp).isEmpty()) {
                oapLevels.get(actualWp.getlastRelevantIndex()).add(actualWp);
            }
        }
        for (int i = WpManager.getRootAp().getLvlIDs().length; i >= 0; i--) {
            for (Workpackage actualWp : oapLevels.get(i)) {
                Loader.setLoadingText(LocalizedStrings.getStatus()
                        .calculateLevel(i));
                this.writeAnalysis(actualWp, baselineID);
            }
        }
        wpOverview.reload();
    }

    /**
     * Konstruktor fuer eine Baseline
     *
     * @param beschreibung
     *            Beschreibung der Baseline
     * @param wpOverview
     *            fuer den Reload wenn fertig
     */
    public CalcOAPBaseline(String beschreibung, WPOverview wpOverview) {
        this(createBaseline(beschreibung), wpOverview);
    }

    /**
     * Berechnet die Daten eines OAP indem alle seine UAP zusammengerechnet
     * werden, falls diese ebenfalls OAP sind muessen sie natuerlich bereits
     * vorher bereechnet worden sein
     *
     * @param oap
     *            aktuelles OAP
     */
    public void calculate(Workpackage oap) {
        if (oap.isIstOAP()) {
            //Wenn OAP, dann berechne neue Werte aus dessen UAPs
            double bac = 0.0;
            double ac = 0.0;
            double etc = 0.0;
            double ev = 0.0;
            double eac = 0.0;
            double cpi = 0.0;
            double bacCost = 0.0;
            double acCost = 0.0;
            double etcCost = 0.0;

            Set<Employee> workers = new HashSet<Employee>();

            Set<Workpackage> uaps = WpManager.getUAPs(oap);


            Date minDate = oap.getStartDateHope();
            Date maxDate = oap.getEndDateHope();

            boolean init = false;

            for(Workpackage uap : uaps) {
                if(!init) {
                    maxDate = uap.getEndDateCalc();
                    minDate = uap.getStartDateCalc();
                    init = true;
                }
                if(uap.getEndDateCalc().after(maxDate)) {
                    maxDate = uap.getEndDateCalc();
                }
                if(uap.getStartDateCalc().before(minDate)) {
                    minDate = uap.getStartDateCalc();
                }
            }
            oap.setStartDateCalc(minDate);
            oap.setEndDateCalc(maxDate);
            Map<Day, Double> oapPvs = getFilledPvMap(minDate, maxDate);
            int i = 0;
            for (Workpackage actualUAP : uaps) {
                workers.addAll(actualUAP.getWorkers());

                if (actualUAP.isIstInaktiv()) {
                    ac += actualUAP.getAc();
                    acCost += actualUAP.getAc_kosten();
                } else {
                    bac += actualUAP.getBac();
                    ac += actualUAP.getAc();
                    etc += actualUAP.getEtc();
                    ev += actualUAP.getEv();
                    eac += actualUAP.getEac();
                    bacCost += actualUAP.getBac_kosten();
                    acCost += actualUAP.getAc_kosten();
                    etcCost += actualUAP.getEtc_kosten();

                    
                    if (withTime) {
                        //Holt alle PVs von Start- bis Enddatum für ein UAP
                        Map<Date, Double> uapPVs = ValuesService.getWPPVs(  ValuesService.getNextFriday(actualUAP.getStartDateCalc()).getTime(),
                                                                            ValuesService.getNextFriday(actualUAP.getEndDateCalc()).getTime(),
                                                                            actualUAP.getWpId());

                        List<Date> pvSorted = new ArrayList<Date>(uapPVs.keySet());
                        Collections.sort(pvSorted);

                        //Summiere PVs für das OAP in dessen Map
                        for (Date actualDate : pvSorted) {

                            double oldOAPPV = oapPvs.get(new Day(actualDate));
                            oapPvs.put(new Day(actualDate), uapPVs.get(actualDate) + oldOAPPV);
                            //System.out.println(i+" --- "+actualDate+":   "+uapPVs.get(actualDate).intValue());
                        }
                        i++;
                        //Summiere nach Paketende dessen PV (=BAC) auf die nachfolgenden Datumswerte
                        double lastPv = actualUAP.getBac_kosten();
                        Date end = actualUAP.getEndDateCalc();
                        for(Date date : oapPvs.keySet()) {
                            if(date.after(end)) {
                                double oldValue = oapPvs.get(new Day(date));
                                oapPvs.put(new Day(date), oldValue + lastPv);

                            }
                        }
                    }
                }
            }



            //Neu berechnete PVs mit Dauer in DB schreiben
            if(withTime) {
                for(Date actualDate : oapPvs.keySet()) {
                    ValuesService.savePv(oap.getWpId(), new Day(actualDate), oapPvs.get(new Day(actualDate)));
                }
            }
            cpi = WpManager.calcCPI(acCost, etcCost, bacCost);

            oap.setBac(bac);
            oap.setAc(ac);
            oap.setEtc(etc);
            oap.setEv(ev);
            oap.setEac(eac);
            oap.setcpi(cpi);
            oap.setbac_kosten(bacCost);
            oap.setAc_kosten(acCost);
            oap.setEtc_kosten(etcCost);

            //vorhandene Worker löschen, neue Worker eintragen
            for (Employee actualWorker : oap.getWorkers()) {
                oap.removeWorker(actualWorker);
            }
            for (Employee actualWorker : workers) {
                oap.addWorker(actualWorker);
            }

            WpManager.updateAP(oap);
        }

    }

    private Map<Day, Double> getFilledPvMap(Date min, Date max) {
        Map<Day, Double> pvMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(min);
        do{
            pvMap.put(new Day(calendar.getTime()),0.0);
            calendar.add(Calendar.DATE,1);
        } while(calendar.getTime().before(max));
        pvMap.put(new Day(calendar.getTime()), 0.0);
        return pvMap;
    }

    /**
     * Schreibt einen Eintrag in die Datenbank fuer eine neue Baseline
     *
     * @param description
     *            Beschreibung der Baseline
     * @return Datenbank-ID der Baseline
     */
    private static int createBaseline(String description) {
        int baseID = -1;
        java.util.Date dt = new Date();
        java.sql.Date dte = new java.sql.Date(dt.getTime());
        int highest = 0;

        Baseline newBaseline = new Baseline();
        newBaseline.setBl_date(dte);
        newBaseline.setDescription(description);
        newBaseline.setFid_project(1);
        if (!DBModelManager.getBaselineModel().addNewBaseline(newBaseline)) {
            System.out.println(LocalizedStrings.getErrorMessages()
                    .baselineCreatingError());
            return baseID;
        }
        List<Baseline> baselines =
                DBModelManager.getBaselineModel().getBaseline();
        for (Baseline bl : baselines) {
            int wert = bl.getId();
            if (wert > highest) {
                highest = wert;
            }
        }
        // baseID wird mit der neu generierten Baseline-ID initialisiert
        baseID = highest;

        ConflictCompat.deleteWpMovedConflicts();

        return baseID;
    }

    /**
     * Schreibt Werte zu einem AP in die DB (Analysedaten)
     *
     * @param wp
     *            Arbeitspaket mit aktuellen Werten
     * @param fidBase
     *            ID der Baseline
     */
    private void writeAnalysis(Workpackage wp, int fidBase) {
        AnalyseData data = new AnalyseData();
        data.setFid_wp(wp.getWpId());
        data.setFid_baseline(fidBase);
        data.setName(wp.getName());
        data.setBac(wp.getBac());
        data.setAc(wp.getAc());
        data.setEv(wp.getEv());
        data.setEtc(wp.getEtc());
        data.setEac(wp.getEac());
        data.setCpi(WpManager.calcCPI(wp.getAc_kosten(), wp.getEtc_kosten(),
                wp.getBac_kosten()));
        data.setBac_costs(wp.getBac_kosten());
        data.setAc_costs(wp.getAc_kosten());
        data.setEtc_costs(wp.getEtc_kosten());
        data.setSv(wp.getSv());
        data.setSpi(wp.getSpi());
        data.setPv(wp.getPv());
        DBModelManager.getAnalyseDataModel().addNewAnalyseData(data);

    }

}
