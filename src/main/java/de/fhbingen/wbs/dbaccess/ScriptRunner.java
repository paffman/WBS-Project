package de.fhbingen.wbs.dbaccess;

/*
 * Changes by git-commit on GitHub.
 * Fixed error that was introduced by blindly parsing the next line as a
 * command if delimiter character was found. This let to issues if a
 * delimiter was set and the next line was a comment,
 * leading to a missing command in the sql script.
 */
/*
 * Added additional null checks when closing the ResultSet and Statements.
 *
 * Thanks to pihug12 and Grzegorz Oledzki at stackoverflow.com
 * http://stackoverflow.com/questions/5332149/jdbc-scriptrunner-java-lang-nullpointerexception?tab=active#tab-top
 */
/*
 * Modified: Use logWriter in print(Object), JavaDoc comments, correct Typo.
 */
/*
 * Modified by Pantelis Sopasakis <chvng@mail.ntua.gr> to take care of DELIMITER statements. This way you
 * can execute scripts that contain some TRIGGER creation code. New version using REGEXPs! Latest
 * modification: Cater for a NullPointerException while parsing. Date: Feb 16, 2011, 11:48 EET
 */
/*
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class from the iBATIS Apache
 * project. Only removed dependency on Resource class and a constructor
 */
/*
 * Copyright 2004 Clinton Begin Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool to run database scripts. This version of the script can be found at
 * https://gist.github.com/joe776/831762/
 */
public class ScriptRunner {

    private static final String DEFAULT_DELIMITER = ";";
    private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";
    private static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

    private final Connection connection;
    private final boolean stopOnError;
    private final boolean autoCommit;
    private PrintWriter logWriter = new PrintWriter(System.out);
    private PrintWriter errorLogWriter = new PrintWriter(System.err);
    private String delimiter = DEFAULT_DELIMITER;
    private boolean fullLineDelimiter = false;
    private boolean isLoggingEnabled;

    /**
     * Default constructor.
     *
     * @param connection
     * @param autoCommit
     * @param stopOnError
     * @param shouldLog if true logging will be enabled and the sql
     *                  statements get written to the set log writer.
     */
    public ScriptRunner(Connection connection, boolean autoCommit,
                        boolean stopOnError, final boolean shouldLog) {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
        isLoggingEnabled = shouldLog;
    }

    /**
     * @param delimiter
     * @param fullLineDelimiter
     */
    public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    /**
     * Setter for logWriter property.
     *
     * @param logWriter
     *        - the new value of the logWriter property
     */
    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    /**
     * Enables or disables logging.
     * @param shouldLog if true enable logging.
     */
    public final void setLoggingIsEnabled(final boolean shouldLog) {
        isLoggingEnabled = shouldLog;
    }
    /**
     * Setter for errorLogWriter property.
     *
     * @param errorLogWriter
     *        - the new value of the errorLogWriter property
     */
    public void setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter).
     *
     * @param reader
     *        - the source of the script
     * @throws java.sql.SQLException
     *         if any SQL errors occur
     * @throws java.io.IOException
     *         if there is an error reading from the Reader
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != autoCommit) {
                    connection.setAutoCommit(autoCommit);
                }
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the connection passed in.
     *
     * @param conn
     *        - the connection to use for the script
     * @param reader
     *        - the source of the script
     * @throws java.sql.SQLException
     *         if any SQL errors occur
     * @throws java.io.IOException
     *         if there is an error reading from the Reader
     */
    private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    println(trimmedLine);
                    continue;
                }
                if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                    continue;
                }
                if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
                    continue;
                }
                if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter())
                        || fullLineDelimiter && trimmedLine.equals(getDelimiter())) {

                    Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
                    Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        setDelimiter(trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(),
                                fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }

                    command.append(trimmedLine.substring(0, trimmedLine.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    Statement statement = conn.createStatement();

                    println(command);

                    boolean hasResults = false;
                    if (stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (SQLException e) {
                            e.fillInStackTrace();
                            printlnError("Error executing: " + command);
                            printlnError(e);
                        }
                    }

                    if (autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    ResultSet rs = statement.getResultSet();
                    if (hasResults && rs != null) {
                        ResultSetMetaData md = rs.getMetaData();
                        int cols = md.getColumnCount();
                        for (int i = 0; i < cols; i++) {
                            String name = md.getColumnLabel(i);
                            print(name + "\t");
                        }
                        println("");
                        while (rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                String value = rs.getString(i);
                                print(value + "\t");
                            }
                            println("");
                        }
                    }

                    command = null;
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (statement != null) {
                            statement.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                } else {
                    Pattern pattern = Pattern.compile(DELIMITER_LINE_REGEX);
                    Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        setDelimiter(trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1].trim(),
                                fullLineDelimiter);
                    } else {
                        command.append(trimmedLine);
                        command.append(" ");
                    }
                }
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } catch (IOException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } finally {
            conn.rollback();
            flush();
        }
    }

    private String getDelimiter() {
        return delimiter;
    }

    private void print(Object o) {
        if (logWriter != null && isLoggingEnabled) {
            logWriter.print(o);
        }
    }

    private void println(Object o) {
        if (logWriter != null && isLoggingEnabled) {
            logWriter.println(o);
        }
    }

    private void printlnError(Object o) {
        if (errorLogWriter != null) {
            errorLogWriter.println(o);
        }
    }

    private void flush() {
        if (logWriter != null) {
            logWriter.flush();
        }
        if (errorLogWriter != null) {
            errorLogWriter.flush();
        }
    }
}

