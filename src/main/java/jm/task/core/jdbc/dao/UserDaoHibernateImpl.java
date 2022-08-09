package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    private Transaction transaction = null;
    private SessionFactory sessionFactory = getSessionFactory();
    private List<User> list = new ArrayList<>();

    @Override
    public void createUsersTable() {

        String SQL = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT not NULL AUTO_INCREMENT, " +
                " name VARCHAR(30) not NULL, " +
                " lastName VARCHAR (30) not NULL, " +
                " age TINYINT not NULL," +
                " PRIMARY KEY (id))";

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(SQL).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            NativeQuery nativeQuery = session.createSQLQuery("INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)");
//            nativeQuery.setParameter(1, name);
//            nativeQuery.setParameter(2, lastName);
//            nativeQuery.setParameter(3, age);
//            nativeQuery.executeUpdate();
//            System.out.println("User с именем – " + name + " добавлен в базу данных");
//            transaction.commit();
            transaction = session.beginTransaction();
            session.save(new User(name,lastName,age));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            NativeQuery nativeQuery = session.createSQLQuery ("DELETE FROM users WHERE id = ?");
            nativeQuery.setParameter(1, id);
            nativeQuery.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            NativeQuery nativeQuery = session.createSQLQuery("SELECT * FROM users")
                    .addEntity(User.class);
            list = nativeQuery.list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DELETE FROM users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }
}
