package maximumsubarraysum;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Max {

    public static int sequence(int[] arr) {
        if (arr.length == 0) { return 0; }
        List<Map.Entry<int[], Integer>> maximums = new ArrayList<>();
        for (int i = 1; i <= arr.length; i++) {
            maximums.add(subarrays(arr, i).collect(Collectors.toMap(Function.identity(), x -> sum(x))).entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get());
        }
        return maximums.stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
    }

    private static int sum(int[] array) {
        return Arrays.stream(array).sum();
    }

    private static Stream<int[]> subarrays(int[] arr, int size) {
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i <= arr.length - size; i++) {
            int[] subarray = new int[size];
            System.arraycopy(arr, i, subarray, 0, size);
            list.add(subarray);
        }
        return list.stream();
    }
}