/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpOverview.tabs;

import de.fhbingen.wbs.controller.LoginViewController;
import de.fhbingen.wbs.controller.ProjectSetupAssistant;
import de.fhbingen.wbs.controller.WBSUserViewController;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.jdbcConnection.MySqlConnect;
import de.fhbingen.wbs.timetracker.TimeTrackerConnector;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpWorker.Worker;

/**
 * The functionality of the WorkerPanel.
 */
public class WorkerPanelAction {

    /**
     * Constructor.
     * @param gui
     *            The GUI of the worker panel.
     * @param over
     *            The functionality of the WPOverview.
     */
    WorkerPanelAction(final WorkerPanel gui, final WPOverview over) {
        gui.tblMitarbeiter.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {

                if (e.getClickCount() == 2
                    && gui.tblMitarbeiter.getSelectedRow() >= 0) {
                    String login = gui.tblMitarbeiter.getValueAt(
                        gui.tblMitarbeiter.getSelectedRow(), 0).toString();
                    Employee employee = DBModelManager.getEmployeesModel()
                        .getEmployee(login);
                    if (employee != null) {
                        Worker mit = new Worker(employee.getLogin(),
                            employee.getId(), employee.getFirst_name(),
                            employee.getLast_name(), employee
                                .isProject_leader(), employee
                                .getDaily_rate());
                        new WBSUserViewController(mit, over);
                        try {
                            TimeTrackerConnector tracker = new TimeTrackerConnector(LoginViewController.lastApplicationAddress);
                            tracker.setToken(LoginViewController.tokenLoginUser);
                            Map<String, Object> data = new HashMap<>();
                            data.put("password", "1234");
                            tracker.patch("users/" + ProjectSetupAssistant.getUserID(MySqlConnect.getConnection(),
                                    employee.getLogin()) +"/",
                                    data);
                        } catch(Exception t) {
                        }
                    }
                    over.reload();
                }
            }
        });
    }
}
