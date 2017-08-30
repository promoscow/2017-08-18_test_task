package repository.jdbc;

import repository.Repository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository class working with DataBase.
 * Created by promoscow on 18.08.17.
 */
public class JdbcRepositoryImpl implements Repository {

    private static final String INSERT = "INSERT INTO test (field) VALUES";
    private static final String DELETE = "DELETE FROM test";
    private static final String GET_ALL = "SELECT * FROM test";
    private static Logger logger = Logger.getLogger(JdbcRepositoryImpl.class.getName());

    public List<Integer> getAll(String url, String user, String password) {
        List<Integer> result = new ArrayList<>();
        ResultSet resultSet = null;

        try (Statement statement = DriverManager.getConnection(url, user, password).createStatement()) {

            resultSet = statement.executeQuery(GET_ALL);
            Class.forName("org.postgresql.Driver");
            while (resultSet.next()) {
                result.add(resultSet.getInt("FIELD"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, null, e);
                }
            }
        }
        logger.log(Level.INFO, "GET RESULTS FROM DATABASE: " + result.size());
        return result;
    }

    @Override
    public void init(String url, String user, String password) {
        try (Statement statement = DriverManager.getConnection(url, user, password).createStatement()) {
            statement.execute("DROP TABLE IF EXISTS TEST; " +
                    "DROP SEQUENCE IF EXISTS global_seq ;" +
                    "CREATE SEQUENCE global_seq START 100000; " +
                    "CREATE TABLE TEST" +
                    "(FIELD INTEGER NOT NULL);");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
        logger.log(Level.INFO, "DATABASE CREATED.");
    }

    @Override
    public void populate(int range, String url, String user, String password) {

        try (Statement statement = DriverManager.getConnection(url, user, password).createStatement()) {
            statement.execute(DELETE);

            for (int i = 1; i < range + 1; i++) {
                statement.addBatch(INSERT + " (" + i + ");");
            }
            statement.executeBatch();

//            StringBuilder builder = new StringBuilder();
//            builder.append(INSERT);
//            for (int i = 1; i < range + 1; i++) {
//                builder.append(" (");
//                builder.append(i);
//                builder.append(")");
//                builder.append(i < range ? "," : ";");
//            }
//            statement.execute(builder.toString());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
        logger.log(Level.INFO, "POPULATED: OK");
    }
}
