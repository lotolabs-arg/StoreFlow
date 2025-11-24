package infrastructure.persistence.config;

import domain.configuration.GlobalConfig;
import domain.stock.Product;
import domain.users.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Utility class to bootstrap Hibernate SessionFactory.
 * Handles the configuration and initialization of the database connection.
 */
public final class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Builds and provides the Hibernate SessionFactory.
     * If it has already been created, it returns the existing instance.
     *
     * @return the singleton SessionFactory instance
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();

                MetadataSources sources = new MetadataSources(registry);

                /* REGISTER ENTITIES HERE */
                sources.addAnnotatedClass(User.class);
                sources.addAnnotatedClass(Product.class);
                sources.addAnnotatedClass(GlobalConfig.class);

                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (RuntimeException e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                throw new RuntimeException("Failed to initialize Hibernate", e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
