package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.db.ClientParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientParamRepository extends CrudRepository<ClientParam, String> {

    @Query(value = "from ClientParam where clid=:clid and title in ('region', 'phone')")
    Iterable<ClientParam> findByClid(String clid);

    @Query(value = "from ClientParam where clid=:clid and title=:value")
    ClientParam findByClidAndTitle(String clid, String value);
}
