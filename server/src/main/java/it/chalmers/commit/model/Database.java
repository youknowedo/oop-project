package it.chalmers.commit.model;

import java.util.function.Function;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * The connection to PostgreSQL. Applies the Flyway migrations on start, then lets Hibernate check
 * that the tables match the entity classes. Entity classes are registered here as they are added.
 */
public class Database implements AutoCloseable {

  private final SessionFactory sessionFactory;

  public Database(String url, String user, String password) {
    Flyway.configure().dataSource(url, user, password).load().migrate();

    sessionFactory =
        new Configuration()
            .setProperty("jakarta.persistence.jdbc.url", url)
            .setProperty("jakarta.persistence.jdbc.user", user)
            .setProperty("jakarta.persistence.jdbc.password", password)
            .setProperty("hibernate.hbm2ddl.auto", "validate")
            .buildSessionFactory();
  }

  /**
   * Runs the work in one transaction and commits it, or rolls it back if the work throws. Changes
   * made to loaded entities are saved automatically on commit.
   */
  <T> T inTransaction(Function<Session, T> work) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      try {
        T result = work.apply(session);
        transaction.commit();
        return result;
      } catch (RuntimeException e) {
        transaction.rollback();
        throw e;
      }
    }
  }

  @Override
  public void close() {
    sessionFactory.close();
  }
}
