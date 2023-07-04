package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class Util {
     //реализуйте настройку соеденения с БД
    private static final String HOST = "jdbc:postgresql://localhost:5432/db-test-sanzhar";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "admin";

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DIALECT = "org.hibernate.dialect.PostgreSQL81Dialect";

    public static Connection getConnection(){
        Connection connection = null;

        try {
            Driver driver = new org.postgresql.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(HOST, LOGIN, PASSWORD);
            //return connection;
            if (!connection.isClosed()){
                System.out.println("Соединение с БД установил");
            }

            if (connection.isClosed()){
                System.out.println("Соединение с БД НЕ УСТАНОВИЛ!!! ");
            }
        } catch (SQLException e) {
            System.out.println("Неудалось загрузить класс драйвера!");
        }
        return connection;
    }

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, HOST);
                settings.put(Environment.USER, LOGIN);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL95Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("owibka");
            }
        }
        return sessionFactory;
    }
}
