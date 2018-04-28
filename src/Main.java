import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    Main() {
        try {
            ArrayList<String> list = parseData(readFile("data.txt", Charset.forName("UTF-8")));
            long start, end;
            String[] arr = list.toArray(new String[list.size()]);
            for(int i = 100; i <= list.size(); i += 100) {
                start = System.nanoTime();
                bubbleSort(Arrays.copyOfRange(arr,0, i));
                end = System.nanoTime();
                System.out.println("Testing bubblesort with " + (i) + " values. Time was: " + (end-start)/1000 + "µs");

                start = System.nanoTime();
                mergeSort(Arrays.copyOfRange(arr,0, i));
                end = System.nanoTime();
                printMergeHelper();
                System.out.println("Testing mergesort with " + (i) + " values. Time was: " + (end-start)/1000 + "µs");

                System.out.println();
            }

            /*for(String s : bubbleSort(list.toArray(new String[list.size()]))) {
                System.out.println(s);
            }
            /*for(String s : arr){
                System.out.println(s);
            }*/

        } catch (IOException a) {
            a.printStackTrace();
        }
    }

    ArrayList<String> parseData(String data) {
        // 1000 items max
        // each one a minimum of 3 chars
        ArrayList<String> words = new ArrayList<>();
        //Collections.addAll(words, new Matcher().get);
        Matcher m = Pattern.compile("\\w{3,}").matcher(data);
        int i = 0;
        while (m.find()) {
            if(i>=1000) {
                break;
            }
            words.add(m.group());
            i++;
        }
        return words;
    }

    static String[] bubbleSort(String[] data) {
        int comps=0, swaps=0;
        for(int i=0; i<data.length-1; i++) {
            for (int j = 0; j<data.length-1; j++) {
                if (data[j].compareTo(data[j+1]) > 0) {
                    String s = data[j];
                    data[j] = data[j+1];
                    data[j+1] = s;
                    swaps++;
                }
                comps++;
            }
        }
        System.out.println("Bubble swap done in " + comps + " comparisons and " + swaps + " swaps, on " + data.length + " elements.");

        return data;
    }

    String[] mergeSort(String[] strings) {
        // Don't recursively probe unless we have more than 2 items left in the sublist
        if (strings.length >= 2) {
            String[] links = new String[strings.length / 2];
            String[] rechts = new String[strings.length - strings.length / 2];

            for (int i = 0; i < links.length; i++) {
                links[i] = strings[i];
            }

            for (int i = 0; i < rechts.length; i++) {
                rechts[i] = strings[i + strings.length / 2];
            }

            mergeSort(links);
            mergeSort(rechts);
            return merge(strings, links, rechts);
        }
        return null;
    }

    String[] merge(String[] strings, String[] links, String[] rechts) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < strings.length; i++) {
            if (y >= rechts.length || (x < links.length && links[x].compareToIgnoreCase(rechts[y]) < 0)) {
                strings[i] = links[x];
                x++;
            } else {
                strings[i] = rechts[y];
                y++;
                mergeSwaps++;
            }
            mergeComps++;
        }
        return strings;
    }

    int mergeComps=0, mergeSwaps=0;
    void printMergeHelper() {
        System.out.println("Merge complete in " + mergeComps + " comparisons and " + mergeSwaps + " swaps.");
        mergeComps=0;
        mergeSwaps=0;
    }

    static String readFile(String path, Charset enc) throws IOException {
        byte[] encodedBytes = Files.readAllBytes(Paths.get(path));
        return new String(encodedBytes, enc);
    }

}
