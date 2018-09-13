package lk.ijse.dep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyPoolImpl implements MyPool{

    private List<Connection> alConnectionPool = new ArrayList<>();
    private List<Connection> alConusmerPool = new ArrayList<>();

    public MyPoolImpl(int initialSize) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < initialSize; i++) {
            try {
                alConnectionPool.add(DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBC","root","mysql"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized Connection getConnection() {
        while (alConnectionPool.isEmpty()){

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Connection connection = alConnectionPool.get(0);
        alConnectionPool.remove(0);
        alConusmerPool.add(connection);
        return connection;
    }

    @Override
    public synchronized void releaseConnection(Connection connection) {
        alConnectionPool.add(connection);
        alConusmerPool.remove(connection);
        notify();
    }

    @Override
    public synchronized void releaseAllConnections() {
        alConusmerPool.forEach(alConnectionPool::add);
        alConusmerPool.removeAll(alConusmerPool);
        notifyAll();
    }
}
