package edu.backend.calculadoraip.service;

import edu.backend.calculadoraip.dto.IpRequest;
import edu.backend.calculadoraip.dto.IpResponse;
import edu.backend.calculadoraip.entity.NetworkInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class IpCalculatorService {

    public IpResponse calcularRede(IpRequest request) {
        NetworkInfo info = parseInput(request.getIp(), request.getMaskOrCidr());

        validarMascaraContigua(info.getMaskOctetos());

        int[] networkOctetos = calcularEndereco(info.getIpOctetos(), info.getMaskOctetos(), true);
        int[] broadcastOctetos = calcularEndereco(info.getIpOctetos(), info.getMaskOctetos(), false);

        int[] primeiroIp = networkOctetos.clone();
        primeiroIp[3] += 1;

        int[] ultimoIp = broadcastOctetos.clone();
        ultimoIp[3] -= 1;

        long qtdHosts = (long) Math.pow(2, 32 - info.getCidr()) - 2;
        if (qtdHosts < 0)
            qtdHosts = 0;

        return new IpResponse(
                request.getIp(),
                formatIp(info.getMaskOctetos()),
                "/" + info.getCidr(),
                formatIp(networkOctetos),
                formatIp(broadcastOctetos),
                formatIp(primeiroIp),
                formatIp(ultimoIp),
                qtdHosts,
                determinarClasse(info.getIpOctetos()[0])
        );
    }

    private NetworkInfo parseInput(String ipString, String maskString){
        NetworkInfo info = new NetworkInfo();
        String[] ipPartes = ipString.split("\\.");
        for (int i = 0; i < 4; i++) info.getIpOctetos()[i] = Integer.parseInt(ipPartes[i]);

        if (maskString.startsWith("/")) {
            info.setCidr(Integer.parseInt(maskString.substring(1)));
            info.setMaskOctetos(cidrToMask(info.getCidr()));
        } else {
            String[] maskParts = maskString.split("\\.");
            int[] mask = new int[4];
            for (int i = 0; i < 4; i++) mask[i] = Integer.parseInt(maskParts[i]);
            info.setMaskOctetos(mask);
            info.setCidr(maskToCidr(mask));
        }
        return info;
    }

    private int[] calcularEndereco(int[] ip, int[] mask, boolean isNetwork) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            if (isNetwork) {
                result[i] = ip[i] & mask[i];
            } else {
                result[i] = ip[i] | (~mask[i] & 0xFF);
            }
        }
        return result;
    }

    private int[] cidrToMask(int cidr) {
        long mask = (0xFFFFFFFFL << (32 - cidr)) & 0xFFFFFFFFL;
        return new int[]{
                (int) (mask >> 24), (int) ((mask >> 16) & 0xFF),
                (int) ((mask >> 8) & 0xFF), (int) (mask & 0xFF)
        };
    }

    private int maskToCidr(int[] mask) {
        int cidr = 0;
        for (int octet : mask) {
            String binary = Integer.toBinaryString(octet);
            cidr += (int) binary.chars().filter(ch -> ch == '1').count();
        }
        return cidr;
    }

    private String formatIp(int[] octets) {
        return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];
    }

    private String determinarClasse(int firstOctet) {
        if (firstOctet >= 1 && firstOctet <= 126) return "A";
        if (firstOctet >= 128 && firstOctet <= 191) return "B";
        if (firstOctet >= 192 && firstOctet <= 223) return "C";
        if (firstOctet >= 224 && firstOctet <= 239) return "D (Multicast)";
        return "E (Experimental)";
    }

    private void validarMascaraContigua(int[] mask) {
        long mascaraCompleta = ((long) mask[0] << 24) | (mask[1] << 16) | (mask[2] << 8) | mask[3];

        long invertida = ~mascaraCompleta & 0xFFFFFFFFL;

        if ((invertida & (invertida + 1)) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Máscara Inválida");
        }
    }
}
