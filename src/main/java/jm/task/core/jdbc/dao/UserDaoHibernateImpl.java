package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        session.beginTransaction();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "lastName VARCHAR(255)," +
                "age INT" +
                ")";
        session.createSQLQuery(createTableSQL).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }



    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS Users").executeUpdate();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User test = new User(name, lastName, age);
        session.saveOrUpdate(test);
        transaction.commit();
    }

    @Override
    public void removeUserById(long id) {
        SessionFactory sessionFactory = Util.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        User user = session.get(User.class, id);
        if (user != null) {
            session.delete(user);
            transaction.commit();
        }
        session.close();

    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "FROM User";
        Query<User> query = session.createQuery(hql, User.class);
        List<User> userList = query.list();
        session.getTransaction().commit();
        session.close();

        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "DELETE FROM User";
        Query q =  session.createQuery(hql);
        int deletedCount = q.executeUpdate();
        session.getTransaction().commit();
        System.out.println("Deleted " + deletedCount + " records from the users table.");
        session.close();
    }
}
