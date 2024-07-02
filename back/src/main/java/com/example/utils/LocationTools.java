package com.example.utils;

import java.util.*;
import com.example.model.*;
public class LocationTools {
    //判断两个字符串是否相等
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }

        if (str1 == null || str2 == null) {
            return false;
        }

        int len1 = str1.length();
        int len2 = str2.length();

        if (len1 != len2) {
            return false;
        }

        // 逐个字符比较，忽略大小写
        for (int i = 0; i < len1; i++) {
            char c1 = Character.toLowerCase(str1.charAt(i));
            char c2 = Character.toLowerCase(str2.charAt(i));
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }
    //字符串匹配算法，返回一个匹配到的位置
    public static int Sunday(String text, String pattern) {
        //Sunday算法字符串匹配,用于关键字查找，若返回-1,代表没有找到匹配。
        Map<Character, Integer> shiftTable = generateShiftTable(pattern);// 生成坏字符表
        int textLength = text.length();
        int patternLength = pattern.length();
        int i = 0;// 指向文本的索引
        while (i <= textLength - patternLength) {
            int j = 0;// 指向模式串的索引
            while (j < patternLength && pattern.charAt(j) == text.charAt(i + j)) {
                j++;
            }
            if (j == patternLength) {
                return i;// 找到匹配,返回匹配位置
            } else {
                if (i + patternLength < textLength) {
                    char nextChar = text.charAt(i + patternLength);
                    int shift = shiftTable.getOrDefault(nextChar, patternLength + 1);
                    i += shift;
                } else {
                    return -1;// 如果已经到达文本末尾,则未找到匹配
                }
            }
        }
        return -1;
    }

    private static Map<Character, Integer> generateShiftTable(String pattern) {
        // 生成Sunday算法要用的坏字符表
        Map<Character, Integer> shiftTable = new HashMap<>();
        int patternLength = pattern.length();
        for (int i = patternLength - 1; i >= 0; i--) {
            char c = pattern.charAt(i);
            if (!shiftTable.containsKey(c)) {
                shiftTable.put(c, patternLength - i);
            }
        }
        shiftTable.put(' ', patternLength + 1); // 如果文本中没有模式串中的字符，则移动整个模式串长度
        return shiftTable;
    }


    //递归快排
    public static void quickSort(List<Location> list, int low, int high, Comparator<Location> comparator) {
        if (low <= high) {
            int pivot = partition(list, low, high, comparator);
            quickSort(list, low, pivot - 1, comparator);
            quickSort(list, pivot + 1, high, comparator);
        }
    }


    //给快排用的分治函数
    public static int partition(List<Location> list, int low, int high, Comparator<Location> comparator) {
        Location pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) < 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    
}







