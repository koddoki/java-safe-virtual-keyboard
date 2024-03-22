package com.top5nacional.virtualkeyboard.service;

import com.top5nacional.virtualkeyboard.dto.LoginResponseDTO;
import com.top5nacional.virtualkeyboard.dto.SessionDTO;
import com.top5nacional.virtualkeyboard.model.Role;
import com.top5nacional.virtualkeyboard.model.Session;
import com.top5nacional.virtualkeyboard.model.User;
import com.top5nacional.virtualkeyboard.repository.RoleRepository;
import com.top5nacional.virtualkeyboard.repository.SessionRepository;
import com.top5nacional.virtualkeyboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(fixedRate = 300000)
    public void deleteOldRows(){
        List<Session> oldSessions = sessionRepository.findByTimeOfCreationLessThan(System.currentTimeMillis()-300000);
        if (oldSessions.isEmpty()) return;

        List<Session> updatedSessions = new ArrayList<>();
        for (Session session : oldSessions){
            session.setActive(false);
            updatedSessions.add(session);
        }
        sessionRepository.saveAll(updatedSessions);
    }
    public User registerUser(String username, String password){
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        return userRepository.save(new User(null, username, encodedPassword, authorities));
    }

    @Deprecated
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

    public ResponseEntity<String> loginUser(String username, List<Integer[]> password){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);

        User user = optionalUser.get();

        List<List<Integer>> binaryCombinations = generateBinaryCombinations(password.size());
        List<String> passwordPossibilities = new ArrayList<>();

        for (List<Integer>binaryCombination:binaryCombinations) {
            List<String> stuff = new ArrayList<>();
            for(Integer binaryNumber:binaryCombination){
                stuff.add(password.get(stuff.size())[binaryNumber].toString());
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (String string : stuff){
                stringBuilder.append(string);
            }

            passwordPossibilities.add(stringBuilder.toString());
        }

        for (String passwordPossibililty : passwordPossibilities){
            try{
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, passwordPossibililty)
                );

                String token = tokenService.generateJwt(auth);

                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(token);

            } catch(AuthenticationException e){
                continue;
            }
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
    }

    public ResponseEntity<SessionDTO> startSession(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);

        User user = optionalUser.get();

        Optional<Session> optionalSession = sessionRepository.findFirstByUserAndIsActive(user, true);
        if (optionalSession.isPresent()){
            Session session = optionalSession.get();
            session.setActive(false);
            sessionRepository.save(session);
            return ResponseEntity.status(HttpStatus.OK).body(new SessionDTO(session));
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("session", "session")
            );

            String token = tokenService.generateJwt(auth);

            List<String> passwords = Session.generateCombinationsPassword(1000);
            List<Session> newSessions = new ArrayList<>();

            for(String password : passwords){
                Session session = new Session(password, token, user, true, System.currentTimeMillis());
                newSessions.add(session);
            }
            sessionRepository.saveAll(newSessions);

        } catch(AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }
        optionalSession = sessionRepository.findFirstByUserAndIsActive(user, true);

        if (optionalSession.isEmpty()){
            System.out.println("Erro fudeu");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }

        Session session = optionalSession.get();
        session.setActive(false);
        sessionRepository.save(session);

        return ResponseEntity.status(HttpStatus.OK).body(new SessionDTO(session));
    }

    @Deprecated
    public ResponseEntity<Session> getKeyboard(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Session());

        User user = optionalUser.get();

        Optional<Session> optionalSession = sessionRepository.findFirstByUserAndIsActive(user, true);

        if (optionalSession.isEmpty()){
            System.out.println("Erro fudeu");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Session());
        }

        Session session = optionalSession.get();
        session.setActive(false);
        sessionRepository.save(session);

        return ResponseEntity.status(HttpStatus.OK).body(session);
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