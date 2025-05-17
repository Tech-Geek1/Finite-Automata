import java.awt.Color;
import java.util.LinkedList;

/**
 * This class extends the Compress class and provides additional methods for decompression.
 *
 * @author Nicole Nkala 25022318
 */
public class Decompression extends Compress {

    /**
     * This method takes in two integers, w and h, and generates an image based on the values in
     * acceptState. For each string in acceptState, the method parses the string and generates a
     * pixel on the image. The parsing algorithm is based on the fillArray() method.
     *
     * @param w the width of the image
     * @param h the height of the image
     */
    public static void image(int w, int h) {

        for (int i = 0; i < acceptState.size(); i++) {  // Loop through all accept states

            String weight = acceptState.get(i);
            // Initialize variables for the current coordinates and dimensions
            int yVal = 0;
            int xVal = 0;
            int width = w;
            int height = h;
            int pixelSize = w;

            // Loop through the characters in the weight string
            for (int j = 0; j < weight.length(); j++) {
                pixelSize = pixelSize / 2;
                // Update the coordinates and dimensions based on the current character
                if (weight.charAt(j) == '0') {
                    // Quadrant 0: move down and reduce height and width by half
                    yVal = yVal + height / 2;
                    height = height / 2;
                    width = width / 2;
                } else if (weight.charAt(j) == '1') {
                    // Quadrant 1: reduce height and width by half
                    height = height / 2;
                    width = width / 2;
                } else if (weight.charAt(j) == '2') {
                    // Quadrant 2: move right and down, and reduce height
                    // and width by half
                    xVal = xVal + width / 2;
                    yVal = yVal + height / 2;
                    height = height / 2;
                    width = width / 2;

                } else if (weight.charAt(j) == '3') {
                    // Quadrant 3: move right and reduce height
                    // and width by half
                    xVal = xVal + width / 2;
                    height = height / 2;
                    width = width / 2;
                }


            }
            // Draw a black pixel at the current coordinates
            gb.setColor(Color.BLACK);
            gb.fillRect(xVal, yVal, pixelSize, pixelSize);

        }


    }

    /**
     * Returns the length of the longest word in the `acceptState` ArrayList.
     *
     * @return the length of the longest word in the `acceptState` ArrayList
     */
    public static int longestWord() {

        int longestWord = 0;
        // Iterate through each element in the acceptState ArrayList
        for (int i = 0; i < acceptState.size(); i++) {
            // Get the string at the current index
            String weight = acceptState.get(i);
            // If the length of the string is greater than the current
            // longest word, update the longest word
            if (weight.length() > longestWord) {
                longestWord = weight.length();
            }
        }
        // Return the length of the longest word
        return longestWord;
    }


    /**
     * Recursively explores the graph and generates paths from the current node to the finalW node.
     *
     * @param current the current node
     * @param finalW  the final node
     * @param wLength the length of the word
     * @param g       the graph
     * @param path    the current path
     */
    public static void cycle(int current, int finalW, int wLength, Graph g, String path) {

        if (current == finalW && wLength == path.length()) {
            acceptState.add(path); //add it to the path
            return;
        }

        LinkedList<Edge> list = g.adjacencylist[current];
        for (int j = 0; j < list.size(); j++) {
            if (path.length() <= wLength) {
                path = path + list.get(j).weight;
                cycle(list.get(j).destination, finalW, wLength, g, path);
                path = path.substring(0, path.length() - 1);
            }
        }
    }

}
