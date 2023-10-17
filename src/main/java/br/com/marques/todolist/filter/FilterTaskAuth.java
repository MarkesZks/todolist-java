package br.com.marques.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.marques.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // O spring pode gerenciar esse component
public class FilterTaskAuth extends OncePerRequestFilter {
    
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        var servletPath = request.getServletPath();
        //começar com /task para poder alterar pelo usuario
        if (servletPath.startsWith("/tasks/")) {
            // pegar a autenticação (usuario e senha)
            var authorization = request.getHeader("Authorization");
            // Substring ele calcula o tamanho da palava Basic e remover todos os espaços
            // com o TRIM
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.println("Authorization");
            System.out.println(username);
            System.out.println(password);

            // validar usuario
            var user = this.userRepository.findByUsername(username);
            if(user == null) {
                response.sendError(401, "Usuário sem autorização");
            } else {
             // Validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser",user.getId());
                    //request é tudo que esta vindo da nossa requisição
                    //reponse é o que estamos enviando para o usuario
                    filterChain.doFilter(request, response); //passar a informação para o controle
                } else {
                    response.sendError(401);
                }
            }

        }else{
                filterChain.doFilter(request, response);
        }
    }

}
