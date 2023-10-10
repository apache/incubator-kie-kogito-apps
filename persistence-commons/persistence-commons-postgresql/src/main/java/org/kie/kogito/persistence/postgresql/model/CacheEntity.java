package org.kie.kogito.persistence.postgresql.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.kie.kogito.persistence.postgresql.hibernate.JsonBinaryType;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@IdClass(CacheId.class)
@Table(name = "kogito_data_cache", uniqueConstraints = @UniqueConstraint(columnNames = { "name",
        "key" }), indexes = @Index(columnList = "name,key", unique = true))
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CacheEntity {

    @Id
    @Column(nullable = false)
    private String name;

    @Id
    @Column(nullable = false)
    private String key;

    @Type(type = "jsonb")
    @Column(name = "json_value", columnDefinition = "jsonb")
    private ObjectNode value;

    public CacheEntity() {
    }

    public CacheEntity(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ObjectNode getValue() {
        return value;
    }

    public void setValue(ObjectNode value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CacheEntity)) {
            return false;
        }
        CacheEntity that = (CacheEntity) o;
        return getName().equals(that.getName()) && getKey().equals(that.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getKey());
    }

    @Override
    public String toString() {
        return "CacheEntity{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
