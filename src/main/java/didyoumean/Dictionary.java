package didyoumean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {

    public static class LevenshteinDistance {
        private int minimum(int a, int b, int c) {
            return Math.min(a, Math.min(b, c));
        }

        public int computeLevenshteinDistance(String str1, String str2) {
            return computeLevenshteinDistance(str1.toCharArray(),
                    str2.toCharArray());
        }

        private int computeLevenshteinDistance(char [] str1, char [] str2) {
            int [][]distance = new int[str1.length+1][str2.length+1];

            for(int i=0;i<=str1.length;i++){
                distance[i][0]=i;
            }
            for(int j=0;j<=str2.length;j++){
                distance[0][j]=j;
            }
            for(int i=1;i<=str1.length;i++){
                for(int j=1;j<=str2.length;j++){
                    distance[i][j]= minimum(distance[i-1][j]+1,
                            distance[i][j-1]+1,
                            distance[i-1][j-1]+
                                    ((str1[i-1]==str2[j-1])?0:1));
                }
            }
            return distance[str1.length][str2.length];
        }
    }

    private final String[] words;

    public Dictionary(String[] words) {
        this.words = words;
    }

    public String findMostSimilar(String to) {
        Map<String, Integer> distances = new HashMap<>();
        for (String word : this.words) {
            distances.put(word, new LevenshteinDistance().computeLevenshteinDistance(word, to));
        }
        int min = Integer.MAX_VALUE;
        String result = "";
        for (Map.Entry<String, Integer> w : distances.entrySet()) {
            if (w.getValue() < min) {
                min = w.getValue();
                result = w.getKey();
            }
        }
        return result;
    }
}