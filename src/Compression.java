import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * The Compression class represents a utility class for compressing data and creating graphs based
 * on pixel lists. It provides methods for generating languages, creating edges in a graph, and
 * generating a text file representation of the graph.
 *
 * @author Nicole Nkala 25022318
 */
public class Compression extends Compress {

    /**
     * Creates and populates a list of languages based on given pixelList.
     */
    public static void languages() {
        value = new ArrayList<>();
        ArrayList<String> newLang;
        value.add(pixelList);
        boolean islang;
        for (int w = 0; w < value.size(); w++) {
            ArrayList<String> language = value.get(w);
            boolean[] indices = new boolean[4];  // Initialize boolean array for indices
            // of each language set
            for (String pathWay : language) {
                if (!pathWay.isEmpty()) {  // Check if the string is not empty
                    char firstChar = pathWay.charAt(0);  // Get the first character in the string
                    if (firstChar >= '0' && firstChar <= '3') {  // Check if first character is
                        // between 0 and 3
                        indices[firstChar - '0'] = true;
                    }
                }
            }
            if (indices[0]) {
                newLang = new ArrayList<>();  // Create a temp ArrayList for language 0
                for (String pathCounter : language) {
                    if (!pathCounter.isEmpty()) {
                        char firstChar = pathCounter.charAt(0);
                        int set = Character.getNumericValue(firstChar);
                        if (set == 0 && pathCounter.length() > 1) {
                            newLang.add(pathCounter.substring(1));
                        }
                    }
                }
                int langIndex = value.indexOf(newLang);  // Check if the new ArrayList already
                // exists in the temp ArrayList
                if (langIndex == -1) {
                    value.add(newLang);  // Add to the new to temp ArrayList
                }
            }
            if (indices[1]) { // Repeat the same process for language sets 1, 2, and 3
                newLang = new ArrayList<>();

                for (String pathCounter : language) {
                    if (!pathCounter.isEmpty()) {
                        char firstChar = pathCounter.charAt(0);
                        int set = Character.getNumericValue(firstChar);
                        if (set == 1 && pathCounter.length() > 1) {
                            newLang.add(pathCounter.substring(1));
                        }
                    }
                }
                int langIndex = value.indexOf(newLang); // Check if the new ArrayList already
                // exists in the temp ArrayList of languages
                if (langIndex == -1) {
                    value.add(newLang);
                }
            }
            if (indices[2]) {
                newLang = new ArrayList<>();
                for (String pathCounter : language) {
                    if (!pathCounter.isEmpty()) {
                        int set = Character.getNumericValue(pathCounter.charAt(0));
                        if (set == 2 && pathCounter.length() > 1) {
                            newLang.add(pathCounter.substring(1));
                        }
                    }
                }
                boolean isLang = false;
                for (ArrayList<String> lang : value) {
                    if (lang.equals(newLang)) {
                        isLang = true;
                        break;
                    }
                }
                if (!isLang) {
                    value.add(newLang);
                }
            }
            if (indices[3]) {
                newLang = new ArrayList<String>(); //make my new temp arraylist

                for (int i = 0; i < language.size(); i++) {
                    String pathCounter = language.get(i);
                    if (pathCounter != "") {
                        int val = pathCounter.charAt(0);
                        int set = Character.getNumericValue(val);
                        if (set == 3) {
                            if (pathCounter.substring(1) != "") {
                                newLang.add(pathCounter.substring(1));
                            }
                        }
                    }
                }
                islang = false;
                for (int j = 0; j < value.size(); j++) {
                    if (value.get(j).equals(newLang)) {
                        islang = true;
                    }
                }
                if (!islang) {
                    value.add(newLang);
                }
            }
        }

//        for (int i = 0; i < value.size(); i++) {
//            if (value.get(i).isEmpty()) {
//                //System.out.println(i);
//            }
//        }
    }

