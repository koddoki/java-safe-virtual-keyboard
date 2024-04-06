package com.top5nacional.virtualkeyboard.service;

import com.top5nacional.virtualkeyboard.dto.LoginResponseDTO;
import com.top5nacional.virtualkeyboard.dto.SessionDTO;
import com.top5nacional.virtualkeyboard.model.Session;
import com.top5nacional.virtualkeyboard.model.User;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AuthenticationService {

    // TODO: Study why field injection is not recommended
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Scheduled(fixedRate = 300000)
    public void deleteOldSessions(){
        List<Session> oldSessions = sessionRepository.findByTimeOfCreationLessThan(System.currentTimeMillis()-300000);
        if (oldSessions.isEmpty())
            return;

        List<Session> updatedSessions = new ArrayList<>();
        for (Session session : oldSessions){
            session.setActive(false);
            updatedSessions.add(session);
        }
        sessionRepository.saveAll(updatedSessions);
    }

    public ResponseEntity<?> loginUser(String username, List<Integer[]> password){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");

        User user = optionalUser.get();

        List<List<Integer>> binaryCombinations = generateBinaryCombinations(password.size());
        List<String> passwordPossibilities = generatePasswordPossibilities(binaryCombinations, password);

        for (String passwordPossibililty : passwordPossibilities){
            try{
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, passwordPossibililty)
                );

                String token = tokenService.generateJwt(auth);

                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(user.getUsername(), token));

            } catch (AuthenticationException e){
                // There is nothing I want to do in here, but since my IDE likes to complain, I'm writing this down =)
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }
    
    public ResponseEntity<?> getSession(String bearerToken){
        String token = bearerToken.substring(7);

        Optional<Session> optionalSession = sessionRepository.findFirstBySessionTokenAndIsActive(token, true);
        if (optionalSession.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session token. Please, log-in again.");

        Session session = optionalSession.get();
        if (!session.isActive())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session token expired.");

        return ResponseEntity.status(HttpStatus.OK).body(new SessionDTO(session));
    }

    public ResponseEntity<?> startSession() {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken("session", "session")
            );

            String token = tokenService.generateJwt(auth);

            String password = Session.generateRandomKey();
            Session session = new Session(password, token);

            sessionRepository.save(session);

            return ResponseEntity.status(HttpStatus.OK).body(new SessionDTO(session));

        } catch(AuthenticationException e) {
            System.out.println("It was not possible to log in with the session user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something happened!");
        }
    }
    private static List<String> generatePasswordPossibilities(List<List<Integer>> binaryCombinations, List<Integer[]> password){
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

        return passwordPossibilities;
    }

    private static List<List<Integer>> generateBinaryCombinations(int n) {
        List<List<Integer>> result = new ArrayList<>();
        generateBinaryCombinations(n, new ArrayList<>(), result);
        return result;
    }

    private static void generateBinaryCombinations(int n, List<Integer> current, List<List<Integer>> result) {
        if (n == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i <= 1; i++) {
            current.add(i);
            generateBinaryCombinations(n - 1, current, result);
            current.removeLast();
        }
    }
}