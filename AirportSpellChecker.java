package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MySplayTree {
    class Node {
        String key;
        Node left, right, parent;

        Node(String key) {
            this.key = key;
            left = right = parent = null;
        }
    }

    Node root;

    private void rotateToRoot(Node currentNode) {
        Node parentNode = currentNode.parent;
        if (currentNode == parentNode.left) {
            parentNode.left = currentNode.right;
            if (currentNode.right != null) {
                currentNode.right.parent = parentNode;
            }
            currentNode.right = parentNode;
        } else {
            parentNode.right = currentNode.left;
            if (currentNode.left != null) {
                currentNode.left.parent = parentNode;
            }
            currentNode.left = parentNode;
        }
        currentNode.parent = parentNode.parent;
        parentNode.parent = currentNode;
        if (currentNode.parent != null) {
            if (parentNode == currentNode.parent.left) {
                currentNode.parent.left = currentNode;
            } else {
                currentNode.parent.right = currentNode;
            }
        } else {
            root = currentNode;
        }
    }

    private void splayToRoot(Node currentNode) {
        while (currentNode.parent != null) {
            Node parentNode = currentNode.parent;
            Node grandparentNode = parentNode.parent;
            if (grandparentNode == null) {
                rotateToRoot(currentNode);
            } else if ((parentNode.left == currentNode && grandparentNode.left == parentNode)
                    || (parentNode.right == currentNode && grandparentNode.right == parentNode)) {
                rotateToRoot(parentNode);
                rotateToRoot(currentNode);
            } else {
                rotateToRoot(currentNode);
                rotateToRoot(currentNode);
            }
        }
    }

    private void collectWordsInOrder(Node node, List<String> wordList) {
        if (node != null) {
            collectWordsInOrder(node.left, wordList);
            wordList.add(node.key);
            collectWordsInOrder(node.right, wordList);
        }
    }

    public List<String> getAllWords() {
        List<String> wordList = new ArrayList<>();
        collectWordsInOrder(root, wordList);
        return wordList;
    }

    public void insert(String word) {
        Node newNode = new Node(word);
        if (root == null) {
            root = newNode;
            return;
        }

        Node currentNode = root;
        Node parentNode = null;
        while (currentNode != null) {
            parentNode = currentNode;
            if (word.compareTo(currentNode.key) < 0) {
                currentNode = currentNode.left;
            } else if (word.compareTo(currentNode.key) > 0) {
                currentNode = currentNode.right;
            } else {
                splayToRoot(currentNode);
                return;
            }
        }

        if (word.compareTo(parentNode.key) < 0) {
            parentNode.left = newNode;
        } else {
            parentNode.right = newNode;
        }
        newNode.parent = parentNode;
        splayToRoot(newNode);
    }

    public boolean search(String word) {
        Node node = searchNode(word);
        if (node != null) {
            splayToRoot(node);
            return true;
        }
        return false;
    }

    private Node searchNode(String word) {
        Node currentNode = root;
        while (currentNode != null) {
            int cmp = word.compareTo(currentNode.key);
            if (cmp == 0) {
                return currentNode;
            } else if (cmp < 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    public void delete(String word) {
        Node node = searchNode(word);
        if (node != null) {
            splayToRoot(node);

            if (node.left == null) {
                root = node.right;
                if (node.right != null) {
                    node.right.parent = null;
                }
            } else {
                Node rightSubtree = node.right;
                root = node.left;
                node.left.parent = null;
                Node currentNode = root;
                while (currentNode.right != null) {
                    currentNode = currentNode.right;
                }
                currentNode.right = rightSubtree;
                if (rightSubtree != null) {
                    rightSubtree.parent = currentNode;
                }
            }
        }
    }
}

public class AirportSpellChecker {
    private static void populateDictionary(MySplayTree dictionary) {
        dictionary.insert("windsor airport");
        dictionary.insert("toronto airport");
        dictionary.insert("vancouver international airport");
        dictionary.insert("ottawa international airport");
        dictionary.insert("montréal–trudeau international airport");
        dictionary.insert("halifax airport");
        dictionary.insert("winnipeg airport");
        dictionary.insert("regina airport");
        dictionary.insert("london airport");
    }

    private static List<String> findClosestMatchingWords(String input, MySplayTree dictionary, int maxDistanceThreshold, int maxMatches) {
        List<String> closestMatches = new ArrayList<>(maxMatches);
        for (int i = 0; i < maxMatches; i++) {
            closestMatches.add("");
        }
        int[] minDistances = new int[maxMatches];
        for (int i = 0; i < maxMatches; i++) {
            minDistances[i] = Integer.MAX_VALUE;
        }

        for (String word : dictionary.getAllWords()) {
            int distance = calculateLevenshteinDistance(input, word);
            if (distance <= maxDistanceThreshold) {
                for (int i = 0; i < maxMatches; i++) {
                    if (distance < minDistances[i]) {
                        for (int j = maxMatches - 1; j > i; j--) {
                            minDistances[j] = minDistances[j - 1];
                            closestMatches.set(j, closestMatches.get(j - 1));
                        }
                        minDistances[i] = distance;
                        closestMatches.set(i, word);
                        break;
                    }
                }
            }
        }

        List<String> validMatches = new ArrayList<>();
        for (int i = 0; i < maxMatches; i++) {
            if (!closestMatches.get(i).isEmpty() && minDistances[i] != Integer.MAX_VALUE) {
                validMatches.add(closestMatches.get(i));
            }
        }

        return validMatches;
    }

    private static int calculateLevenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int substitutionCost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + substitutionCost);
                }
            }
        }

        return dp[m][n];
    }

    public static void main(String[] args) {
        MySplayTree airportDictionary = new MySplayTree();
        populateDictionary(airportDictionary);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter an airport name to check its spelling (or 'exit' to quit): ");
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (userInput.equals("exit")) {
                System.out.println("Thank you for using Airport Spell Checker! Goodbye :)");
                break;
            }

            int maxDistanceThreshold = 2;
            int maxMatches = 3;

            if (airportDictionary.search(userInput)) {
                System.out.println("The airport name '" + userInput + "' is spelled correctly.");
            } else {
                List<String> suggestions = findClosestMatchingWords(userInput, airportDictionary, maxDistanceThreshold, maxMatches);

                System.out.println("The airport name '" + userInput + "' is not found in the database.");
                if (!suggestions.isEmpty()) {
                    System.out.println("Did you mean:");
                    for (String suggestion : suggestions) {
                        System.out.println("  - '" + suggestion + "'");
                    }
                }
            }
        }

        scanner.close();
    }
}
