/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import net.matrix.sql.hibernate.type.LocalDateTimeAsNumericType;

@Entity
@Table(name = "TEST_LOCAL_DATETIME_NUMERIC")
public class LocalDateTimeAsNumericTypeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @UuidGenerator
    private String id;

    @Type(value = LocalDateTimeAsNumericType.class, parameters = @Parameter(name = "pattern", value = "yyyyMMddHHmmss"))
    private LocalDateTime value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getValue() {
        return value;
    }

    public void setValue(LocalDateTime value) {
        this.value = value;
    }
}
