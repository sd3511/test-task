package similarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static List<String> resultList;

    public static void main(String[] args) throws FileNotFoundException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));
        //По условию известно, что сначала будет идти значение количества строк, а затем сами строки(х2)
        //поэтому вытаскиваем с файла в таком порядке без всяких проверок
        try {
            int n = Integer.parseInt(reader.readLine()); //узнаем количество строк
            List<String> list1 = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                list1.add(reader.readLine());           //кладем эти строки в ArrayList
            }
            int m = Integer.parseInt(reader.readLine());
            List<String> list2 = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                list2.add(reader.readLine());
            }
            reader.close();
            resultList = new ArrayList<>();
            searchResult(list1, list2);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }



        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt")))) {
            for (String r : resultList
            ) {
                writer.write(r);            //запись в файл результата
                writer.append("\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


    }

    public static void searchResult(List<String> a, List<String> b) {
        //Метод на вход принимает две коллекции String, ищет максимальный процент похожести между двумя строками
        //Добавляет две максимально похожие строки в результирующий List, удаляет эти строки из текущих коллекций и
        //продолжает процесс рекурсивно пока строки в одной из коллекций не кончатся, т.е. - не с чем будет сравнивать
        //оставшиеся строки
        double max = 0;
        int ii = 0;
        int jj = 0;
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (checkSimilar(a.get(i), b.get(j)) > max) {
                    max = checkSimilar(a.get(i), b.get(j));
                    ii = i;
                    jj = j;
                }
            }
        }
        resultList.add(a.get(ii) + ":" + b.get(jj));
        a.remove(ii);
        b.remove(jj);
        if ((a.size() > 0 && b.size() == 0) || (a.size() == 0 && b.size() > 0)) {
            if (a.size() > 0) {
                for (String s : a) {
                    resultList.add(s + ": ?");
                }
            } else {
                for (String s : b) {
                    resultList.add("? : " + s);
                }
            }
            return;
        }
        searchResult(a, b);

    }

    public static double checkSimilar(String s1, String s2) {
        String longString = s1;
        String shortString = s2;

        if (s1.length() < s2.length()) {
            longString = s2;
            shortString = s1;
        }
        int longLength = longString.length();

        return (longLength - levenstain(longString, shortString)) / (double) longLength;
        //Процент "похожести" одной строки на другую

    }

    public static int levenstain(String str1, String str2) {
        String s1 = str1.toLowerCase().trim();
        String s2 = str2.toLowerCase().trim();
        //Алгоритм Вагнера — Фишера возвращает нам расстояние Левенштейна
        int[] arr1 = new int[s2.length() + 1];
        int[] arr = new int[s2.length() + 1];

        for (int j = 0; j <= s2.length(); j++) {
            arr[j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            System.arraycopy(arr, 0, arr1, 0, arr1.length);
            arr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) != s2.charAt(j - 1)) ? 1 : 0;
                arr[j] = min(
                        arr1[j] + 1,
                        arr[j - 1] + 1,
                        arr1[j - 1] + cost
                );
            }
        }
        return arr[arr.length - 1];
    }

    private static int min(int n1, int n2, int n3) {
        return Math.min(Math.min(n1, n2), n3);
    }

}
