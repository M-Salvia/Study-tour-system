package com.example.utils;
import java.util.*;
public class LogTools {
    public class KMPMatcher {
        public static int[] computePrefixFunction(String pattern) {
            int m = pattern.length();
            int[] prefix = new int[m];
            int k = 0;
            prefix[0] = 0;
            for (int q = 1; q < m; q++) {
                while (k > 0 && pattern.charAt(k) != pattern.charAt(q)) {
                    k = prefix[k - 1];
                }
                if (pattern.charAt(k) == pattern.charAt(q)) {
                    k++;
                }
                prefix[q] = k;
            }
            return prefix;
        }

        public static boolean kmpMatch(String text, String pattern) {
            int n = text.length();
            int m = pattern.length();
            if (m == 0) {
                return true; // An empty pattern matches everything.
            }
            int[] prefix = computePrefixFunction(pattern);
            int q = 0;
            for (int i = 0; i < n; i++) {
                while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                    q = prefix[q - 1];
                }
                if (pattern.charAt(q) == text.charAt(i)) {
                    q++;
                }
                if (q == m) {
                    return true; // Found a match.
                }
            }
            return false;
        }
    }

    static class HuffmanNode {
        char data;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;
    }

    public class HuffmanCompression {
        private static Map<Character, String> huffmanCodes = new HashMap<>();
        private static Map<String, Character> reverseHuffmanCodes = new HashMap<>();

        public static void buildHuffmanTree(String data) {
            Map<Character, Integer> frequencyMap = new HashMap<>();
            for (char c : data.toCharArray()) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }

            PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.frequency));

            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                HuffmanNode node = new HuffmanNode();
                node.data = entry.getKey();
                node.frequency = entry.getValue();
                node.left = null;
                node.right = null;
                priorityQueue.add(node);
            }

            while (priorityQueue.size() > 1) {
                HuffmanNode left = priorityQueue.poll();
                HuffmanNode right = priorityQueue.poll();

                HuffmanNode newNode = new HuffmanNode();
                newNode.data = '-';
                newNode.frequency = left.frequency + right.frequency;
                newNode.left = left;
                newNode.right = right;

                priorityQueue.add(newNode);
            }

            generateHuffmanCodes(priorityQueue.poll(), "");
        }

        private static void generateHuffmanCodes(HuffmanNode root, String code) {
            if (root == null) {
                return;
            }
            if (root.left == null && root.right == null) {
                huffmanCodes.put(root.data, code);
                reverseHuffmanCodes.put(code, root.data);
            }
            generateHuffmanCodes(root.left, code + "0");
            generateHuffmanCodes(root.right, code + "1");
        }

        public static String compress(String data) {
            buildHuffmanTree(data);
            StringBuilder compressedData = new StringBuilder();
            for (char c : data.toCharArray()) {
                compressedData.append(huffmanCodes.get(c));
            }
            return compressedData.toString();
        }

        public static String decompress(String compressedData) {
            StringBuilder decompressedData = new StringBuilder();
            StringBuilder currentCode = new StringBuilder();
            for (char c : compressedData.toCharArray()) {
                currentCode.append(c);
                if (reverseHuffmanCodes.containsKey(currentCode.toString())) {
                    decompressedData.append(reverseHuffmanCodes.get(currentCode.toString()));
                    currentCode.setLength(0);
                }
            }
            return decompressedData.toString();
        }
    }
}
