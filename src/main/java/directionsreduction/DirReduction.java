package directionsreduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 24/9/17.
 */
public class DirReduction {

    public static boolean areOpposite(String dir1, String dir2) {
        List<String> dirs = Arrays.asList(dir1, dir2);
        if (dirs.contains("NORTH") && dirs.contains("SOUTH")) {
            return true;
        }
        if (dirs.contains("EAST") && dirs.contains("WEST")) {
            return true;
        }
        return false;
    }

    public static String[] dirReduc(String[] arr) {
        List<String> list = new ArrayList<>(Arrays.asList(arr));
        int index;
        do {
            index = -1;
            for (int i = 0; i < list.size() - 1; i++) {
                if (areOpposite(list.get(i), list.get(i + 1))) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                list.remove(index + 1);
                list.remove(index);
            }
        } while (index != -1);
        return list.toArray(new String[]{});
    }
}
