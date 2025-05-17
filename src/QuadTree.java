import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Represents a QuadTree data structure for image compression.
 *
 * @author Nicole Nkala 25022318
 */
public class QuadTree {

    BufferedImage image;
    Node root;
    int picLength;
    ArrayList<String> paths = new ArrayList<>();

    /**
     * Constructs a QuadTree with the given picture size and image.
     *
     * @param picSize the size of the picture
     * @param image   the image to be compressed
     */
    QuadTree(int picSize, BufferedImage image) {
        this.image = image;
        this.picLength = picSize;
        root = new Node(0, 0, picSize, "");
    }

    /**
     * Represents a node in the QuadTree.
     */
    class Node {
        int x, y, size;
        String index;
        boolean isLeaf, isBlack;
        Node[] children;

        /**
         * Constructs a new Node object with the specified coordinates, size, and index.
         *
         * @param x     the x-coordinate of the node
         * @param y     the y-coordinate of the node
         * @param size  the size of the node
         * @param index the index of the node
         */
        Node(int x, int y, int size, String index) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.index = index;
            this.isLeaf = false;
            this.isBlack = false;
            this.children = new Node[4];
        }

    }

    /**
     * Splits a node into four children nodes.
     *
     * @param node the node to be split
     */
    public void splitNode(Node node) {
        int x = node.x;
        int y = node.y;
        int size = node.size;
        node.isLeaf = false;
        node.children[0] = new Node(x, y + size / 2, size / 2, "0");
        node.children[1] = new Node(x, y, size / 2, "1");
        node.children[2] = new Node(x + size / 2, y + size / 2, size / 2, "2");
        node.children[3] = new Node(x + size / 2, y, size / 2, "3");
    }

    /**
     * Checks if at least one pixel in the node is black.
     *
     * @param node the node to check
     * @return true if at least one pixel is black, false otherwise
     */
    public boolean hasBlackPixel(Node node) {
        int x = node.x;
        int y = node.y;
        int size = node.size;
        int sizeX = x + size;
        int sizeY = y + size;

        for (int i = x; i < sizeX; i++) {
            for (int j = y; j < sizeY; j++) {
                Color colour = new Color(image.getRGB(i, j));
                if (colour.equals(Color.BLACK)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if all pixels in the node are black.
     *
     * @param node the node to check
     * @return true if all pixels are black, false otherwise
     */
    public boolean isAllBlackPixels(Node node) {
        int x = node.x;
        int y = node.y;
        int size = node.size;
        int sizeX = x + size;
        int sizeY = y + size;

        for (int i = x; i < sizeX; i++) {
            for (int j = y; j < sizeY; j++) {
                Color pixelColour = new Color(image.getRGB(i, j));
                if (pixelColour.equals(Color.WHITE)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates the QuadTree by recursively splitting the nodes based on the image pixels.
     *
     * @param node the current node being processed
     */
    public void createQuadTree(Node node) {
        if (isAllBlackPixels(node)) {
            node.isLeaf = true;
        } else if (hasBlackPixel(node)) {
            splitNode(node);
            for (int i = 0; i < 4; i++) {
                createQuadTree(node.children[i]);
            }
        }
    }

    /**
     * Generates the paths in the QuadTree by traversing the nodes recursively.
     *
     * @param node the current node being processed
     * @param path the path representing the current node's position in the QuadTree
     */
    public void generatePaths(Node node, String path) {
        String index = "";
        if (node != null) {
            index = node.index;
        }
        String newPath = path + index;

        if (node != null) {
            if (node.isLeaf) {
                if (!paths.contains(newPath)) {
                    paths.add(newPath);
                }
            } else {
                generatePaths(node.children[0], newPath);
                generatePaths(node.children[1], newPath);
                generatePaths(node.children[2], newPath);
                generatePaths(node.children[3], newPath);
            }
        }
    }

}

