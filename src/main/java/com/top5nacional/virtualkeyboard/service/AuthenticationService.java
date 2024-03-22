package com.top5nacional.virtualkeyboard.service;

import com.top5nacional.virtualkeyboard.dto.LoginResponseDTO;
import com.top5nacional.virtualkeyboard.model.Role;
import com.top5nacional.virtualkeyboard.model.Session;
import com.top5nacional.virtualkeyboard.model.User;
import com.top5nacional.virtualkeyboard.repository.RoleRepository;
import com.top5nacional.virtualkeyboard.repository.SessionRepository;
import com.top5nacional.virtualkeyboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public User registerUser(String username, String password){
        System.out.println("1");
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("2");
        Role userRole = roleRepository.findByAuthority("USER").get();
        System.out.println("3");
        Set<Role> authorities = new HashSet<>();
        System.out.println("4");
        authorities.add(userRole);
        System.out.println("Chegou nesse ponto");
        return userRepository.save(new User(0, username, encodedPassword, authorities));
    }

    public LoginResponseDTO loginUser(String username, String password){
        User user = userRepository.findByUsername(username).get();

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(user, token);

        } catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }

    public LoginResponseDTO loginUserTest(String username, List<Integer[]> password){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // TODO: Fazer retornar ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT);
        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();

        List<List<Integer>> binaryCombinations = generateBinaryCombinations(password.size());
        List<String> passwordPossibilities = new ArrayList<>();

        for (List<Integer>binaryCombination:binaryCombinations) {
            List<String> stuff = new ArrayList<>();
            for(Integer binaryNumber:binaryCombination){
                System.out.println("rawPassword.get(stuff.size())[binaryNumber] = " + password.get(stuff.size())[binaryNumber].toString());
                stuff.add(password.get(stuff.size())[binaryNumber].toString());
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (String string : stuff){
                stringBuilder.append(string);
            }

            System.out.println("Olha meu número, que pica: " + stringBuilder.toString());

            passwordPossibilities.add(stringBuilder.toString());
        }

        for (String passwordPossibililty : passwordPossibilities){
            try{
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, passwordPossibililty)
                );

                String token = tokenService.generateJwt(auth);

                return new LoginResponseDTO(user, token);

            } catch(AuthenticationException e){
                continue;
            }
        }
        return new LoginResponseDTO(null, "");
    }

    // TODO 1
    public ResponseEntity<Session> startSession(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Session());

        User user = optionalUser.get();
        if (sessionRepository.existsByUserAndIsActive(user, true)) {
            List<Session> sessions = sessionRepository.findByUserAndIsActive(user, true);
            List<Session> updatedSessions = new ArrayList<>();
            for (Session session : sessions){
                session.setActive(false);
                updatedSessions.add(session);
            }
            sessionRepository.saveAll(sessions);
        }





        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Session());
    }

    // TODO 2
    public ResponseEntity<Session> getKeyboard(String username){



        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Session());
    }

    protected static List<List<Integer>> generateBinaryCombinations(int n) {
        List<List<Integer>> result = new ArrayList<>();
        generatePasswordPossibilities(n, new ArrayList<>(), result);
        return result;
    }

    protected static void generatePasswordPossibilities(int n, List<Integer> current, List<List<Integer>> result) {
        if (n == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i <= 1; i++) {
            current.add(i);
            generatePasswordPossibilities(n - 1, current, result);
            current.removeLast();
        }
    }
}