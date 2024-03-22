package com.top5nacional.virtualkeyboard.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class SessionTest {

    @Test
    void testNoRepeatedCombinationsInNext1000() {
        int amountOfCombinations = 1000; // Change this according to your requirement

        Set<List<Integer>> existingCombinations = Session.generateCombinations(amountOfCombinations);
        
        assertTrue(existingCombinations.size() == amountOfCombinations, "No repeated combinations found in the next 1000 combinations.");
    }
}
