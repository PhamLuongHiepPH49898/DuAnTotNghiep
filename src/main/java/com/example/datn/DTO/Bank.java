package com.example.datn.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Bank {
    private String code;

    @JsonProperty("shortName")
    private String shortName;

    public Bank() {}
    public Bank(String code, String shortName) {
        this.code = code; this.shortName = shortName;
    }
    public String getCode() { return code; }
    public String getShortName() { return shortName; }
    public void setCode(String code) { this.code = code; }
    public void setShortName(String shortName) { this.shortName = shortName; }
}

