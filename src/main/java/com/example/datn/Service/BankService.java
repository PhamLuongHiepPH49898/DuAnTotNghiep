package com.example.datn.Service;

import com.example.datn.DTO.Bank;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class BankService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Bank> getAllBanks() {
        String url = "https://api.vietqr.io/v2/banks";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
        return data.stream()
                .map(item -> {
                    Bank bank = new Bank();
                    bank.setCode((String) item.get("code"));
                    bank.setShortName((String) item.get("shortName"));
                    return bank;
                })
                .toList();
    }
}

