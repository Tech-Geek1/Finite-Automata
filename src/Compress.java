import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class compresses and decompresses an image to and from a file using an automata.
 *
 * @author Nicole Nkala 25022318
 */
public class Compress {

//dcjfbhkrjasbgrfabgrflgtbnfjgehfnreujkhtrafeioWKLGNATE

    /**
     * An ArrayList of Strings representing accept state.
     */
    public static ArrayList<String> acceptState = new ArrayList<>();

    /**
     * An ArrayList of Strings representing the languages.
     */
    public static ArrayList<String> pixelList = new ArrayList<>();
    /**
     * An ArrayList of ArrayLists of Strings representing languages.
     */
    public static ArrayList<ArrayList<String>> value = new ArrayList<>();
    /**
     * An ArrayList of String arrays representing the word count.
     */
    public static ArrayList<String[]> wordCount = new ArrayList<>();
    /**
     * An array of Strings representing accept state.
     */
    public static String[] acceptS;
    /**
     * A Graphics2D object used for compressing images.
     */
    public static Graphics2D gb;
    /**
     * An integer representing the number of compressed images.
     */
    public static int n = 0;
    /**
     * A File object representing the compressed image file.
     */
    public static File file;
    /**
     * An array of Strings representing the arguments passed to the Compress class.
     */
    public static String[] arguments;

    public static BufferedImage image;

