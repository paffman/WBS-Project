package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;


/**
 * Interface for login form related translations.
 */
public interface Login {
    @En("Login")
    @De("Benutzername")
    String login();

    @En("Password")
    @De("Passwort")
    String password();

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

}
