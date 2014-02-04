package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

import java.util.StringTokenizer;

/**
 * Interface for database related translations.
 */
public interface Database {
    @En("Database")
    @De("Datenbank")
    String database();

    @En("Server address")
    @De("Serveradresse")
    String serverAddress();

    @En("Root username")
    @De("Adminbenutzername")
    String rootLoginName();

    @En("Root password")
    @De("Adminpasswort")
    String rootPassword();

    @De("Datenbankname (wird angelegt)")
    @En("Database name (will be created)")
    String databaseName();
    
    @En("Index-db password")
    @De("Index-DB Passwort")
    String indexPassword();
    
    @En("User")
    @De("Benutzer")    
    String user();
}
