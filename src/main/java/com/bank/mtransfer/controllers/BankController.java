package com.bank.mtransfer.controllers;

import com.bank.mtransfer.models.ERole;
import com.bank.mtransfer.models.Method;
import com.bank.mtransfer.models.ResponseApi;
import com.bank.mtransfer.models.db.*;
import com.bank.mtransfer.models.payload.TransInfoBank;
import com.bank.mtransfer.models.payload.UserList;
import com.bank.mtransfer.models.payload.request.FraudTranReq;
import com.bank.mtransfer.models.payload.request.PassportRequest;
import com.bank.mtransfer.models.payload.request.SignupRequest;
import com.bank.mtransfer.models.payload.response.ErrorResponse;
import com.bank.mtransfer.models.payload.response.UserInfoResponse;
import com.bank.mtransfer.repository.*;
import com.bank.mtransfer.security.EncryptionUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.UUID.randomUUID;

@Slf4j
@Controller
@RequestMapping("/bank")
public class BankController {

    @Value("${antifraud}")
    String antifraud;

    private final ClientRepository clientRepository;

    private final ClientParamRepository clientParamRepository;

    private final AccRepository accRepository;

    private final TransactionRepository transactionRepository;

    private final FraudTranRepository fraudTranRepository;

    private final UserRepository userRepository;

    public BankController(ClientRepository clientRepository, ClientParamRepository clientParamRepository, AccRepository accRepository, TransactionRepository transactionRepository, FraudTranRepository fraudTranRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.clientParamRepository = clientParamRepository;
        this.accRepository = accRepository;
        this.transactionRepository = transactionRepository;
        this.fraudTranRepository = fraudTranRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/useradd")
    public String registration(){
        return "useradd";
    }

    @PostMapping("/registration")
    public String useradd(@RequestParam(value = "surname") String surname,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "patronymic") String patronymic,
                          @RequestParam(value = "acc") String acc,
                          @RequestParam(value = "bic") String bic,
                          @RequestParam(value = "phone") String phone,
                          @RequestParam(value = "region") String region,
                          @RequestParam(value = "country") String country,
                          @RequestParam(value = "passport") String passport,
                          @RequestParam(value = "username") String username,
                          @RequestParam(value = "email") String email,
                          @RequestParam(value = "password") String password,
                          Model model) throws Exception {

        //Проверка номера карты
        String cardNumber = acc.replaceAll("[ -]+", "");
        if (!cardNumber.matches("\\d+")
                || cardNumber.length() < 13 || cardNumber.length() == 14
                || cardNumber.length() > 19){
            model.addAttribute("errorCard", "Некорректный номер карты");
            model.addAttribute("styleCard", "color: red;");
        } else {
            int sum = 0;
            boolean alternate = false;
            for (int i = cardNumber.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(cardNumber.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > 9) { n = (n % 10) + 1; }
                }
                sum += n;
                alternate = !alternate;
            }
            if (sum % 10 != 0 && !model.containsAttribute("errorCard")) {
                model.addAttribute("errorCard", "Передан номер недействительной карты");
                model.addAttribute("styleCard", "color: red;");
            }
        }

        if(bic.length() != 9){
            model.addAttribute("errorBIC", "Некорректный БИК");
            model.addAttribute("styleBIC", "color: red;");
        }

        if (!phone.substring(1, 11).matches("\\d+") || phone.length() != 12 || !phone.startsWith("+7")){
            model.addAttribute("errorPhone", "Некорректный номер телефона");
            model.addAttribute("stylePhone", "color: red;");
        }

        RestTemplate restTemplate = new RestTemplate();
        String json = "["+passport+"]";
        ResponseEntity<String> request_pass = restTemplate.postForEntity("http://localhost:8084/api/auth/password", json, String.class);
        String info_pass = Objects.requireNonNull(request_pass.getBody()).replaceAll("\\[", "").replaceAll("]","");
        Gson gson = new Gson();
        PassportRequest valid = gson.fromJson(info_pass, PassportRequest.class);

        log.warn("Код проверки паспортных данных: "+valid.getQc());

        if (valid.getQc() != 0){
            model.addAttribute("errorPassport", "Некорректные данные паспорта");
            model.addAttribute("stylePass", "color: red;");
        }

        User userdata = userRepository.loginUser(username);
        if(userdata != null){
            model.addAttribute("errorUsername", "Логин используется в системе");
            model.addAttribute("styleUser", "color: red;");
        }

        model.addAttribute("surname", surname);
        model.addAttribute("name", name);
        model.addAttribute("patronymic", patronymic);
        model.addAttribute("acc", acc);
        model.addAttribute("bic", bic);
        model.addAttribute("phone", phone);
        model.addAttribute("region", region);
        model.addAttribute("country", country);
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        if(!model.containsAttribute("errorCard") &&
                !model.containsAttribute("errorBIC") &&
                !model.containsAttribute("errorPhone") &&
                !model.containsAttribute("errorPassport")&&
                !model.containsAttribute("errorUsername")){

            SignupRequest regData = new SignupRequest();
            regData.setUsername(username);
            regData.setEmail(email);
            regData.setPassword(password);
            Set<String> role = Collections.singleton(String.valueOf(ERole.ROLE_USER));
            regData.setRole(role);

            ResponseEntity<String> signup_result = restTemplate.postForEntity("http://localhost:8084/api/auth/signup", regData, String.class);

            UserInfoResponse result = gson.fromJson(signup_result.getBody(), UserInfoResponse.class);
            log.warn(result.getId()+" signup_result");

            String clid = String.valueOf(randomUUID());

            Client client = new Client(clid, new Date(),surname, name, patronymic, result.getId());
            clientRepository.save(client);

            ClientParam param_phone = new ClientParam(new Date(),clid,"phone",phone);
            clientParamRepository.save(param_phone);

            ClientParam param_region = new ClientParam(new Date(),clid,"region",region);
            clientParamRepository.save(param_region);

            ClientParam param_pass = new ClientParam(new Date(),clid,"passport", EncryptionUtils.encrypt(passport));
            clientParamRepository.save(param_pass);

            ClientParam param_bic = new ClientParam(new Date(),clid,"bic",bic);
            clientParamRepository.save(param_bic);

            ClientParam param_country = new ClientParam(new Date(), clid, "country", country);
            clientParamRepository.save(param_country);

            Acc card = new Acc(String.valueOf(randomUUID()), new Date(), clid, "DEF", EncryptionUtils.encrypt(acc), "A");
            accRepository.save(card);

            model.addAttribute("msg","Клиент успешно создан");
            model.addAttribute("styleMsg","display:block;");
            return "useradd";

        }

