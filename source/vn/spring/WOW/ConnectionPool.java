/*
 * ConnectionPool.java
 * Dec 27, 2005
 * 
 */
package vn.spring.WOW;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.WowConfig;

/**
 * Class for connection pooling of database connections. 
 * @author Zdenek Velart, modified by Loc Nguyen
 */
public final class ConnectionPool {
    private DataSource dataSource;

    private static ConnectionPool instance;

    private String jdbcDriver;
    private String jdbcUrl;

    private String user;
    private String password;

    private ConnectionPool(String jdbcDriver, String jdbcUrl, String user, String password) {
        this.jdbcDriver = jdbcDriver;
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        try {
            Class.forName(this.jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.dataSource = setUpDataSource();

    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
			WowConfig conf = WOWStatic.config;
			String jdbcDriver=conf.Get("jdbcDriver");
			String jdbcUrl=conf.Get("jdbcUrl");
			String muser=conf.Get("dbuser");
			String mpassword=conf.Get("dbpassword");

			if (conf.Get("dbimpl").equals("db"))
				init(jdbcDriver, jdbcUrl, muser, mpassword);
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
		//System.out.println("@Loc: Get database connection: call ConnectionPool.getConnection()");
        return getInstance().dataSource.getConnection();
    }

    public static void init(String jdbcDriver, String jdbcUrl, String user, String password) {
		System.out.println("@Loc: Call ConnectionPool.init()");
		if(instance==null) {
			System.out.println("@Loc: Init database connection: connection string=" + jdbcUrl + "&user=" + user + 
				", jdbcDriver=" + jdbcDriver);
			instance = new ConnectionPool(jdbcDriver, jdbcUrl, user, password);
		}
    }

    /**
     * Connection creation and test.
     * @return DataSource
     */
    private DataSource setUpDataSource() {
        ObjectPool connectionPool = new GenericObjectPool(null, 45,
                GenericObjectPool.WHEN_EXHAUSTED_BLOCK, -1L,
                GenericObjectPool.DEFAULT_MAX_IDLE, true, true);
        ConnectionFactory cf = new DriverManagerConnectionFactory(jdbcUrl +
                        "?user=" + this.user + "&password=" + 
                        this.password+"&useServerPrepStmts=false", null);
        new PoolableConnectionFactory(cf, connectionPool, null,
                "SELECT 1", false, true);
        return new PoolingDataSource(connectionPool);
    }
}
