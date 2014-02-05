package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;


/**
 * Interface for login form related translations.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
 */
public interface Login {
    @En("Login")
    @De("Benutzer")
    String login();

    @En("Password")
    @De("Passwort")
    String password();

    @De("Altes Passwort")
    @En("Old password")
    String oldPassword();

    @De("Neues Passwort")
    @En("New password")
    String newPassword();

    @En("Repeat password")
    @De("Passwort wiederholen")
    String repeatPassword();

    @En("Login name")
    @De("Benutzername")
    String loginLong();

    @En("First name")
    @De("Vorname")
    String firstName();

    @En("Surname")
    @De("Nachname")
    String surname();

    @De("Passwort ändern")
    @En("Change password")
    String changePassword();

    @De("Benutzer")
    @En("User")
    String user();

    @De("Neuen Benutzer anlegen")
    @En("Add new user")
    String newUserWindowTitle();

    @De("Projektleiterberechtigung")
    @En("Project managing permission")
    String projectManagerPermission();

}
