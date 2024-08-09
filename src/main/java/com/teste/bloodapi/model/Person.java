package com.teste.bloodapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
public class Person {

    @Id
    private String cpf;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data_nasc;


    private String rg;
    private String nome;
    private String sexo;
    private String mae;
    private String pai;
    private String email;
    private String cep;
    private String endereco;
    private int numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefone_fixo;
    private String celular;
    private double altura;
    private double peso;
    private String tipo_sanguineo;


    public double getBmi() {
        return peso / (altura * altura);
    }

    public boolean canDonate() {return peso > 50;}

}

