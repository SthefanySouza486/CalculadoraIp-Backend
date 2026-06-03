package edu.backend.calculadoraip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubnetBlock {

        private String enderecoRede;
        private String primeiroIp;
        private String ultimoIp;
        private String broadcast;

        public SubnetBlock(String enderecoRede, String primeiroIp, String ultimoIp, String broadcast) {
            this.enderecoRede = enderecoRede;
            this.primeiroIp = primeiroIp;
            this.ultimoIp = ultimoIp;
            this.broadcast = broadcast;
        }
}
