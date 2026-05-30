package edu.backend.calculadoraip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpRequest {
    private String ip;
    private String maskOrCidr;
}
