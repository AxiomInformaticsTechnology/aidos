package com.skyline.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.h2.tools.DeleteDbFiles;

import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyPart;
import com.skyline.model.SkyPart.Bias;
import com.skyline.model.SkyStructure;

public class H2 {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:./test";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public static void delete() {
        DeleteDbFiles.execute(".", "test", true);
    }

    public static void createTable(SkyStructure<BasicSkyTuple> structure) throws ClassNotFoundException, SQLException {
        Connection connection = connect();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute(craftCreateTableStatement(structure));
            statement.close();
            connection.commit();
        } finally {
            connection.close();
        }
    }

    public static void insertTuples(SkyStructure<BasicSkyTuple> structure, List<BasicSkyTuple> tuples) throws ClassNotFoundException, SQLException {
        Connection connection = connect();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (BasicSkyTuple tuple : tuples) {
                statement.execute(craftInsertStatement(structure, tuple));
            }
            statement.close();
            connection.commit();
        } finally {
            connection.close();
        }
    }

    public static List<BasicSkyTuple> skylineQuery(SkyStructure<BasicSkyTuple> structure) throws ClassNotFoundException, SQLException {
        Connection connection = connect();
        List<BasicSkyTuple> tuples = new ArrayList<BasicSkyTuple>();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(craftSkylineQuery(structure));
            tuples.addAll(getSkylineTuples(structure, rs));
            stmt.close();
            connection.commit();
        } finally {
            connection.close();
        }
        return tuples;
    }

    private static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }

    private static String craftCreateTableStatement(SkyStructure<BasicSkyTuple> structure) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(formatDatabaseName(structure.getName()));
        sb.append("(identifier varchar(255) primary key,");
        for (SkyPart part : structure.getParts()) {
            if (part.getBias().equals(Bias.VALUE)) {
                sb.append(formatColumnName(part.getLabel()) + " int,");
            } else {
                sb.append(formatColumnName(part.getLabel()) + " double,");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    private static String craftInsertStatement(SkyStructure<BasicSkyTuple> structure, BasicSkyTuple tuple) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(formatDatabaseName(structure.getName()));
        sb.append("(identifier,");
        SkyPart[] parts = structure.getParts();
        for (SkyPart part : parts) {
            sb.append(formatColumnName(part.getLabel()) + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        sb.append("VALUES('" + tuple.getIdentifier() + "',");
        Object[] values = tuple.getValues();
        for (int i = 0; i < structure.getDimensions(); i++) {
            SkyPart part = parts[i];
            Object value = values[i];
            if (part.getBias().equals(Bias.VALUE)) {
                sb.append(((double) Arrays.asList(part.getOptions()).indexOf((String) value)));
            } else {
                sb.append(((double) value) + ",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    private static String craftSkylineQuery(SkyStructure<BasicSkyTuple> structure) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(formatDatabaseName(structure.getName()) + " v1 ");
        sb.append("WHERE NOT EXISTS(");
        sb.append("SELECT * FROM ");
        sb.append(formatDatabaseName(structure.getName()) + " v2 ");
        sb.append("WHERE ");
        SkyPart[] parts = structure.getParts();
        for (int i = 0; i < structure.getDimensions(); i++) {
            SkyPart part = parts[i];
            sb.append("v2." + formatColumnName(part.getLabel()));
            if (part.getBias().equals(Bias.MAX)) {
                sb.append(" >= ");
            } else {
                sb.append(" <= ");
            }
            sb.append("v1." + formatColumnName(part.getLabel()));
            sb.append(" AND ");
        }
        sb.append("(");
        for (int i = 0; i < structure.getDimensions(); i++) {
            SkyPart part = parts[i];
            sb.append("v2." + formatColumnName(part.getLabel()));
            if (part.getBias().equals(Bias.MAX)) {
                sb.append(" > ");
            } else {
                sb.append(" < ");
            }
            sb.append("v1." + formatColumnName(part.getLabel()));
            sb.append(" OR ");
        }
        sb.delete(sb.length() - 4, sb.length());
        sb.append("))");
        return sb.toString();
    }

    private static List<BasicSkyTuple> getSkylineTuples(SkyStructure<BasicSkyTuple> structure, ResultSet rs) throws SQLException {
        List<BasicSkyTuple> tuples = new ArrayList<BasicSkyTuple>();
        while (rs.next()) {
            Object[] expectedValues = new Object[structure.getDimensions()];
            String identifier = rs.getString("identifier");
            SkyPart[] parts = structure.getParts();
            for (int i = 0; i < structure.getDimensions(); i++) {
                SkyPart part = parts[i];
                Object[] options = part.getOptions();
                double v = rs.getDouble(H2.formatColumnName(part.getLabel()));
                if (part.getBias().equals(Bias.VALUE)) {
                    expectedValues[i] = options[(int) v];
                } else {
                    expectedValues[i] = v;
                }
            }
            tuples.add(new BasicSkyTuple(expectedValues, identifier));
        }
        return tuples;
    }

    private static String formatDatabaseName(String name) {
        return name.replace(" ", "_").replace("-", "_").trim().toUpperCase();
    }

    private static String formatColumnName(String name) {
        return name.replace(" ", "_").replace("-", "_").trim().toLowerCase();
    }

}
