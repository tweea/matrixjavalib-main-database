/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TEST_INTEGER_LIST_STRING")
public class IntegerListAsStringTypeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Type(type = "net.matrix.sql.hibernate.type.IntegerListAsStringType", parameters = @Parameter(name = "separator", value = "#"))
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
