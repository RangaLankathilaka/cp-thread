package lk.ijse.dep;

import java.sql.Connection;

public interface MyPool {

    Connection getConnection();

    void releaseConnection(Connection connection);

    void releaseAllConnections();

}
