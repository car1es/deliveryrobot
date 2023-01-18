package ru.netology.deliveryrobot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int NUMBERS_ROUTES = 1000;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBERS_ROUTES);

        for (int i = 0; i < NUMBERS_ROUTES; i++) {
            MyCallable myCallable = new MyCallable(sizeToFreq);
            executorService.submit(myCallable);
        }

        executorService.shutdown();
        int maxSizeValue = 0;
        int maxSizeKey = 0;
        Set<Integer> keySetMap = sizeToFreq.keySet();
        for (Integer integer : keySetMap) {
            if (sizeToFreq.get(integer) > maxSizeValue) {
                maxSizeValue = sizeToFreq.get(integer);
                maxSizeKey = integer;
            }
        }
        sizeToFreq.remove(maxSizeKey, maxSizeValue);

        System.out.println("Самое частое количество повторений " + maxSizeKey + " (встретилось " + maxSizeValue + " раз)");
        System.out.println("Другие размеры:");
        for (Integer integer : sizeToFreq.keySet()) {
            System.out.println("- " + integer + " (" + sizeToFreq.get(integer) + " раз)");
        }

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}

class MyCallable implements Callable<Map<Integer, Integer>> {

    private final Map<Integer, Integer> map;
    private String text;

    public MyCallable(Map<Integer, Integer> map) {
        this.map = map;
    }

    @Override
    public Map<Integer, Integer> call() throws Exception {
        text = Main.generateRoute("RLRFR", 100);
        synchronized (map) {

            int maxValue = 0;
            for (int i = 0; i < text.length(); i++) {
                if ("R".equals(text.substring(i, i + 1))) {
                    maxValue++;
                }
            }
            if (map.containsKey(maxValue)) {
                map.put(maxValue, map.get(maxValue) + 1);
            } else {
                map.put(maxValue, 1);
            }

            return map;
        }
    }
}
