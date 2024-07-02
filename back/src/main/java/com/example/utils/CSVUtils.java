package com.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.model.*;

public class CSVUtils {
    private static final List<Place> places = CSVUtils.readPlacesFromCSV("D:\\Aack\\arch\\src\\main\\resources\\bupt_locations.csv");

    public static List<Location> readLocationsFromCSV(String filePath) {
        List<Location> locations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Location location = new Location();
                location.setId(Integer.parseInt(values[0]));
                location.setName(values[1]);
                location.setConvenience(Integer.parseInt(values[2]));
                location.setQo(Integer.parseInt(values[3]));
                location.setFlow(Integer.parseInt(values[4]));
                location.setRatio(Integer.parseInt(values[5]));
                location.setHumanity(Integer.parseInt(values[6]));
                location.setNature(Integer.parseInt(values[7]));
                location.setKey1(values[8]);
                location.setHot(Integer.parseInt(values[9]));
                locations.add(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }
    public static List<Place> readPlacesFromCSV(String filePath) {
        List<Place> places = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Place place = new Place();
                place.setId(Integer.parseInt(values[0]));
                place.setName(values[1]);
                place.setLatitude(Double.parseDouble(values[2]));
                place.setLongitude(Double.parseDouble(values[3]));
                place.setType(values[4]);
                places.add(place);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }
    public static List<Edge> readEdgesFromCSV(String filePath) {
        List<Edge> edges = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                int start = Integer.parseInt(values[1]);
                int end = Integer.parseInt(values[2]);
                int speed = Integer.parseInt(values[3]);
                double length = calculateDistance(start, end); // 根据起始点和终点计算距离
                Edge edge = new Edge(id, speed, start, end, length);
                edge.setCongestion(Math.random()); // 随机生成拥挤度
                edges.add(edge);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edges;
    }

    private static double calculateDistance(int startId, int endId) {
        // 计算两个地点之间的距离，这里可以实现实际的经纬度计算逻辑
        // 假设已经有places列表，且每个place对象有latitude和longitude属性
        Place start = places.get(startId);
        Place end = places.get(endId);

        double lat1 = start.getLatitude();
        double lon1 = start.getLongitude();
        double lat2 = end.getLatitude();
        double lon2 = end.getLongitude();

        // 使用简单的欧氏距离计算
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2)) * 111000; // 转换为米
    }
}
