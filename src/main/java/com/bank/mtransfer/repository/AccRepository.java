package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.db.Acc;
import org.springframework.data.repository.CrudRepository;

public interface AccRepository extends CrudRepository<Acc, String> {
    Acc findByClidAndAAndValue(String client_id, String a, String value);
    Acc findByValueAndA(String value, String a);

    Acc findByClid(String clid);
}
