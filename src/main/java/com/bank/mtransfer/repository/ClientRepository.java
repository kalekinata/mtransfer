package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.db.Client;
import com.bank.mtransfer.models.payload.UserList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {

    @Query("from Client where name=:name and patronymic=:patronymic and surname=:surname")
    Client findBySurnameAndNameAndPatronymic(String name, String patronymic, String surname);

    @Query(value = "select" +
            "       new com.bank.mtransfer.models.payload.UserList(c.id, c.dadd, u.username," +
            "       c.name, c.patronymic, c.surname, cp.value, u.email, count(t.id))" +
            "       from Client as c" +
            "       left join ClientParam cp on c.id = cp.clid and cp.title = 'region'" +
            "       left join Transaction t on c.id = t.clid" +
            "       left join User u on c.userid = u.id" +
//            "       where c.userid is not null" +
            "       group by c.id, cp.id, u.id" +
            "       order by c.dadd desc")
    Iterable<UserList> findAllClient();

    @Query(value = "select new Client(c.id, c.dadd, c.surname, c.name, c.patronymic, c.userid)" +
            "       from Client c" +
            "       join User u on c.userid = u.id" +
            "       where u.username=:username")
    Client getClient(String username);
}
