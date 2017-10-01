package yourorderplease;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by fleax on 24/9/17.
 */
public class Order {
    public static String order(String words) {
        if (words.isEmpty()) {
            return "";
        }

        Map<Integer, List<String>> collect = Arrays.stream(words.split(" ")).collect(Collectors.groupingBy(w -> number(w)));
        List<Integer> integers = new ArrayList<>(collect.keySet());
        Collections.sort(integers);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < integers.size(); i++) {
            sb.append(collect.get(integers.get(i)).get(0));
            if (i < integers.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static int number(String word) {
        Stream<Character> characterStream = word.chars().mapToObj(c -> (char) c).filter(c -> Character.isDigit(c));
        return Character.getNumericValue(characterStream.findFirst().get());
    }
}