    /**
     * This method creates edges in a graph based on input pixel lists.
     *
     * @param mode The multi res compression value
     * @throws IOException if an I/O error occurs.
     */
    public static void makeEdge(String mode) throws IOException {
        value.add(pixelList); //my arraylist of arraylists
        ArrayList<String> newLang;
        int count = 1; // initialize a counter
        Graph graph = new Graph(value.size());
        boolean islang;  //initialize boolean to check whether a language exists

        for (int i = 0; i < value.size(); i++) {
            ArrayList<String> aStates = value.get(i);
            if (aStates.isEmpty()) {
                acceptState.add(String.valueOf(i));
            }
        }
        int numStates = value.size() - 1;

        if (mode.equals("1")) {
            //System.out.println(acceptState.size());
            for (int i = 0; i < acceptState.size(); i++) {
                for (int k = 0; k < 4; k++) {
                    if (k != getWhite()) {
                        graph.addEdge(Integer.parseInt(acceptState.get(i)),
                                Integer.parseInt(acceptState.get(i)), k);
                    }
                }
            }
        } else if (mode.equals("2")) {
            for (int i = 0; i < acceptState.size(); i++) {
                for (int k = 0; k < 4; k++) {
                    graph.addEdge(0, 0, k);
                }
            }
        }

        for (int w = 0; w < value.size() - 1; w++) { //loop through arraylist of arraylists
            ArrayList<String> language = value.get(w); //gets index of the arraylist of arraylists
            boolean[] indices = new boolean[4]; //array containing all 4 laguages
            for (String pathWay : language) {
                if (!pathWay.isEmpty()) {
                    char firstChar = pathWay.charAt(0);
                    if (firstChar >= '0' && firstChar <= '3') {
                        indices[firstChar - '0'] = true;
                    }
                }
            }
            if (indices[0]) {
                newLang = new ArrayList<>(); // create temp arraylist
                for (String pathCounter : language) {
                    if (!pathCounter.isEmpty()) {
                        char firstChar = pathCounter.charAt(0);
                        int set = Character.getNumericValue(firstChar);
                        if (set == 0 && pathCounter.length() > 1) {
                            newLang.add(pathCounter.substring(1));
                        }
                    }
                }
                int langIndex = value.indexOf(newLang);
                if (langIndex == -1) {
                    value.add(newLang);
                    count++;
                    graph.addEdge(w, count - 1, 0);
                } else {
                    graph.addEdge(w, langIndex, 0);
                }
            }
            // Repeat the same process for language sets 1, 2, and 3
            if (indices[1]) {
                newLang = new ArrayList<>();

                for (String pathCounter : language) {
                    if (!pathCounter.isEmpty()) {
                        char firstChar = pathCounter.charAt(0);
                        int set = Character.getNumericValue(firstChar);

                        if (set == 1 && pathCounter.length() > 1) {
                            newLang.add(pathCounter.substring(1));
                        }
                    }
                }
                int langIndex = value.indexOf(newLang); // Check if the new ArrayList already
                // exists in the temp ArrayList
                if (langIndex == -1) {
                    value.add(newLang);
                    count++;
                    graph.addEdge(w, count - 1, 1);
                } else {
                    graph.addEdge(w, langIndex, 1);
                }
            }
            if (indices[2]) {
                newLang = new ArrayList<>(); //temp arraylist

                for (String pathCounter : language) {
                    if (!pathCounter.isEmpty()) {
                        int set = Character.getNumericValue(pathCounter.charAt(0));
                        if (set == 2 && pathCounter.length() > 1) {
                            newLang.add(pathCounter.substring(1));
                        }
                    }
                }
                boolean isLang = false;
                for (int j = 0; j < value.size(); j++) {
                    if (value.get(j).equals(newLang)) {
                        isLang = true;
                        graph.addEdge(w, j, 2);
                        break;
                    }
                }
                if (!isLang) {
                    value.add(newLang);
                    count++;
                }
            }
            if (indices[3]) {
                newLang = new ArrayList<>();
                for (int i = 0; i < language.size(); i++) {
                    String pathCounter = language.get(i);
                    if (pathCounter != "") {
                        int val = pathCounter.charAt(0);
                        int set = Character.getNumericValue(val);
                        if (set == 3) {
                            if (!pathCounter.substring(1).equals("")) {
                                newLang.add(pathCounter.substring(1));
                            }
                        }
                    }
                }
                islang = false;
                for (int j = 0; j < value.size(); j++) {
                    if (value.get(j).equals(newLang)) {
                        islang = true;
                        graph.addEdge(w, j, 3);
                    }
                }
                if (!islang) {
                    value.add(newLang);
                    count++;
                }
            }
        }
        if (mode.equals("3")) {
            for (int i = 0; i < numStates; i++) {
                if (!acceptState.contains(i)) {
                    acceptState.add(String.valueOf(i));
                }
            }
        }
        graph.printGraph(); //print my graph with all my edges
        makeTextfile(graph, mode, acceptState); //print my graph in textfile modex
    }

