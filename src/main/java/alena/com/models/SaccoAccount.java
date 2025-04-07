package alena.com.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "sacco_account", schema = "P1FCHWGBM")
public class SaccoAccount implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TENANTID")
    private String tenantId;
    @Column(name = "TENANTNAME")
    private String tenantName;
    @Column(name = "ACCOUNT")
    private String account;
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "SaccoAccount{" +
                "id=" + id +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
