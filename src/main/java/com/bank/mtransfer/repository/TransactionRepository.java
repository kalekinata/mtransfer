package com.bank.mtransfer.repository;

import com.bank.mtransfer.models.payload.TransInfoBank;
import com.bank.mtransfer.models.db.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    @Query(value = "select new com.bank.mtransfer.models.payload.TransInfoBank(t.id, t.dadd, t.clid," +
            "           c.id, t.region, t.type, t.sum," +
            "           t.commission, t.status, t.status_check)" +
            "       from Transaction as t" +
            "       join Acc as a on t.accid_recip = a.id" +
            "       join Client as c on a.clid = c.id" +
            "       order by t.dadd desc")
    Iterable<TransInfoBank> findAllTran();


    @Query(value = "select new Transaction (t.id, t.dadd," +
            "       t.clid, t.accid_send, t.accid_recip," +
            "       t.type, t.sum," +
            "       t.commission, t.region," +
            "       t.status)" +
            "       from Transaction as t" +
            "       join FraudTran as ft on t.id = ft.trid" +
            "       where t.clid=:clid" +
            "       order by t.dadd desc")
    Iterable<Transaction> findAllClTr(String clid);

    @Query("from Transaction where clid=:clid")
    List<Transaction> findByClid(String clid);

    @Modifying
    @Transactional
    @Query("update Transaction t set t.status_check=:status_check where t.id=:trid")
    int setFixedTranCheck(String trid, String status_check);

    @Modifying
    @Transactional
    @Query("update Transaction t set t.status=:status where t.id=:trid")
    int setFixedTranStatus(String trid, String status);

    @Query(value = "from Transaction order by dadd desc")
    Iterable<Transaction> findAllSort();

    @Query(value = "select avg(t.sum)" +
            "       from Transaction as t" +
            "       where t.clid=:clid")
    Float avgByClid(String clid);

    @Query(value = "select (count(t.id)*5) /:countTr" +
            "       from Transaction as t" +
            "       join FraudTran as ft on t.id = ft.trid" +
            "       where t.clid=:clid and ft.description = 'green'")
    Float gradeByClid(Long countTr, String clid);
}