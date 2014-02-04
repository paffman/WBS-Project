package de.fhbingen.wbs.translation;

import c10n.C10N;

/**
 * Class to access localizations.
 */
public final class LocalizedStrings {
    private static Button button = null;
    private static Database database = null;
    private static ErrorMessages errorMessages = null;
    private static Login login = null;
    private static Messages messages = null;
    private static Project project = null;
    private static ProjectSetup projectSetup = null;
    private static Wbs wbs = null;

    public static Button getButton() {
        if (button == null) {
            button = C10N.get(Button.class);
        }
        return button;
    }

    public static Database getDatabase() {
        if (database == null) {
            database = C10N.get(Database.class);
        }
        return database;
    }

    public static ErrorMessages getErrorMessages() {
        if (errorMessages == null) {
            errorMessages = C10N.get(ErrorMessages.class);
        }
        return errorMessages;
    }

    public static Login getLogin() {
        if (login == null) {
            login = C10N.get(Login.class);
        }
        return login;
    }

    public static Messages getMessages() {
        if (messages == null) {
            messages = C10N.get(Messages.class);
        }
        return messages;
    }

    public static Project getProject() {
        if (project == null) {
            project = C10N.get(Project.class);
        }
        return project;
    }

    public static ProjectSetup getProjectSetup() {
        if (projectSetup == null) {
            projectSetup = C10N.get(ProjectSetup.class);
        }
        return projectSetup;
    }

    public static Wbs getWbs() {
        if (wbs == null) {
            wbs = C10N.get(Wbs.class);
        }
        return wbs;
    }

    private LocalizedStrings() {
        //forbid usage
    }
}

