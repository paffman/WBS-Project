/*
 * The WBS-­Tool is a project managment tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-­Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dbaccess.data;

public class Employee {
    private int id;
    private String login;
    private String last_name;
    private String first_name;
    private boolean project_leader;
    private String password;
    private double daily_rate;
    private int time_preference;

    public Employee(int id, String login, String last_name, String first_name,
            boolean project_leader, String password, double daily_rate,
            int time_preference) {
        this.id=id;
        this.login=login;
        this.last_name=last_name;
        this.first_name=first_name;
        this.project_leader=project_leader;
        this.password=password;
        this.daily_rate=daily_rate;
        this.time_preference=time_preference;
    }

    public String toString() {
        return id + " " + login + " " + last_name + " " + first_name + " "
                + project_leader + " " + password + " " + daily_rate + " "
                + time_preference;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public boolean isProject_leader() {
        return project_leader;
    }

    public String getPassword() {
        return password;
    }

    public double getDaily_rate() {
        return daily_rate;
    }

    public int getTime_preference() {
        return time_preference;
    }
}
