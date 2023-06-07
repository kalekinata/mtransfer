package com.bank.mtransfer.controllers;

import com.bank.mtransfer.models.payload.response.location.*;
import com.bank.mtransfer.models.Method;
import com.bank.mtransfer.models.ResponseApi;
import com.bank.mtransfer.models.db.Acc;
import com.bank.mtransfer.models.db.Client;
import com.bank.mtransfer.models.db.ClientParam;
import com.bank.mtransfer.models.db.Transaction;
import com.bank.mtransfer.models.payload.request.TransactionList;
import com.bank.mtransfer.models.payload.response.ErrorResponse;
import com.bank.mtransfer.models.payload.response.TranCheck;
import com.bank.mtransfer.repository.*;
import com.bank.mtransfer.security.jwt.JwtUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.hibernate.id.uuid.Helper.format;

@Slf4j
@Controller
@CrossOrigin("*")
public class TranController {

    @Value("${antifraud}")
    String antifraud;

    public final TransactionRepository transactionRepository;
    public final ClientRepository clientRepository;
    public final AccRepository accRepository;
    public final UserRepository usersRepository;
    public final FraudTranRepository fraudTranRepository;
    public final ClientParamRepository clientParamRepository;

    public final JwtUtils jwtUtils;

    public TranController(TransactionRepository transactionRepository, ClientRepository clientRepository, AccRepository accRepository, UserRepository usersRepository, FraudTranRepository fraudTranRepository, ClientParamRepository clientParamRepository, JwtUtils jwtUtils) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.accRepository = accRepository;
        this.usersRepository = usersRepository;
        this.fraudTranRepository = fraudTranRepository;
        this.clientParamRepository = clientParamRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/home")
    public String main(HttpServletRequest request, Model model) throws UnknownHostException {
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("bank")) {
                    username = jwtUtils.getUserNameFromJwtToken(cookie.getValue());
                    System.out.println("Логин пользователя: " + username);
                }
            }
        }

        model.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/user/transaction")
    public String tranList(HttpServletRequest request, Model model){
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("bank")) {
                    username = jwtUtils.getUserNameFromJwtToken(cookie.getValue());
                    System.out.println("Логин пользователя: " + username);
                }
            }
        }

        Client client = clientRepository.getClient(username);
        System.out.println("Клиент: " + client.getId());

        Iterable<Transaction> transactions = transactionRepository.findAllClTr(client.getId());
        //Iterable<Transaction> transactions = transactionRepository.findAllSort();
        model.addAttribute("transactions",transactions);
        return "tran-list";
    }

    @GetMapping("/user/transaction/{id}")
    public String transactionDetail(@PathVariable(value = "id") String id, Model model){
        if (!transactionRepository.existsById(id)){
            log.warn("Не найдена транзакция: "+id);
            return "redirect:/";
        }

        Optional<Transaction> transactions = transactionRepository.findById(id);
        ArrayList<Transaction> result = new ArrayList<>();
        transactions.ifPresent(result::add);
        model.addAttribute("tran",result);

        Optional<Client> clSend = clientRepository.findById(
                                        accRepository.findById(
                                            transactionRepository.findById(id).get().getAccid_send()).get().getClid());
        ArrayList<Client> resSend = new ArrayList<>();
        clSend.ifPresent(resSend::add);
        model.addAttribute("clSend",resSend);

        Optional<Client> clRecip = clientRepository.findById(
                                        accRepository.findById(
                                            transactionRepository.findById(id).get().getAccid_recip()).get().getClid());
        ArrayList<Client> resRecip = new ArrayList<>();
        clRecip.ifPresent(resRecip::add);
        model.addAttribute("clRecip",resRecip);

        return "transactionUser";
    }

    @PostMapping("/location/updateLocation")
    public String updateLocation(@RequestBody Location location, Model model) {
        System.out.println("test");
        // Обработка координат местоположения
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        System.out.println("test3 "+latitude+" "+longitude);

        String request = "{\"lat\":"+latitude+", \"lon\": "+longitude+", \"count\":\"1\" }";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> geo = restTemplate.postForEntity("http://localhost:8084/location/geo", request, String.class);
        Gson gson = new Gson();
        GeoData geoData = gson.fromJson(geo.getBody(),GeoData.class);

        String region = geoData.getSuggestions().get(0).getData().getRegion_with_type();
        String country = geoData.getSuggestions().get(0).getData().getCountry();

        System.out.println("region: "+geoData.getSuggestions().get(0).getData().getRegion_with_type());
        System.out.println("country: "+geoData.getSuggestions().get(0).getData().getCountry());

        model.addAttribute("region", region);
        model.addAttribute("country", country);
        return "transfer";
    }

    @GetMapping("/user/transfer")
    public String tranForm(Model model){
        return "transfer";
    }

    @PostMapping("/transfer")
    public String tranAdd(@RequestParam String surname, @RequestParam String name,
                          @RequestParam String patronymic, @RequestParam String acc,
                          @RequestParam String bic, @RequestParam String region,
                          @RequestParam String country, @RequestParam float sum,
                          HttpServletRequest request, ModelMap modelMap) throws ParseException {

        modelMap.addAttribute("surname", surname);
        modelMap.addAttribute("name", name);
        modelMap.addAttribute("patronymic", patronymic);
        modelMap.addAttribute("acc", acc);
        modelMap.addAttribute("bic", bic);
        modelMap.addAttribute("region", region);
        modelMap.addAttribute("country", country);
        modelMap.addAttribute("sum", sum);

        String trid = String.valueOf(randomUUID());
        String clid_R, accid_R;

        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("bank")) {
                    username = jwtUtils.getUserNameFromJwtToken(cookie.getValue());
                    System.out.println("Логин пользователя: " + username);
                }
            }
        }

        Client client = clientRepository.getClient(username);
        System.out.println("Клиент: " + client.getId());

        ClientParam regionCl = clientParamRepository.findByClidAndTitle(client.getId(), "region");
        ClientParam countryCl = clientParamRepository.findByClidAndTitle(client.getId(), "country");

        Acc accSend = accRepository.findByClid(client.getId());
        ClientParam clientParam = clientParamRepository.findByClidAndTitle(client.getId(), "bic");


        String cardNumber = acc.replaceAll("[ -]+", "");
        if (!cardNumber.matches("\\d+")
                || cardNumber.length() < 13 || cardNumber.length() == 14
                || cardNumber.length() > 19){
            modelMap.addAttribute("errorCard", "Некорректный номер карты");
            modelMap.addAttribute("styleCard", "color: red;");
        } else {
            int len_acc = 0;
            boolean alternate = false;
            for (int i = cardNumber.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(cardNumber.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > 9) { n = (n % 10) + 1; }
                }
                len_acc += n;
                alternate = !alternate;
            }
            if (len_acc % 10 != 0 && !modelMap.containsAttribute("errorCard")) {
                modelMap.addAttribute("errorCard", "Передан номер недействительной карты");
                modelMap.addAttribute("styleCard", "color: red;");
            }
        }

        if(bic.length() != 9){
            modelMap.addAttribute("errorBIC", "Некорректный БИК");
            modelMap.addAttribute("styleBIC", "color: red;");
        }

        if(clientRepository.findBySurnameAndNameAndPatronymic(name, patronymic,surname) != null){
            clid_R = clientRepository.findBySurnameAndNameAndPatronymic(name,patronymic,surname).getId();
            if (accRepository.findByValueAndA(acc, "A") != null){
                String clid_acc_R = String.valueOf(accRepository.findByValueAndA(acc, "A").getClid());
                if(!clid_acc_R.equals(clid_R)){
                    System.out.println(clid_acc_R);
                    System.out.println(clid_R);
                    String message = "Счёт получателя принадлежит другому клиенту";
                    System.out.println(message);
                    modelMap.addAttribute("errorCard", message);
                    modelMap.addAttribute("styleCard", "color: red;");
                    return "transfer";
                }else {
                    accid_R = accRepository.findByClidAndAAndValue(clid_R, "A",acc).getId();
                }
            }
            else {
                accid_R = String.valueOf(randomUUID());
                Acc acc_Rec = new Acc(accid_R, new Date(), clid_R, "DEF", acc, "A");
                accRepository.save(acc_Rec);
            }
        }else{
            if (accRepository.findByValueAndA(acc, "A") != null){
                String message = "Счёт получателя принадлежит другому клиенту";
                System.out.println(message);
                modelMap.addAttribute("errorCard", message);
                modelMap.addAttribute("styleCard", "color: red;");
                return "transfer";
            }
            else {
                clid_R = String.valueOf(randomUUID());
                Client client_R = new Client(clid_R, new Date(),surname, name, patronymic, null);
                clientRepository.save(client_R);

                accid_R = String.valueOf(randomUUID());
                Acc acc_Rec = new Acc(accid_R, new Date(), clid_R, "DEF", acc, "A");
                accRepository.save(acc_Rec);
            }
        }

        if(sum < 0){
            modelMap.addAttribute("errorSum", "Некорректная сумма транзакции");
            modelMap.addAttribute("styleSum", "color: red;");
        }

        Transaction transaction = new Transaction(trid, new Date(), client.getId(), accSend.getId(), accid_R, "transfer", sum, (float) (sum * 0.01),  region, "create");
        transactionRepository.save(transaction);

        TransactionList list = new TransactionList();
        list.setId(trid);
        list.setRegion(region);
        list.setCountry(country);
        list.setSum(sum);
        System.out.println((sum * 0.01));
        list.setCommission((float) (sum * 0.01));


        TransactionList.ClientS clientS = new TransactionList.ClientS();
        clientS.setClid(client.getId());
        clientS.setSurname(client.getSurname());
        clientS.setName(client.getName());
        clientS.setPatronymic(client.getPatronymic());
        clientS.setRegion(regionCl.getValue());
        clientS.setCountry(countryCl.getValue());

        TransactionList.ClientS.AccS acc_S = new TransactionList.ClientS.AccS();
        acc_S.setAccid(accSend.getId());
        acc_S.setBic(clientParam.getValue());
        clientS.setAccS(acc_S);

        list.setClientS(clientS);

        TransactionList.ClientR clientR = new TransactionList.ClientR();
        clientR.setClid(clid_R);
        clientR.setSurname(surname);
        clientR.setName(name);
        clientR.setPatronymic(patronymic);

        TransactionList.ClientR.AccR acc_R = new TransactionList.ClientR.AccR();
        acc_R.setAccid(accid_R);
        acc_R.setBic(bic);
        clientR.setAccR(acc_R);

        list.setClientR(clientR);

        if (!modelMap.containsAttribute("errorCard") &&
            !modelMap.containsAttribute("errorBIC") &&
            !modelMap.containsAttribute("errorSum")) {
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(list);

            System.out.println(jsonInputString);

            String address = antifraud + "check";

            ResponseApi responseApi = new ResponseApi();

            responseApi = responseApi.sendRequest(Method.POST, jsonInputString, address, request);


            ErrorResponse error = new ErrorResponse(responseApi.getCode(),responseApi.getSb().toString());


            if (error.getCode() != 200){
                System.out.println(error.getCode()+"\n"+ error.getStatus()+" ERROR");
                modelMap.addAttribute("msg","Заявка не отправлена на проверку");
                modelMap.addAttribute("styleMsg","display:block;");
                return "transfer";
            }
            else {
                TranCheck tranCheck = new Gson().fromJson(responseApi.getSb().toString(), TranCheck.class);

                int statusFixed = transactionRepository.setFixedTranStatus(tranCheck.getTrid(), tranCheck.getStatus_tr());
                System.out.println(statusFixed);

                return "redirect:/user/transaction";
            }
        }
        return "transfer";
    }
}
