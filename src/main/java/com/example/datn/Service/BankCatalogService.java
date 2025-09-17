
package com.example.datn.Service;

import com.example.datn.DTO.Bank;
import com.example.datn.Repository.TaiKhoanNganHangRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BankCatalogService {

    @Value("${vietqr.banks.url:https://api.vietqr.io/v2/banks}")
    private String vietQrUrl;

    private final Object lock = new Object();
    private volatile List<Bank> cached = Collections.emptyList();
    private volatile Instant lastFetch = Instant.EPOCH;

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Bank> getBanks() {
        // Cache 24h
        if (cached.isEmpty() || Instant.now().isAfter(lastFetch.plusSeconds(24 * 3600))) {
            synchronized (lock) {
                if (cached.isEmpty() || Instant.now().isAfter(lastFetch.plusSeconds(24 * 3600))) {
                    cached = fetchOnlineOrFallback();
                    lastFetch = Instant.now();
                }
            }
        }
        return cached;
    }

    private List<Bank> fetchOnlineOrFallback() {
        // RestTemplate với timeout ngắn
        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setConnectTimeout(2000); // 2s
        rf.setReadTimeout(3000);    // 3s
        RestTemplate rt = new RestTemplate(rf);

        try {
            String json = rt.getForObject(vietQrUrl, String.class);
            return parseBanks(json);
        } catch (Exception ex) {
            // Fallback classpath
            try (InputStream in = new ClassPathResource("bank.json").getInputStream()) {
                String json = new String(in.readAllBytes());
                return parseBanks(json);
            } catch (Exception e) {
                // Cuối cùng, trả tối thiểu để UI vẫn chạy
                List<Bank> min = new ArrayList<>();
                min.add(new Bank("mbbank", "MBBank"));
                return min;
            }
        }
    }

    private List<Bank> parseBanks(String json) throws Exception {
        JsonNode root = mapper.readTree(json);
        JsonNode data = root.path("data");
        List<Bank> out = new ArrayList<>();
        if (data.isArray()) {
            for (JsonNode n : data) {
                String code = n.path("code").asText(null);
                String shortName = n.path("shortName").asText(null);
                if (code != null && shortName != null) {
                    out.add(new Bank(code, shortName));
                }
            }
        }
        return out;
    }
}
