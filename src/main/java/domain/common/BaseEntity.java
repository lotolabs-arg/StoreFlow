package domain.common;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all domain entities.
 * Provides a unique identifier (UUID), audit timestamp, and equality contract.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    private String id;

    private LocalDateTime createdAt;

    /**
     * Default constructor.
     * Generates a new random UUID for the entity.
     */
    public BaseEntity() {
        this.id = UUID.randomUUID().toString();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Checks if this entity is equal to another object.
     * Equality is based strictly on the unique ID.
     *
     * @param o the object to compare with
     * @return true if both objects are of the same class and have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Generates a hash code based on the unique ID.
     *
     * @return the hash code of the ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
