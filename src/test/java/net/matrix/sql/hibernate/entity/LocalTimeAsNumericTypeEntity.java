/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.entity;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import net.matrix.sql.hibernate.type.LocalTimeAsNumericType;

@Entity
@Table(name = "TEST_LOCAL_TIME_NUMERIC")
public class LocalTimeAsNumericTypeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @UuidGenerator
    private String id;

    @Type(value = LocalTimeAsNumericType.class, parameters = @Parameter(name = "pattern", value = "HHmmss"))
    private LocalTime value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getValue() {
        return value;
    }

    public void setValue(LocalTime value) {
        this.value = value;
    }
}