    /**
     * Recursively traverses the graph to check if a given node is an accept state.
     * If the node is an accept state, adds the path to acceptState ArrayList.
     *
     * @param current the current node being traversed
     * @param finalW  the final node to be checked if an accept state
     * @param path    the path from the start node to the current node
     * @param g       the graph being traversed
     */
    public static void isAccept(int current, int finalW, String path, Graph g) {

        //check if the current word is equal to the final word
        if (current == finalW) {
            acceptState.add(path); //add it to the path
            return;
        }

        LinkedList<Edge> list = g.adjacencylist[current];

        for (int j = 0; j < list.size(); j++) {
            path = path + list.get(j).weight;
            isAccept(list.get(j).destination, finalW, path, g);
            path = path.substring(0, path.length() - 1);
        }

    }

    /**
     * Creates a text file representation of a given graph object, in the format required by the
     * Automata project. The output file will be stored in the "out" directory and have the same
     * name as the input image file.
     *
     * @param graph The graph object to be represented in the output file
     * @param mode The multi res compression value
     * @param acceptState The array list of strings that store all the accept states
     * @throws IOException If there is an error while writing the output file
     */
    public static void makeTextfile(Graph graph, String mode,
                                    ArrayList<String> acceptState) throws IOException {

        // Get the name of the file
         String picName = file.getName();
         ArrayList<String> pictureName = new ArrayList<>(Arrays.asList(picName.split("\n")));
         pictureName.removeAll(Collections.singleton(null)); // removes all null elements from list
         pictureName.removeAll(Collections.singleton("")); // removes empty elements from list
         picName = picName.split("\\.")[0];
         FileWriter fileW = new FileWriter("out/" + picName + "_cmp.txt");

        // Get the name of the file
//        String picName = file.getName();
//        picName = picName.split("\\.")[0];
//        FileWriter fileW = new FileWriter("./out/" + picName + "_cmp.txt");

        BufferedWriter file = new BufferedWriter(fileW);

        ArrayList<Edge> adjacencyList = graph.adjlist;

        String aState = "";
        // Get the initial state of the automaton
        if (mode.equals("3")) {
            for (String i : acceptState) {
                aState += i + " ";
            }
        } else {
            for (int i = 0; i < value.size(); i++) {
                if (value.get(i).isEmpty()) {
                    aState += i + " ";
                }
            }
        }
        System.out.println(acceptState.size());

        System.out.println(aState);
        String numStates = String.valueOf(value.size() - 1);
        file.write(numStates);
        file.write("\n" + aState);
        for (int i = 0; i < adjacencyList.size(); i++) {
            Edge edge = adjacencyList.get(i);
            file.write("\n" + edge.startState + " " + edge.destination + " " + edge.weight);
        }

        file.close();

    }


    //Multi Res Methods

    /**
     * Retrieves the quadrant with the highest number of white pixels in the image.
     *
     * @return The index of the quadrant (0-3) with the most white pixels.
     */
    public static int getWhite() {

        int[] whitePixelsPerQuadrant = new int[4];

        Color pixel;

        // Count white pixels in each quadrant
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixel = new Color(image.getRGB(x, y));
                boolean isWhite = pixel.equals(Color.WHITE);
                if (isWhite) {
                    if (y > image.getWidth() / 2) {
                        if (x > image.getWidth() / 2) {
                            whitePixelsPerQuadrant[2]++;
                        } else {
                            whitePixelsPerQuadrant[0]++;
                        }
                    } else {
                        if (x > image.getWidth() / 2) {
                            whitePixelsPerQuadrant[3]++;
                        } else {
                            whitePixelsPerQuadrant[1]++;
                        }
                    }
                }
            }
        }

//        for (int i = 0; i < whitePixelsPerQuadrant.length; i++) {
//            System.out.println(i + ": " + whitePixelsPerQuadrant[i]);
//        }

        // Find quadrant with most white pixels
        int maxWhitePixels = whitePixelsPerQuadrant[0];
        int quadrantMax = 0;
        for (int i = 1; i < whitePixelsPerQuadrant.length; i++) {
            if (whitePixelsPerQuadrant[i] > maxWhitePixels) {
                maxWhitePixels = whitePixelsPerQuadrant[i];
                quadrantMax = i;
            }
        }

        return quadrantMax;
    }
}