    /**
     * The main method that runs the program.
     *
     * @param args An array of strings containing the command line arguments passed to the program.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {

        Decompression d = new Decompression();
        Compression c = new Compression();

        arguments = args;

        boolean isValid = validateInput(arguments);
        char multiResFlag = args[2].charAt(0);

        ArrayList<String> input = new ArrayList<>(); // creates arraylist to place input

        if (isValid) {
            if (arguments[1].equals("1")) {

                if (!decompErrors(arguments)) { //exit if im no longer testing for decomp errors
                    System.exit(0);
                }

                Scanner fileReader = new Scanner(file); //scans the file

                int counter = 0;
                while (fileReader.hasNextLine()) { // checks for next line
                    String line = fileReader.nextLine();
                    input.add(line); // adds next line to the arraylist

                    if (counter >= 2) {
                        wordCount.add(input.get(counter).split("\\s"));
                    }

                    counter++;
                }
                int stateNum = Integer.parseInt(input.get(0));
                acceptS = input.get(1).split("\\s"); //taking all the acceptStates

                Graph g = new Graph(stateNum);

                for (int i = 0; i < wordCount.size(); i++) {

                    String[] temp = wordCount.get(i);
                    g.addEdge(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
                            Integer.parseInt(temp[2]));

                }

                for (int i = 0; i < acceptS.length; i++) {

                    if (multiResFlag == 'f' || multiResFlag == 'F') {
                        c.isAccept(0, Integer.parseInt(acceptS[i]), "", g);
                    } else {
                        int wLength = Integer.parseInt(args[3]);
                        d.cycle(0, Integer.parseInt(acceptS[i]), wLength, g, "");
                    }

                }

                int width = (int) Math.pow(2, d.longestWord());

                int height = (int) Math.pow(2, d.longestWord());

                BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                gb = bImage.createGraphics();
                gb.setColor(Color.WHITE);
                gb.fillRect(0, 0, width, height);

                d.image(width, height);

                String name = file.getName();
                name = name.split("\\.")[0];
                File oFile = new File("./out/" + name + "_dec.png");
                //oFile.getParentFile().createNewFile();
                // oFile.createNewFile();
                ImageIO.write(bImage, "png", oFile);
                //System.out.println(oFile);


            } else if (arguments[1].equals("2")) { //make sure to check for the error method

                if (!compErrors(arguments)) {
                    System.exit(0);
                }
                BufferedImage cImage = ImageIO.read(file);
                image = cImage;

                QuadTree quad = new QuadTree(cImage.getHeight(), cImage);

                quad.createQuadTree(quad.root);
                quad.generatePaths(quad.root, "");
                pixelList = quad.paths;

                c.languages();
                c.makeEdge(args[3]);

            }
        } else {
            System.exit(0);
        }
    }


    /**
     * This method basically checks input validation
     *
     * @param args - this basically take in the arguments I put in.
     * @return - returns true because in every case the file must exist. Returns
     * false whenever there is an input error.
     * @throws FileNotFoundException - throws an error if the file is not found
     */
    private static boolean validateInput(String[] args) throws FileNotFoundException {

        if (args.length == 1) {
            String[] fileSplit = args[0].split("/");
            String path = fileSplit[fileSplit.length - 1];

            String[] fileSplit2 = path.split("\\.");
            String extension = fileSplit2[fileSplit2.length - 1];

            if (extension.equals("png")) {
                file = new File(path);
                arguments = path.split("\\s");
            } else {
                Scanner fileReader = new Scanner(new File(args[0]));
                String argument = fileReader.nextLine();
                String[] fileSplit3 = argument.split("\\s");
                String filePath = fileSplit3[fileSplit3.length - 1];
                file = new File(filePath);

                arguments = fileSplit3;
            }
        }
        args = arguments;
        if (args.length < 4 || args.length > 5) {
            System.err.println("Input Error - Invalid number of arguments");
            // System.exit(1);
            return false;
        }

        char gui = args[0].charAt(0);
        char modeC = args[1].charAt(0);
        char mRes = args[2].charAt(0);

        if (args.length == 4) {
            file = new File(args[3]);
        } else {
            file = new File(args[4]);
        }

        if (Character.isLetter(gui) || Character.isLetter(modeC) || Character.isDigit(mRes)) {

            System.err.println("Input Error - Invalid argument type");
            return false;

        }

        int guiMode = Integer.parseInt(args[0]);
        int mode = Integer.parseInt(args[1]);
        String multiRes = args[2];
        String resMode;

        if (guiMode != 0 && guiMode != 1) { //check GUI arguments

            System.err.println("Input Error - Invalid GUI argument");
            return false;
        }
        if (mode != 1 && mode != 2) { //check the mode
            System.err.println("Input Error - Invalid mode");
            return false;
        }
        //check multiRes flag
        if (multiRes.charAt(0) != 'f' && multiRes.charAt(0) != 't'
                && multiRes.charAt(0) != 'F' && multiRes.charAt(0) != 'T') {
            System.err.println("Input Error - Invalid multi-resolution flag");
            return false;
        }

        if ((multiRes.equals("f") || multiRes.equals("F"))
                && args.length != 4) {
            System.err.println("Input Error - Invalid number of arguments");
            System.exit(0);
        } else if ((multiRes.equals("t") || multiRes.equals("T"))
                && args.length != 5) {
            System.err.println("Input Error - Invalid number of arguments");
            System.exit(0);
        }

        if (!file.exists()) { //check the file
            System.err.println("Input Error - Invalid or missing file");
            return false;

        }

        if ((multiRes.charAt(0) == 't' || multiRes.charAt(0) == 'T') && mode == 2) {
            resMode = args[3];
            if (!resMode.equals("1") && !resMode.equals("2") && !resMode.equals("3")) {
                System.err.println("Compress Error - Invalid multi-resolution method");
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the arguments for the compress command and checks for errors in the input image.
     *
     * @param args command line arguments
     * @return true if the arguments are valid and the input image is error-free, false otherwise
     * @throws FileNotFoundException throws an error if the file is not found
     */
    public static boolean decompErrors(String[] args) throws FileNotFoundException {

        //check if args is equal to 1
        if (args.length == 1) {
            file = new File(args[0]);
            Scanner fileReader = new Scanner(new File(args[0])); //scans the file
            String text = fileReader.nextLine();
            //initialize a string array
            String[] name;
            name = text.split("\\s");
            file = new File(name[3]);
            arguments = new String[4];
            arguments[0] = name[0];
            arguments[1] = name[1];
            arguments[2] = name[2];
            arguments[3] = name[3];

        }

        char gui = arguments[0].charAt(0);
        char modeC = arguments[1].charAt(0);
        char mRes = arguments[2].charAt(0);
        //file = new File(arguments[3]);

        // Check for invalid combinations of mode, gui, and multi-resolution
        if (gui == '0' && (mRes == 't' || mRes == 'T')) {
            int wordLength = Integer.parseInt(args[3]);
            if (wordLength <= 0) {
                System.err.println("Decompression Error - Invalid word length");
                return false;
            }
        }

        // Check the format of the automaton file
        Scanner scan = new Scanner(file);

        String states = scan.nextLine();
        if (!states.matches("\\d+")) {
            System.err.println("Decompress Error - Invalid automaton formatting");
            return false;
        }

        String as = scan.nextLine();
        String[] accept = as.split("\\s");
        int[] acceptedStates = new int[accept.length];

        for (int i = 0; i < accept.length; i++) {
            int acceptedState;
            try {
                acceptedState = Integer.parseInt(accept[i]);
            } catch (NumberFormatException e) {
                System.err.println("Decompress Error - Invalid automaton formatting");
                return false;
            }

            if (acceptedState >= Integer.parseInt(states)) {
                System.err.println("Decompress Error - Invalid accept state");
                return false;
            }
            acceptedStates[i] = acceptedState;
        }

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            Scanner lineScanner = new Scanner(line);
            int numLanguages = 0;
            while (lineScanner.hasNext()) {
                String language = lineScanner.next();
                if (!language.matches("\\d+")) {
                    System.err.println("Decompress Error - Invalid automaton formatting");
                    return false;
                }

                int languageNum = Integer.parseInt(language);
                if (numLanguages < 2 && languageNum >= Integer.parseInt(states)) {
                    System.err.println("Decompress Error - Invalid transition");
                    return false;
                }
                if (numLanguages == 2 && (languageNum < 0 || languageNum > 3)) {
                    System.err.println("Decompress Error - Invalid transition");
                    return false;
                }
                numLanguages++;
            }
            if (numLanguages != 3) {
                System.err.println("Decompress Error - Invalid automaton formatting");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for errors in the input image and the input text file path provided as arguments.
     *
     * @param args an array of command line arguments that includes the input image path and the
     *             output text file path.
     * @return true if the input image is valid and the output text file path is correct, false
     * otherwise.
     * @throws IOException if there is an error reading the input image file.
     */
    public static boolean compErrors(String[] args) throws IOException {

         if (args.length == 5) {
             File file = new File(args[4]);
         } else if (args.length == 4) {
             File file = new File(args[3]);
         }

        String picName = file.getName();

        if (!picName.toLowerCase().endsWith(".png")) { //check if picture is a png file.
            System.err.println("Compress Error - Invalid input image");
            return false;
        }
        if (picName.toLowerCase().endsWith(".in")) { //check if picture is a .in file.
            ImageIO.read(file);
        }
        BufferedImage image = ImageIO.read(file); //read  the file
        picName = picName.split("\\.")[0];
        File oFile = new File("./out/" + picName + "_cmp.txt");
        // oFile.createNewFile();
        ImageIO.write(image, "txt", oFile);

        //get width and height of image
        int width = image.getWidth();
        int height = image.getHeight();
        if (width != height) {
            System.err.println("Compress Error - Invalid input image");
            return false;
        }
        // loop through width and height of the image
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color pixel = new Color(image.getRGB(i, j));

                //check for other colors other than black or white in the image
                if (!pixel.equals(Color.BLACK)) {
                    if (!pixel.equals(Color.WHITE)) {
                        System.err.println("Compress Error - Invalid input image");
                        return false;
                    }
                }
            }
        }
        return true;
    }

}


