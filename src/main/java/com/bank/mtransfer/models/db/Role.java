package com.bank.mtransfer.models.db;

import com.bank.mtransfer.models.ERole;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private Date dadd;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role(){}

    public Role(ERole name){ this.name = name; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public ERole getName() { return name; }

    public void setName(ERole name) { this.name = name; }

    public Date getDadd() { return dadd; }

    public void setDadd(Date dadd) { this.dadd = dadd; }
}