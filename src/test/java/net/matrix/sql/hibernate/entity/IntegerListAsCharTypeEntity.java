/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import net.matrix.sql.hibernate.type.IntegerListAsCharType;

@Entity
@Table(name = "TEST_INTEGER_LIST_CHAR")
public class IntegerListAsCharTypeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @UuidGenerator
    private String id;

    @Type(value = IntegerListAsCharType.class, parameters = @Parameter(name = "separator", value = "#"))
    private List<Integer> value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getValue() {
        return value;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
    }
}
