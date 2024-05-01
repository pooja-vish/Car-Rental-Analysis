package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MykSplayTreez {
    class MykNode {
        String mykKey;
        MykNode mykLeft, mykRight, mykParent;

        MykNode(String mykKey) {
            this.mykKey = mykKey;
            mykLeft = mykRight = mykParent = null;
        }
    }

    MykNode mykRoot;

    private void mykRotateToRoot(MykNode currentNode) {
        MykNode mykParentNode = currentNode.mykParent;
        if (currentNode == mykParentNode.mykLeft) {
            mykParentNode.mykLeft = currentNode.mykRight;
            if (currentNode.mykRight != null) {
                currentNode.mykRight.mykParent = mykParentNode;
            }
            currentNode.mykRight = mykParentNode;
        } else {
            mykParentNode.mykRight = currentNode.mykLeft;
            if (currentNode.mykLeft != null) {
                currentNode.mykLeft.mykParent = mykParentNode;
            }
            currentNode.mykLeft = mykParentNode;
        }
        currentNode.mykParent = mykParentNode.mykParent;
        mykParentNode.mykParent = currentNode;
        if (currentNode.mykParent != null) {
            if (mykParentNode == currentNode.mykParent.mykLeft) {
                currentNode.mykParent.mykLeft = currentNode;
            } else {
                currentNode.mykParent.mykRight = currentNode;
            }
        } else {
            mykRoot = currentNode;
        }
    }

    private void mykSplayToRoot(MykNode currentNode) {
        while (currentNode.mykParent != null) {
            MykNode mykParentNode = currentNode.mykParent;
            MykNode mykGrandparentNode = mykParentNode.mykParent;
            if (mykGrandparentNode == null) {
                mykRotateToRoot(currentNode);
            } else if ((mykParentNode.mykLeft == currentNode &&
                    mykGrandparentNode.mykLeft == mykParentNode)
                    || (mykParentNode.mykRight == currentNode &&
                    mykGrandparentNode.mykRight == mykParentNode)) {
                mykRotateToRoot(mykParentNode);
                mykRotateToRoot(currentNode);
            } else {
                mykRotateToRoot(currentNode);
                mykRotateToRoot(currentNode);
            }
        }
    }

    private void mykCollectWordsInOrder(MykNode mykNode, List<String> wordList) {
        if (mykNode != null) {
            mykCollectWordsInOrder(mykNode.mykLeft, wordList);
            wordList.add(mykNode.mykKey);
            mykCollectWordsInOrder(mykNode.mykRight, wordList);
        }
    }

    public List<String> mykGetAllWords() {
        List<String> wordList = new ArrayList<>();
        mykCollectWordsInOrder(mykRoot, wordList);
        return wordList;
    }

    public void mykInsert(String mykWord) {
        MykNode newMykNode = new MykNode(mykWord);
        if (mykRoot == null) {
            mykRoot = newMykNode;
            return;
        }
        MykNode currentMykNode = mykRoot;
        MykNode mykParentNode = null;
        while (currentMykNode != null) {
            mykParentNode = currentMykNode;
            if (mykWord.compareTo(currentMykNode.mykKey) < 0) {
                currentMykNode = currentMykNode.mykLeft;
            } else if (mykWord.compareTo(currentMykNode.mykKey) > 0) {
                currentMykNode = currentMykNode.mykRight;
            } else {
                mykSplayToRoot(currentMykNode);
                return;
            }
        }
        if (mykWord.compareTo(mykParentNode.mykKey) < 0) {
            mykParentNode.mykLeft = newMykNode;
        } else {
            mykParentNode.mykRight = newMykNode;
        }
        newMykNode.mykParent = mykParentNode;
        mykSplayToRoot(newMykNode);
    }

    private MykNode mykSearchNode(String mykWord) {
        MykNode currentMykNode = mykRoot;
        while (currentMykNode != null) {
            int cmp = mykWord.compareTo(currentMykNode.mykKey);
            if (cmp == 0) {
                return currentMykNode;
            } else if (cmp < 0) {
                currentMykNode = currentMykNode.mykLeft;
            } else {
                currentMykNode = currentMykNode.mykRight;
            }
        }
        return null;
    }
}

public class WordCompletion {
    private static void mykPopulateDictionary(MykSplayTreez dictionary) {
        dictionary.mykInsert("Vancouver International Airport");
        dictionary.mykInsert("Montreal–Trudeau International Airport");
        dictionary.mykInsert("Ottawa International Airport");
        dictionary.mykInsert("Toronto Airport");
        dictionary.mykInsert("Windsor Airport");
        dictionary.mykInsert("Hamilton International Airport");
        dictionary.mykInsert("London International Airport");
        dictionary.mykInsert("Billy Bishop Toronto City Airport");
        dictionary.mykInsert("Thunder Bay International Airport");
        dictionary.mykInsert("Waterloo International Airport");
    }

    private static List<String> mykWordCompletion(String prefix, MykSplayTreez dictionary) {
        List<String> suggestions = new ArrayList<>();
        prefix = prefix.toLowerCase();

        for (String word : dictionary.mykGetAllWords()) {
            if (word.toLowerCase().startsWith(prefix) && !word.equalsIgnoreCase(prefix)) {
                suggestions.add(word);
            }
        }
        return suggestions;
    }

    public static void main(String[] args) {
        MykSplayTreez splayTree = new MykSplayTreez();
        mykPopulateDictionary(splayTree);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a prefix for word completion (or 'exit' to quit): ");
            String userInput = scanner.nextLine().trim().toLowerCase();
            if (userInput.equals("exit")) {
                System.out.println("Thank you for using Word Completion! Goodbye :)");
                break;
            }

            List<String> completionSuggestions = mykWordCompletion(userInput, splayTree);
            if (!completionSuggestions.isEmpty()) {
                System.out.println("Word completion suggestions:");
                for (String suggestion : completionSuggestions) {
                    System.out.println(" - '" + suggestion + "'");
                }
            } else {
                System.out.println("No suggestions found for the given prefix.");
            }
        }
        scanner.close();
    }
}
