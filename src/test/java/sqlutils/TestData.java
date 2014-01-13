package sqlutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;

public class TestData {

    public static final void reloadData(Connection con) {
        URL sqlScriptURL = TestData.class.getResource("test_data.sql");
        String sqlScriptPath = sqlScriptURL.getPath();

        Statement stmt = null;

        try {
            // Initialize object for ScripRunner
            ScriptRunner sr = new ScriptRunner(con, false, false);

            // Give the input file to Reader
            Reader reader = new BufferedReader(
                    new FileReader(sqlScriptPath));

            // Exctute script
            sr.runScript(reader);
        } catch (Exception e) {
            System.err.println("Failed to Execute " + sqlScriptPath
                    + " The error is " + e.getMessage());
        }

    }
}
