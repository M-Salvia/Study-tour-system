package com.example.utils;
import com.example.model.*;
import java.util.*;
public class FoodTools {
    public static List<Food> getTop10Foods(List<Food> foods) {
        //这堆排序算法，用于对美食列表进行排序并获取前十名美食
        List<Food> top10Foods = new ArrayList<>();
        int n = foods.size();

        // 构建最大堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(foods, n, i);
        }

        // 从堆中获取前十名
        for (int i = n - 1; i >= n - 10 && i >= 0; i--) {
            top10Foods.add(foods.get(0));
            swap(foods, 0, i);
            heapify(foods, i, 0);
        }

        return top10Foods;
    }

    private static void heapify(List<Food> arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr.get(l).hot2 > arr.get(largest).hot2) {
            largest = l;
        }

        if (r < n && arr.get(r).hot2 > arr.get(largest).hot2) {
            largest = r;
        }

        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    private static void swap(List<Food> arr, int i, int j) {
        Food temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    public static List<Food> getTop10Foods2(List<Food> foods) {
        //堆排序算法，跟以上那个几乎完全相同，不过这次是用评分进行排序前十位
        List<Food> top10Foods = new ArrayList<>();
        int n = foods.size();

        // 构建最大堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify2(foods, n, i);
        }

        // 从堆中获取前十名
        for (int i = n - 1; i >= n - 10 && i >= 0; i--) {
            top10Foods.add(foods.get(0));
            swap2(foods, 0, i);
            heapify2(foods, i, 0);
        }

        return top10Foods;
    }

    private static void heapify2(List<Food> arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr.get(l).rank2 > arr.get(largest).rank2) {
            largest = l;
        }

        if (r < n && arr.get(r).rank2 > arr.get(largest).rank2) {
            largest = r;
        }

        if (largest != i) {
            swap2(arr, i, largest);
            heapify2(arr, n, largest);
        }
    }

    private static void swap2(List<Food> arr, int i, int j) {
        Food temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
