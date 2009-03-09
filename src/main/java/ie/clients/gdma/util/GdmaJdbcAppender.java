package ie.clients.gdma.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.jdbc.support.JdbcUtils;

public class GdmaJdbcAppender extends org.apache.log4j.AppenderSkeleton implements org.apache.log4j.Appender {

    private static Logger LOG = Logger.getLogger(GdmaJdbcAppender.class);

    public GdmaJdbcAppender() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent
     * )
     */
    public void append(LoggingEvent event) {
        // make suer we don't log ourselves?!!

        if (event.getLoggerName() != null && event.getLoggerName().indexOf("GdmaJdbcAppender") > -1) {
            return;
        }

        Connection con = null;
        PreparedStatement pstmt = null;
        String insertSql = "insert into GDMA2_LOG (LOGGED_AT, LOGLEVEL, LOGGERNAME, MESSAGE) values (?, ?, ?, ?)";

        event.getLevel().toString();
        event.getLoggerName();
        event.getMessage();
        new Date(event.getTimeStamp());

        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/GdmaDB");

            con = ds.getConnection();
            pstmt = con.prepareStatement(insertSql);
            pstmt.setDate(1, new Date(event.getTimeStamp()));
            pstmt.setString(2, event.getLevel().toString());
            pstmt.setString(3, event.getLoggerName());
            pstmt.setString(4, event.getMessage().toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e, e);
            if (e.getCause() != null) {
                LOG.error(e.getCause(), e.getCause());
            }
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error(e, e);
            e.printStackTrace();
        }

        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(con);
    }

    public void close() {

    }

    public boolean requiresLayout() {
        return false;
    }

}
