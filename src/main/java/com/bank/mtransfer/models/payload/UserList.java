package com.bank.mtransfer.models.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserList {
    private String id;
    private Date dadd;
    @NotBlank
    @Size(max=20)
    private String username;
    private String name;
    private String patronymic;
    private String surname;
    private String value;
    @NotBlank
    @Size(max=50)
    private String email;
    private Long transactions;

    public UserList() { }

    public UserList(String id, Date dadd,
                    String username, String name,
                    String patronymic, String surname,
                    String value, String email,
                    Long transactions) {
        this.id = id;
        this.dadd = dadd;
        this.username = username;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.value = value;
        this.email = email;
        this.transactions = transactions;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Date getDadd() { return dadd; }

    public void setDadd(Date dadd) { this.dadd = dadd; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPatronymic() { return patronymic; }

    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Long getTransactions() { return transactions; }

    public void setTransactions(Long transactions) { this.transactions = transactions; }
}
