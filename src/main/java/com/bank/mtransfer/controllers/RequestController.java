package com.bank.mtransfer.controllers;

import com.bank.mtransfer.models.db.FraudTran;
import com.bank.mtransfer.models.db.Transaction;
import com.bank.mtransfer.models.payload.StatisticUser;
import com.bank.mtransfer.models.payload.response.MessageResponse;
import com.bank.mtransfer.repository.AccRepository;
import com.bank.mtransfer.repository.FraudTranRepository;
import com.bank.mtransfer.repository.TransactionRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
public class RequestController {

    @Value("${dalocation}")
    String dalocation;
    @Value("${dacountry}")
    String dacountry;

    @Value("${token}")
    String token;

    @Autowired
    AccRepository accRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    FraudTranRepository fraudTranRepository;

    @GetMapping("/info_tran")
    public String infoTrans(@RequestParam(value = "clid") String clid, Model model){
        if(clid == null){
            return "{\"errdesc\":\"Не передан обязательный параметр\"}";
        }

        System.out.println(clid);
        List<Transaction> list = transactionRepository.findByClid(clid);

        Gson gson = new Gson();
        String js = gson.toJson(list);
        System.out.println(js);

        return js;
    }

    @PostMapping("/result_check")
    public String resultCheck(@RequestBody FraudTran fT){
        if(fT.getCheckid() == null || fT.getTrid() == null || fT.getStatus_check() == null || fT.getDescription() == null){
            return "{\"errdesc\":\"Не переданы обязательные параметры\"}";
        }

        if(!transactionRepository.existsById(fT.getTrid())){
            return "{\"errdesc\":\"Транзакция отсутствует в системе\"}";
        }

        String json = new Gson().toJson(fT);

        FraudTran fraudTran = new FraudTran(fT.getCheckid(), new Date(), fT.getTrid(), fT.getStatus_check(), fT.getMarker(), fT.getDescription());
        fraudTranRepository.save(fraudTran);

        String jsonStr = new Gson().toJson(fraudTran);

        int statusCheckFixed = transactionRepository.setFixedTranCheck(fT.getTrid(), fT.getStatus_check());

        System.out.println(json);
        System.out.println(jsonStr);
        System.out.println(statusCheckFixed);

        return "{\"status\":\"success\"}";
    }

    @GetMapping("/bank/statistic")
    public Iterable<StatisticUser> getHistogramData(@RequestParam(value = "clid") String clid) {

        Long countTr = fraudTranRepository.countByClid(clid);
        System.out.println("Количество транзакций: " + countTr);

        List<StatisticUser> data = null;
        if (countTr > 0) {
            data = fraudTranRepository.findByCheckid(countTr, clid);
        }
        return data;
    }

    @PostMapping("/location/geo")
    public ResponseEntity<?> geo(@RequestBody String geo){
        try {
            URL url = new URL(dalocation+"api/4_1/rs/geolocate/address");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Token "+token);
            conn.setDoOutput(true);

            byte[] input = geo.getBytes("utf-8");
            conn.getOutputStream().write(input, 0, input.length);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("geo "+response.toString());

            return ResponseEntity.ok()
                    .body(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new MessageResponse(400,"Error: Service not responding"));
        }
    }
}