package edu.backend.calculadoraip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter@AllArgsConstructor
public class IpResponse {
    private String ipInformado;
    private String mascaraSubRede;
    private String prefixoCidr;
    private String enderecoRede;
    private String enderecoBroadcast;
    private String primeiroIpValido;
    private String ultimoIpValido;
    private Long quantidadeHosts;
    private String classeIp;
}
