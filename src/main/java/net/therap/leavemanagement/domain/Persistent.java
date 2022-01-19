package net.therap.leavemanagement.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author rumi.dipto
 * @since 11/22/21
 */
@Getter
@Setter
@MappedSuperclass
public class Persistent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Version
    private int version;

    @PrePersist
    private void onCreate() {
        created = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        updated = new Date();
    }

    public boolean isNew() {
        return this.getId() == 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (Objects.isNull(object) || !(object instanceof Persistent)) {
            return false;
        }

        return this.getId() == ((Persistent) object).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
