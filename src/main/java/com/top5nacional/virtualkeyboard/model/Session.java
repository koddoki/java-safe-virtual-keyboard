package com.top5nacional.virtualkeyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;
    @Column(name = "keys")
    private String keys;
    @Column(name = "session_token", length = 2048)
    private String sessionToken;
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id", unique = false, nullable = false, updatable = false)
    private User user;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "time_of_creation")
    private long timeOfCreation;

    public Session(){};
    public Session(String keys, String sessionToken, User user, boolean isActive, long timeOfCreation) {
        this.keys = keys;
        this.sessionToken = sessionToken;
        this.user = user;
        this.isActive = isActive;
        this.timeOfCreation = timeOfCreation;
    }

    private static Set<List<Integer>> generateRandomNumberSequences(int amountOfCombinations) {
        Set<List<Integer>> numberSequences = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        while (numberSequences.size() < amountOfCombinations) {
            Collections.shuffle(numbers, new Random());
            numberSequences.add(new ArrayList<>(numbers));
        }

        return numberSequences;
    }

    public static List<String> generateRandomKeys(int amountOfCombinations) {
        Set<List<Integer>> combinations = Session.generateRandomNumberSequences(amountOfCombinations);
        List<String> passwords = new ArrayList<>();

        for(List<Integer> numbers : combinations){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numbers.size(); i += 2) {
                sb.append(numbers.get(i));
                if (i + 1 < numbers.size()) {
                    sb.append(",");
                    sb.append(numbers.get(i + 1));
                    if (i + 2 < numbers.size()) {
                        sb.append(";");
                    }
                }
            }
            passwords.add(sb.toString());
        }
        return passwords;
    }

    public static List<List<Integer>> convertKeysToList(String stringKeys) {
        return Arrays.stream(stringKeys.split(";"))
                .map(innerKey -> Arrays.stream(innerKey.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
