package similarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
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
        //Метод на вход принимает две коллекции String, ищет длину самой длинной общей последовательности между двумя строками
        //Добавляет две строки с максимальной длиной общей последовательности в результирующий List, удаляет эти строки из текущих коллекций и
        //продолжает процесс рекурсивно пока строки в одной из коллекций не кончатся, т.е. - не с чем будет сравнивать
        //оставшиеся строки
        double max = 0;
        int ii = 0;
        int jj = 0;
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (searchMaxSubstring(a.get(i), b.get(j)).length() > max) {
                    max = searchMaxSubstring(a.get(i), b.get(j)).length();
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

    public static String searchMaxSubstring(String s1, String s2) {
        //Алгоритм поиска самой длинной подпоследовательности (неэффективен O(m*n))
        s1 = s1.toLowerCase().trim();
        s2 = s2.toLowerCase().trim();
        StringBuilder lengthSubstring = new StringBuilder(Math.max(s1.length(), s2.length()));
        int[][] arr = lcsLengthArray(s1, s2);
        int i = s1.length() - 1;
        int j = s2.length() - 1;
        int k = arr[s1.length()][s2.length()] - 1;
        while (k >= 0) {
            if (s1.charAt(i) == s2.charAt(j)) {
                lengthSubstring.append(s1.charAt(i));
                i = i - 1;
                j = j - 1;
                k = k - 1;
            } else if (arr[i + 1][j] < arr[i][j + 1]) {
                i = i - 1;
            } else {
                j = j - 1;
            }
        }
        return lengthSubstring.reverse().toString();
    }

    public static int[][] lcsLengthArray(String s1, String s2) {
        int[][] arr = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                if (i == 0) {
                    arr[i][j] = 0;
                }
                if (j == 0) {
                    arr[i][j] = 0;
                }
                if (s1.charAt(i) == s2.charAt(j)) {
                    arr[i + 1][j + 1] = arr[i][j] + 1;
                } else {
                    arr[i + 1][j + 1] = Math.max(arr[i + 1][j], arr[i][j + 1]);
                }
            }
        }
        return arr;
    }
}
