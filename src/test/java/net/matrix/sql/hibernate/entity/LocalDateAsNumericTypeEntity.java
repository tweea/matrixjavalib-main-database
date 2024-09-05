/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import net.matrix.sql.hibernate.type.LocalDateAsNumericType;

@Entity
@Table(name = "TEST_LOCAL_DATE_NUMERIC")
public class LocalDateAsNumericTypeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @UuidGenerator
    private String id;

    @Type(value = LocalDateAsNumericType.class, parameters = @Parameter(name = "pattern", value = "yyyyMMdd"))
    private LocalDate value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }
}
