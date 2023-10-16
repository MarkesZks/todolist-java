package br.com.marques.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data //define os Getters e Setters
@Entity(name = "tb_users") //transforma atributos em colunas na tabela 

public class UserModel {

    @Id // informa que sera a chave primaria 
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true) //O username sera uma coluna com restrição de atributo unico
    private String username;
    private String name;
    private String password;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
   
}
