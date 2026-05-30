package edu.backend.calculadoraip.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetworkInfo {
    private int[] ipOctetos = new int[4];
    private int[] maskOctetos = new int[4];
    private Integer cidr;
}
