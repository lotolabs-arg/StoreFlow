package infrastructure.persistence;

import application.interfaces.ProductRepository;
import domain.stock.Barcode;
import domain.stock.Product;
import infrastructure.persistence.config.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class SqliteProductRepository implements ProductRepository {

    @Override
    public Product save(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();
            return product;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save product", e);
        }
    }

    @Override
    public Optional<Product> findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Product.class, id));
        }
    }

    @Override
    public Optional<Product> findByBarcode(Barcode barcode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Product p WHERE p.barcode.value = :barcode";
            Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("barcode", barcode.getValue());
            return query.uniqueResultOptional();
        }
    }

    @Override
    public List<Product> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product", Product.class).list();
        }
    }
}
