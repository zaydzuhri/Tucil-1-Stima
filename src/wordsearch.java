package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class wordsearch {
    public static void main(String[] args) throws IOException {
        System.out.println("Enter puzzle file name:");

        Scanner scan = new Scanner(System.in);
        String file = scan.nextLine();


        FileReader fr = new FileReader(
                System.getProperty("user.dir")+"/test/"+file);
        
        ArrayList<ArrayList<Character>> grid = new ArrayList<ArrayList<Character>>();
        ArrayList<String> words = new ArrayList<String>();

        int i, gridRow;
        boolean gridLoop = true;
        boolean lastNewline = false;

        grid.add(new ArrayList<Character>());
        gridRow = 0;
        while (gridLoop) {
            i = fr.read();
            if (i == -1) {
                gridLoop = false;
            } else if (i == '\n') {
                if (lastNewline) {
                    gridLoop = false;
                } else {
                    lastNewline = true;
                    grid.add(new ArrayList<Character>());
                    gridRow += 1;
                }
            } else if (i != ' ') {
                lastNewline = false;
                grid.get(gridRow).add((char) i);
            }
        }

        System.out.println("\nWord Search Puzzle: ");
        for (int j = 0; j < grid.size(); j++) {
            for (int k = 0; k < grid.get(j).size(); k++) {
                System.out.print(grid.get(j).get(k));
                System.out.print(" ");
            }
            System.out.println();
        }

        boolean wordLoop = true;
        StringBuffer buffer = new StringBuffer();
        while (wordLoop) {
            i = fr.read();
            if (i == -1) {
                wordLoop = false;
            } else if (i == '\n') {
                words.add(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) i);
            }
        }

        System.out.println("Words:");
        for (int l = 0; l < words.size(); l++) {
            System.out.println(words.get(l));
        }
        System.out.println();

        long execTime = 0;
        int totalComparisons = 0;
        for (String word : words) {
            long startTime = System.nanoTime();
            int comparisons = 0;
            boolean found = false;
            System.out.println("Solution for " + word + ":");

            for (int row = 0; row < grid.size() && !found; row++) {
                for (int col = 0; col < grid.get(row).size() && !found; col++) {
                    if (grid.get(row).get(col) == word.charAt(0)) {
                        for (int rowOff = -1; rowOff <= 1 && !found; rowOff++) {
                            for (int colOff = -1; colOff <= 1 && !found; colOff++) {
                                if ((row + rowOff) >= 0 && (row + rowOff) < grid.size() && (col + colOff) >= 0
                                        && (col + colOff) < grid.get(row + rowOff).size()) {
                                    if (grid.get(row + rowOff).get(col + colOff) == word.charAt(1)) {
                                        for (int mult = 2; mult < word.length()
                                                && (row + (rowOff * mult)) >= 0
                                                && (row + (rowOff * mult)) < grid.size()
                                                && (col + (colOff * mult)) >= 0
                                                && (col + (colOff * mult)) < grid.get(row + (rowOff * mult)).size()
                                                && !found; mult++) {
                                            comparisons += 1;
                                            if (grid.get(row + (rowOff * mult)).get(col + (colOff * mult)) != word
                                                    .charAt(mult)) {
                                                break;
                                            } else if (mult == word.length() - 1) {
                                                found = true;
                                                for (int x = 0; x < grid.size(); x++) {
                                                    for (int y = 0; y < grid.get(x).size(); y++) {
                                                        if ((rowOff == 0 || colOff == 0)
                                                                && x >= Math.min(row, (row + (rowOff * mult)))
                                                                && x <= Math.max(row, (row + (rowOff * mult)))
                                                                && y >= Math.min(col, (col + (colOff * mult)))
                                                                && y <= Math.max(col, (col + (colOff * mult)))) {
                                                            System.out.print(grid.get(x).get(y));
                                                        } else if (rowOff == colOff
                                                                && x >= Math.min(row, (row + (rowOff * mult)))
                                                                && x <= Math.max(row, (row + (rowOff * mult)))
                                                                && y >= Math.min(col, (col + (colOff * mult)))
                                                                && y <= Math.max(col, (col + (colOff * mult)))
                                                                && x - y == row - col) {
                                                            System.out.print(grid.get(x).get(y));
                                                        } else if (x >= Math.min(row, (row + (rowOff * mult)))
                                                                && x <= Math.max(row, (row + (rowOff * mult)))
                                                                && y >= Math.min(col, (col + (colOff * mult)))
                                                                && y <= Math.max(col, (col + (colOff * mult)))
                                                                && x + y == row + col) {
                                                            System.out.print(grid.get(x).get(y));
                                                        } else {
                                                            System.out.print("-");
                                                        }
                                                        System.out.print(" ");
                                                    }
                                                    System.out.println();
                                                }
                                            }
                                        }
                                    }
                                    comparisons += 1;
                                }
                            }
                        }
                    }
                    comparisons += 1;
                }
            }

            long endTime = System.nanoTime();
            System.out.println("Execution time: " + ((endTime - startTime) / 1000000) + "ms");
            System.out.println("Comparisons: " + comparisons + "\n");
            execTime += (endTime - startTime);
            totalComparisons += comparisons;
        }
        
        System.out.println("Total Execution Time: " + execTime/1000000 + "ms");
        System.out.println("Total Comparisons: " + totalComparisons);
        
        fr.close();
    }
}

