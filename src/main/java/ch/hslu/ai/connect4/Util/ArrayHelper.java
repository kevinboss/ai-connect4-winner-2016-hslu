package ch.hslu.ai.connect4.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Boss on 11.11.2016.
 */
public class ArrayHelper {
    public static List<List<Integer>> asdsadpermute(Integer... num) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (int i = 0; i < num.length; i++) {
            List<List<Integer>> current = new ArrayList<>();
            for (List<Integer> l : result) {
                for (int j = 0; j < l.size() + 1; j++) {
                    l.add(j, num[i]);
                    ArrayList<Integer> temp = new ArrayList<>(l);
                    current.add(temp);
                    l.remove(j);
                }
            }

            result = new ArrayList<>(current);
        }

        return result;
    }
}
