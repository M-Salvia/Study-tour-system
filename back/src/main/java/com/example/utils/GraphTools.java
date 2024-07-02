package com.example.utils;

import com.example.model.Graph;
import com.example.model.Place;
import java.util.*;

public class GraphTools {
    private static final int INF = Integer.MAX_VALUE;
    private static final List<Place> places = CSVUtils.readPlacesFromCSV("D:\\Aack\\arch\\src\\main\\resources\\bupt_locations.csv");

    // 递归重构路径的方法
    public static List<String> dijkstra4(List<Place> placeList, double[][] graph, int start, int end) {
        // 重构的dijkstra2算法，用于求两点间最短路径或最短通行时间，返回一个包含最短路径上地点名称的列表。
        int n = graph.length;
        double[] dist = new double[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minDistance2(dist, visited);
            visited[u] = true;
            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Double.POSITIVE_INFINITY && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    prev[v] = u;
                }
            }
        }

        return getPath(placeList, start, end, prev);
    }



    private static List<String> getPath(List<Place> placeList, int start, int end, int[] prev) {
        // 返回从起点到终点的路径上的地点名称列表
        List<Integer> path = new ArrayList<>();
        for (int at = end; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);

        List<String> pathNames = new ArrayList<>();
        for (int index : path) {
            for (Place p : placeList) {
                if (p.id == index) {
                    pathNames.add(p.name);
                    break;
                }
            }
        }
        return pathNames;
    }
    private static int minDistance2(double[] dist, boolean[] visited) {
        //求两点间最短距离，被dijkstra算法调用
        double min = INF;
        int minIndex = -1;
        for (int v = 0; v < dist.length; v++) {// 如果顶点 v 尚未访问且到达顶点 v 的距离比当前最小距离小或相等
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;//更新最小距离
            }
        }
        return minIndex;
    }

    public static List<Place> floyd(Graph graph, int numNodes, int source, List<Place> placelist) {
        double[][] dist = new double[numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                dist[i][j] = graph.Matrix1[i][j];
            }
        }

        for (int k = 0; k < numNodes; k++) {
            for (int i = 0; i < numNodes; i++) {
                for (int j = 0; j < numNodes; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        List<Place> placeout = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            if (i == source) continue;
            if (dist[source][i] < 300) {
                Place place = new Place(placelist.get(i).getId(), placelist.get(i).getName(),
                        placelist.get(i).getLatitude(), placelist.get(i).getLongitude(), placelist.get(i).getType());
                placeout.add(place);
            }
        }

        quickSort(placeout, 0, placeout.size() - 1, Comparator.comparing(Place::getLatitude));
        return placeout;
    }

    public static void quickSort(List<Place> list, int low, int high, Comparator<Place> comparator) {
        if (low <= high) {
            int pivot = partition(list, low, high, comparator);
            quickSort(list, low, pivot - 1, comparator);
            quickSort(list, pivot + 1, high, comparator);
        }
    }

    private static int partition(List<Place> list, int low, int high, Comparator<Place> comparator) {
        Place pivot = list.get(high);
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


    public static List<Integer> solveTSP(double[][] dist) {
        //旅行商问题 用于求途径多点的最短路径
        int n = dist.length;
        double[][] dp = new double[1 << n][n];
        int[][] path = new int[1 << n][n];

        for (double[] row : dp) {
            Arrays.fill(row, INF);
        }

        // 初始化
        dp[1][0] = 0;

        // 动态规划
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) continue;

                for (int j = 0; j < n; j++) {
                    if ((mask & (1 << j)) != 0) continue;

                    int nextMask = mask | (1 << j);
                    double newDist = dp[mask][i] + dist[i][j];

                    if (newDist < dp[nextMask][j]) {
                        dp[nextMask][j] = newDist;
                        path[nextMask][j] = i;
                    }
                }
            }
        }

        // 回溯找路径
        List<Integer> result = new ArrayList<>();
        result.add(0);
        int mask = (1 << n) - 1;
        int last = 0;

        for (int i = 1; i < n; i++) {
            if (dp[mask][i] + dist[i][0] < dp[mask][last] + dist[last][0]) {
                last = i;
            }
        }

        for (int i = 0; i < n; i++) {
            result.add(last);
            int temp = last;
            last = path[mask][last];
            mask ^= (1 << temp);
        }

        return result;
    }

    public static List<String> multiplepoint(List<Place>placeList,List<Integer> midpoint,double[][] Matrix1,String source){
        int multiple=1;
        for(int i=0;i<midpoint.size();i++){
            multiple*=i+1;
        }
        double[][] juli=new double[midpoint.size()][midpoint.size()];
        for(int i=0;i<midpoint.size();i++){
            for(int j=i+1;j<midpoint.size();j++){
                juli[i][j]=GraphTools.dijkstra3(placeList,Matrix1,midpoint.get(i),midpoint.get(j));
                juli[j][i]=GraphTools.dijkstra3(placeList,Matrix1,midpoint.get(i),midpoint.get(j));
            }
        }
        List<Integer>order=new ArrayList<>();
        order=GraphTools.solveTSP(juli);
        List<String> waittoprint=new ArrayList<>();
        List<String> listplus=new ArrayList<>();
        for(int i=0;i< order.size();i++){
            if(i!=order.size()-1) {
                listplus=GraphTools.dijkstra4(placeList, Matrix1, midpoint.get(order.get(i)), midpoint.get(order.get(i + 1)));
                waittoprint.addAll(listplus.subList(0,listplus.size()-1));

            }
            else{
                List<String> new1=GraphTools.dijkstra4(placeList, Matrix1, midpoint.get(order.get(i)), getPlaceIdByName(source));
               waittoprint.addAll(new1);
            }
        }
        return waittoprint;
    }
    private static int getPlaceIdByName(String name) {
        return places.stream()
                .filter(place -> place.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Place not found: " + name))
                .getId();
    }
    public static double dijkstra3(List<Place> placeList, double[][] graph, int start, int end) {
        //重构的dijkstra3算法，用于求两点间最短路径或最短通行时间，返回一个double类型数值，
        //可以是距离（米），可以是时间（分钟）。
        //其中调用mindistance算法求最小距离
        // 调用printpath算法，打印距离最短路径或者最短时间路径。
        int n = graph.length;
        double[] dist = new double[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minDistance2(dist, visited);
            visited[u] = true;
            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Double.POSITIVE_INFINITY && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    prev[v] = u;
                }
            }
        }
        return dist[end];
    }
}
