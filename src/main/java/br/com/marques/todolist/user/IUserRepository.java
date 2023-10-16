package br.com.marques.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

//interface temos os metodos mas não tera a implementação
//<> -> generetor  
public interface IUserRepository  extends JpaRepository<UserModel,UUID>{
  UserModel findByUsername(String username);
    
}