        return "useradd";
    }

    @GetMapping("/users")
    public String userList(Model model){
        Iterable<UserList> userLists = clientRepository.findAllClient();

        String jsTrans = new Gson().toJson(userLists);
        System.out.println(jsTrans);

        model.addAttribute("userList", userLists);
        return "userList";
    }

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable(value = "id") String id, Model model){
        if (!clientRepository.existsById(id)){
            log.warn("Не найден клиента: "+id);
            return "redirect:/";
        }

        model.addAttribute("id", id);

        Optional<Client> client = clientRepository.findById(id);
        ArrayList<Client> result = new ArrayList<>();
        client.ifPresent(result::add);
        model.addAttribute("client",result);

        Iterable<ClientParam> clientParams = clientParamRepository.findByClid(id);
        model.addAttribute("clParams",clientParams);

        Iterable<Transaction> transactionList = transactionRepository.findAllClTr(id);
        String jsList = new Gson().toJson(transactionList);
        System.out.println(jsList);

        model.addAttribute("tranList",transactionList);

        Iterable<FraudTran> fraudTrans = fraudTranRepository.findAllClTr(id);
        String jsFraud = new Gson().toJson(fraudTrans);
        System.out.println(jsFraud);

        model.addAttribute("fraud",fraudTrans);

        Float avgSum = transactionRepository.avgByClid(id);
        model.addAttribute("avgSum", avgSum);
        System.out.println("Средняя сумма транзакций: "+avgSum);

        Long countTran = fraudTranRepository.countByClid(id);
        System.out.println("Общее количество транзакций: "+countTran);

        Float grade = transactionRepository.gradeByClid(countTran, id);
        model.addAttribute("grade", grade);
        System.out.println("Оценка клиента: "+grade);

        return "statistics";
    }

    @GetMapping("/transaction")
    public String tranList(Model model){
        Iterable<TransInfoBank> transactions = transactionRepository.findAllTran();
        model.addAttribute("transactions",transactions);
        return "tranInfoBank";
    }

    @GetMapping("/transaction/{id}")
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

        Optional<FraudTran> fraudTran = fraudTranRepository.findByTrans(id);
        ArrayList<FraudTran> resFraud = new ArrayList<>();
        fraudTran.ifPresent(resFraud::add);
        model.addAttribute("fraudTrans", resFraud);

        String clid = accRepository.findById(
                transactionRepository.findById(id).get().getAccid_send()).get().getClid();

        model.addAttribute("id", clid);

        Iterable<Transaction> transaction_list = transactionRepository.findAllClTr(clid);

        String json = new Gson().toJson(transaction_list);
        System.out.println(json);

        model.addAttribute("tranList",transaction_list);

        Iterable<FraudTran> fraudTrans = fraudTranRepository.findAllClTr(clid);
        String jsFraud = new Gson().toJson(fraudTrans);

        System.out.println(jsFraud);
        model.addAttribute("fraud",fraudTrans);

        Float avgSum = transactionRepository.avgByClid(clid);
        model.addAttribute("avgSum", avgSum);
        System.out.println("Средняя сумма транзакций: "+avgSum);

        Long countTran = fraudTranRepository.countByClid(clid);
        System.out.println("Общее количество транзакций: "+countTran);

        Float grade = transactionRepository.gradeByClid(countTran,clid);
        model.addAttribute("grade", grade);
        System.out.println("Оценка клиента: "+grade);

        return "tranDetailsBank";
    }

    @PostMapping("/transaction/{id}")
    public String transactionReq(@PathVariable(value = "id") String id,
                                 @RequestParam(value = "button") String status,
                                 HttpServletRequest request, Model model){
        if(!transactionRepository.existsById(id)){
            return "{\"status\":\"error\"}";
        }
        System.out.println(id);
        System.out.println(status);

        String address = antifraud+"accept";
        ResponseApi responseApi = new ResponseApi();

        FraudTranReq fraudTranReq = new FraudTranReq();
        fraudTranReq.setCheckid(fraudTranRepository.findByTrid(id).getCheckid());
        fraudTranReq.setTrid(id);
        fraudTranReq.setStatus_tr(status);

        int statusFixed = transactionRepository.setFixedTranStatus(id,status);

        System.out.println(statusFixed);

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(fraudTranReq);

        System.out.println(jsonInputString);

        responseApi = responseApi.sendRequest(Method.POST, jsonInputString, address, request);

        ErrorResponse error = new ErrorResponse(responseApi.getCode(),responseApi.getSb().toString());

        if (error.getCode() != 200 || error.getStatus().indexOf("errdesc") != -1){
            model.addAttribute("error", "Ошибка на сервере. Данные о действиях по транзакции не переданы");
        }

        return "redirect:/bank/transaction";
    }
}
