import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Erik Bratton
 */

public final class WordCount {

    // No argument constructor--private to prevent instantiation.
    private WordCount() {
        // no code needed here
    }

    private static class IntSort
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    private static class StringSort
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o1.getKey().toLowerCase()
                    .compareTo(o2.getKey().toLowerCase());
        }
    }

    /**
     * Opens a file and returns a BufferedReader for it.
     *
     * @param prompt
     *            the message to prompt the user for file input
     * @return a BufferedReader for the specified file
     * @throws IOException
     *             if an I/O error occurs while opening the file
     */

    /**
     * Prompts the user to enter the name of a file and returns the file name.
     *
     * @param prompt
     *            The prompt message displayed to the user.
     * @return The name of the file entered by the user.
     * @throws IOException
     *             If an I/O error occurs when reading the user input.
     */
    private static String getFileName(String prompt) throws IOException {
        System.out.println(prompt);
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));
        return input.readLine();
    }

    /**
     * Creates a {@code BufferedReader} for the specified file.
     *
     * @param fileName
     *            The name of the file to be opened.
     * @return A {@code BufferedReader} for the specified file.
     * @throws IOException
     *             If an I/O error occurs while opening the file.
     */
    private static BufferedReader openFileReader(String fileName)
            throws IOException {
        return new BufferedReader(new FileReader(fileName));
    }

    /**
     * Gets an integer input from the user.
     *
     * @param prompt
     *            the message to prompt the user for input
     * @return the integer input from the user
     * @throws IOException
     *             if an I/O error occurs while reading input
     */
    private static int getUserInputInt(String prompt) throws IOException {
        System.out.println(prompt);
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));
        return Integer.parseInt(input.readLine());
    }

    /**
     * Creates a PrintWriter for the output file.
     *
     * @param prompt
     *            the message to prompt the user for output file name
     * @return a PrintWriter for the specified output file
     * @throws IOException
     *             if an I/O error occurs while creating the file
     */
    private static PrintWriter createOutputFile(String prompt)
            throws IOException {
        System.out.println(prompt);
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));
        String outputFileName = input.readLine();
        return new PrintWriter(
                new BufferedWriter(new FileWriter(outputFileName + ".html")));
    }

    /**
     * Extracts the next word or separator string from the given text starting
     * at the specified position.
     *
     * @param text
     *            the input text
     * @param position
     *            the starting index
     * @param separators
     *            the set of separator characters
     * @return the extracted word or separator string
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        boolean flag = true;
        StringBuilder word = new StringBuilder();
        char firstChar = text.charAt(position);
        for (int i = position; flag && (i < text.length()); i++) {
            flag = false;
            if (separators.contains(firstChar)) {
                if (separators.contains(text.charAt(i))) {
                    word.append(text.charAt(i));
                    flag = true;
                }
            } else {
                if (!separators.contains(text.charAt(i))) {
                    word.append(text.charAt(i));
                    flag = true;
                }
            }
        }
        return word.toString();
    }

    /**
     * Prints the opening HTML tags to the given PrintWriter.
     *
     * @param filePrint
     *            the PrintWriter to write to
     * @param fileName
     *            the name of the file
     * @param wordCount
     *            the number of top words
     */
    private static void printOpeningTags(PrintWriter filePrint, String fileName,
            int wordCount) {
        filePrint.println("<html>");
        filePrint.println("<head>");
        filePrint.println("<title> Top " + wordCount + " words in " + fileName
                + "</title>");
        filePrint.println(
                "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n"
                        + "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n"
                        + "<link href=\"https://fonts.googleapis.com/css2?family=Inria+Sans:ital,wght@0,300;0,400;0,700;1,300;1,400;1,700&display=swap\" rel=\"stylesheet\">");
        filePrint.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-"
                        + "sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css\" "
                        + "rel=\"stylesheet\" type=\"text/css\">");
        filePrint.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        filePrint.println("</head>");
        filePrint.println("<body>");
        filePrint.println(
                "<h2> Top " + wordCount + " words in " + fileName + "</h2>");
        filePrint.println("<hr>");
        filePrint.println("<div class=\"cdiv\">");
        filePrint.println("<p class=\"cbox\">");
    }

    /**
     * Prints the closing HTML tags to the given PrintWriter.
     *
     * @param filePrint
     *            the PrintWriter to write to
     */
    public static void printClosingTags(PrintWriter filePrint) {
        filePrint.println("</p>");
        filePrint.println("</div>");
        filePrint.println("</body>");
        filePrint.println("</html>");
    }

    /**
     * Adds common separator characters to the given set.
     *
     * @param separatorSet
     *            the set to add separators to
     */
    private static void addSeparators(Set<Character> separatorSet) {
        separatorSet.add(' ');
        separatorSet.add(',');
        separatorSet.add(';');
        separatorSet.add('.');
        separatorSet.add(':');
        separatorSet.add(')');
        separatorSet.add('(');
        separatorSet.add('-');
        separatorSet.add('[');
        separatorSet.add(']');
    }

    /**
     * Reads the file and counts the frequency of each word.
     *
     * @param file
     *            the BufferedReader for the file
     * @param separatorSet
     *            the set of separator characters
     * @return a map with word frequencies
     * @throws IOException
     *             if an I/O error occurs while reading the file
     */
    private static Map<String, Integer> countWords(BufferedReader file,
            Set<Character> separatorSet) throws IOException {
        Map<String, Integer> wordCount = new HashMap<>();
        String line = file.readLine();
        while (line != null) {
            int position = 0;
            while (position < line.length()) {
                String token = nextWordOrSeparator(line, position,
                        separatorSet);
                if (wordCount.containsKey(token)) {
                    wordCount.put(token, wordCount.get(token) + 1);
                } else if (!separatorSet.contains(token.charAt(0))) {
                    wordCount.put(token, 1);
                }
                position += token.length();
            }
            line = file.readLine();
        }
        return wordCount;
    }

    /**
     * Calculates the maximum word count from the list of entries.
     *
     * @param entryList
     *            the list of word entries
     * @return the maximum count among the entries
     */
    private static int getMaxCount(List<Map.Entry<String, Integer>> entryList) {
        int max = 0;
        for (Map.Entry<String, Integer> entry : entryList) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        return max;
    }

    /**
     * Sorts the word entries first by frequency and then alphabetically.
     *
     * @param wordCount
     *            the map of word frequencies
     * @param topWords
     *            the number of top words to consider
     * @return a sorted list of word entries
     */
    private static List<Map.Entry<String, Integer>> sortWords(
            Map<String, Integer> wordCount, int topWords) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(
                wordCount.entrySet());

        // Sort by frequency
        Collections.sort(entryList, new IntSort());

        // Limit the list to topWords
        if (topWords > entryList.size()) {
            topWords = entryList.size();
        }
        entryList = entryList.subList(0, topWords);

        // Sort alphabetically
        Collections.sort(entryList, new StringSort());
        return entryList;
    }

    /**
     * Prints the words with calculated font sizes to the output.
     *
     * @param output
     *            the PrintWriter for the output file
     * @param entryList
     *            the list of sorted word entries
     * @param max
     *            the maximum word count for scaling font size
     */
    private static void printWordsWithSize(PrintWriter output,
            List<Map.Entry<String, Integer>> entryList, int max) {
        final int maxSize = 48;
        final int minSize = 10;

        for (Map.Entry<String, Integer> entry : entryList) {
            String word = entry.getKey();
            int count = entry.getValue();
            int fontSize = maxSize * count / max + minSize;
            output.print("<span style=\"cursor:default; font-size:" + fontSize
                    + "px\" title=\"count: " + count + "\">" + word);
            output.println("</span>");
        }
    }

    /**
     * Main method to execute the word counting and HTML generation process.
     *
     * @param args
     *            Command line arguments (not used).
     */
    public static void main(String[] args) {
        BufferedReader file = null;
        PrintWriter output = null;
        String fileName = null;

        try {
            // Step 1: Get file name and create BufferedReader
            fileName = getFileName("Please enter the name of your file:");
            file = openFileReader(fileName);

            int topWords = getUserInputInt(
                    "Please enter the number of words you want:");
            output = createOutputFile(
                    "What do you want the name of your output file to be?");

            // Step 2: Setup separators
            Set<Character> separatorSet = new HashSet<>();
            addSeparators(separatorSet);

            // Step 3: Count words
            Map<String, Integer> wordCount = countWords(file, separatorSet);

            // Step 4: Sort words and calculate maximum frequency
            List<Map.Entry<String, Integer>> sortedWords = sortWords(wordCount,
                    topWords);
            int max = getMaxCount(sortedWords);

            // Step 5: Generate HTML output
            printOpeningTags(output, fileName, topWords);
            printWordsWithSize(output, sortedWords, max);
            printClosingTags(output);

        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                System.err
                        .println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
