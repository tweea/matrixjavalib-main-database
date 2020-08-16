/*
 * 版权所有 2020 Matrix。
 * 保留所有权利。
 */
package net.matrix.sql.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * 用户信息。
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "UserInfo.findAll", query = "select o from UserInfo o"),
    @NamedQuery(name = "UserInfo.findAll.size", query = "select count(o) from UserInfo o")
})
@Table(name = "TEST_USER")
public class UserInfo
    implements Serializable {
    private static final long serialVersionUID = -1218387201153161009L;

    @Id
    @Column(nullable = false)
    private String yhm;

    private String mm;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate csrq;

    private String xb;

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public LocalDate getCsrq() {
        return csrq;
    }

    public void setCsrq(LocalDate csrq) {
        this.csrq = csrq;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }
}
