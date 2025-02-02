package gucant.inv.utils;

import java.util.List;

public class StringSimilarity {
    
    /**
     * Calcule la distance de Levenshtein entre deux chaînes
     */
    public static int levenshteinDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        
        int[] costs = new int[s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    /**
     * Vérifie si deux chaînes sont similaires en utilisant la distance de Levenshtein
     * et un seuil de similarité
     */
    public static boolean areSimilar(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        
        // Supprime les espaces multiples et convertit en minuscules
        s1 = s1.replaceAll("\\s+", " ").trim().toLowerCase();
        s2 = s2.replaceAll("\\s+", " ").trim().toLowerCase();
        
        // Si les chaînes sont identiques après nettoyage
        if (s1.equals(s2)) {
            return true;
        }
        
        // Supprime les caractères non alphanumériques
        s1 = s1.replaceAll("[^a-z0-9]", "");
        s2 = s2.replaceAll("[^a-z0-9]", "");
        
        // Si une des chaînes est vide après nettoyage
        if (s1.isEmpty() || s2.isEmpty()) {
            return false;
        }
        
        // Calcule la distance de Levenshtein
        int distance = levenshteinDistance(s1, s2);
        
        // Calcule le seuil basé sur la longueur de la plus longue chaîne
        int maxLength = Math.max(s1.length(), s2.length());
        int threshold = Math.max(1, maxLength / 2); // 50% de similarité
        
        return distance <= threshold;
    }
    
    /**
     * Vérifie si un nom de produit est similaire à un des produits existants
     */
    public static boolean isProductNameSimilar(String newProductName, List<String> existingProducts) {
        return existingProducts.stream()
            .anyMatch(existingName -> areSimilar(newProductName, existingName));
    }
}