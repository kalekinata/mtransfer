package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.db.FraudTran;
import com.bank.mtransfer.models.payload.StatisticUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FraudTranRepository extends CrudRepository<FraudTran, String> {
    FraudTran findByTrid(String trid);

    @Query(value = "from FraudTran where trid=:trid")
    Optional<FraudTran> findByTrans(String trid);

    @Query(value = "select new FraudTran (ft.checkid, ft.dadd," +
            "       ft.trid, ft.status_check, ft.marker, ft.description)" +
            "       from Transaction as t" +
            "       left join FraudTran as ft on t.id = ft.trid" +
            "       where t.clid=:clid and ft.status_check is not null" +
            "       order by t.dadd")
    Iterable<FraudTran> findAllClTr(String clid);

    @Query(value = "select count(t.id)" +
            "       from Transaction as t" +
            "       join FraudTran as ft on t.id = ft.trid" +
            "       where t.clid=:clid")
    Long countByClid(String clid);

    @Query(value = "select new com.bank.mtransfer.models.payload.StatisticUser(ft.description, (count(t.id)*100)/:countTr)" +
            "       from Transaction as t" +
            "       join FraudTran as ft on t.id = ft.trid" +
            "       where t.clid=:clid" +
            "       group by ft.description")
    List<StatisticUser> findByCheckid(Long countTr, String clid);
}
