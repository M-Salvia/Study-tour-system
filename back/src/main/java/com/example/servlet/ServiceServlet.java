package com.example.servlet;

import com.example.model.*;
import com.example.dao.*;
import com.example.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet("/api/service/*")
public class ServiceServlet extends HttpServlet {
    private static final int INF = Integer.MAX_VALUE;
    private final UserDao userDao = new UserDao();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Location> locations = CSVUtils.readLocationsFromCSV("D:\\Aack\\arch\\src\\main\\resources\\new_data.csv");
    private final List<Place> places = CSVUtils.readPlacesFromCSV("D:\\Aack\\arch\\src\\main\\resources\\bupt_locations.csv");
    private final List<Edge> edges = CSVUtils.readEdgesFromCSV("D:\\Aack\\arch\\src\\main\\resources\\new_edgedata.csv");
    private Graph graph;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            initGraph();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize graph", e);
        }
    }

    private void initGraph() throws IOException {
        int numNodes = places.size();
        graph = new Graph(numNodes);
        for(int i=0;i<edges.size();i++){
            graph.Matrix1[edges.get(i).start][edges.get(i).end]=edges.get(i).length;
            graph.Matrix1[edges.get(i).end][edges.get(i).start]=edges.get(i).length;
        }
        for(int i=0;i<edges.size();i++){
            graph.Matrix2[edges.get(i).start][edges.get(i).end]=edges.get(i).length/edges.get(i).speed;
            graph.Matrix2[edges.get(i).end][edges.get(i).start]=edges.get(i).length/edges.get(i).speed;
        }
    }
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
            return;
        }

        try {
            switch (pathInfo) {
                case "/person":
                    handlePerson(req, resp);
                    break;
                case "/all-log":
                    handleAllLog(req, resp);
                    break;
                case "/re-log":
                    handleReLog(req, resp);
                    break;
                case "/write-log":
                    handleWriteLog(req, resp);
                    break;
                case "/search-log":
                    handleSearchLog(req, resp);
                    break;
                case "/update-log":
                    handleUpdateLog(req, resp);
                    break;
                case "/recommendations":
                    handleRecommendations(req, resp);
                    break;
                case "/search-loc":
                    handleSearchLoc(req, resp);
                    break;
                case "/search-place":
                    handleSearchPlace(req, resp);
                    break;
                case "/nearby-places":
                    handleNearbyPlaces(req, resp);
                    break;
                case "/plan-route":
                    handlePlanRoute(req, resp);
                    break;
                case "/plan-route-time":
                    handlePlanRouteTime(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown API path");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void handlePerson(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        UserPreferencesRequest preferencesRequest = objectMapper.readValue(req.getInputStream(), UserPreferencesRequest.class);
        User user = userDao.getUserByUsername(preferencesRequest.getUsername());
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        user.setTraffic_convenience(preferencesRequest.getPreferences().getTrafficConvenience());
        user.setService_quality(preferencesRequest.getPreferences().getServiceQuality());
        user.setVisitor_flow_rate(preferencesRequest.getPreferences().getVisitorFlowRate());
        user.setCultural_atmosphere(preferencesRequest.getPreferences().getCulturalAtmosphere());
        user.setNatural_landscape(preferencesRequest.getPreferences().getNaturalLandscape());
        user.setPrice_performance_ratio(preferencesRequest.getPreferences().getPricePerformanceRatio());

        userDao.updateUserPreferences(user);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), user);
    }

    private void handleAllLog(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        List<Diary> diaries = DiaryDao.getAllDiaries();
        for (Diary diary : diaries) {
            diary.setContent(LogTools.HuffmanCompression.decompress(diary.getContent()));
        }
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), diaries);
    }

    private void handleReLog(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String authorName = req.getParameter("authorName");
        List<Diary> diaries = DiaryDao.getRecommendedDiaries(authorName);
        for (Diary diary : diaries) {
            diary.setContent(LogTools.HuffmanCompression.decompress(diary.getContent()));
        }
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), diaries);
    }

    private void handleWriteLog(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Diary diary = objectMapper.readValue(req.getInputStream(), Diary.class);
        diary.setContent(LogTools.HuffmanCompression.compress(diary.getContent()));
        DiaryDao.saveDiary(diary);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), diary);
    }
    private void handleSearchLog(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String searchQuery = req.getParameter("query");
//        System.out.println(searchQuery);
        if (searchQuery.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search query is required");
            return;
        }
        List<Diary> diaries = DiaryDao.searchDiariesByTitle(searchQuery);
        for (Diary diary : diaries) {
            diary.setContent(LogTools.HuffmanCompression.decompress(diary.getContent()));
        }
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), diaries);
    }
//    private void handleSearchLog(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader reader = req.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//        String body = sb.toString();
//        Map<String, String> params = objectMapper.readValue(body, Map.class);
//
//        String searchQuery = params.get("query");
//        if (searchQuery == null || searchQuery.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search query is required");
//            return;
//        }
//        List<Diary> diaries = diaryDao.searchDiariesByTitle(searchQuery);
//        resp.setContentType("application/json");
//        objectMapper.writeValue(resp.getOutputStream(), diaries);
//    }

    private void handleUpdateLog(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Diary diary = objectMapper.readValue(req.getInputStream(), Diary.class);
        DiaryDao.updateDiary(diary);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), diary);
    }
    private void handleRecommendations(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        try {
            // 从请求体中读取 JSON 数据并解析为 Map
            Map<String, String> requestMap = objectMapper.readValue(req.getInputStream(), Map.class);
            String username = requestMap.get("username");

            // 打印接收到的用户名以便调试
            System.out.println("Parsed username: " + username);

            // 根据用户名查找用户
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            // 获取推荐的景点列表
            List<Location> recommendations = getRecommendations(user);

            // 设置响应内容类型为 JSON
            resp.setContentType("application/json");

            // 将推荐的景点列表写入响应体
            objectMapper.writeValue(resp.getOutputStream(), recommendations);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }


    private List<Location> getRecommendations(User user) {
        for (Location location : locations) {
            int score = calculateScore(location, user);
            location.setHot(score);
        }

        // 使用自定义的快速排序算法对景点进行排序
        LocationTools.quickSort(locations, 0, locations.size() - 1, Comparator.comparingInt(Location::getHot).reversed());

        return locations.stream().limit(10).collect(Collectors.toList());
    }

    private int calculateScore(Location location, User user) {
        int score = 0;
        score += location.getConvenience() * user.getTraffic_convenience();
        score += location.getQo() * user.getService_quality();
        score += location.getFlow() * user.getVisitor_flow_rate();
        score += location.getRatio() * user.getPrice_performance_ratio();
        score += location.getHumanity() * user.getCultural_atmosphere();
        score += location.getNature() * user.getNatural_landscape();
        return score;
    }
    private void handleSearchLoc(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> requestMap = objectMapper.readValue(req.getInputStream(), Map.class);
        String searchQuery = requestMap.get("query");

        if (searchQuery == null || searchQuery.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search query is required");
            return;
        }
        System.out.println(searchQuery);
        List<String> matchingLocationNames = locations.stream()
                .filter(location -> LocationTools.Sunday(location.getName(), searchQuery) != -1 ||
                        LocationTools.Sunday(location.getKey1(), searchQuery) != -1)
                .map(Location::getName)
                .collect(Collectors.toList());

        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), matchingLocationNames);
    }
    private void handleSearchPlace(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> requestMap = objectMapper.readValue(req.getInputStream(), Map.class);
        String query = requestMap.get("query");

        System.out.println(query);
        if (query == null || query.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search query is required");
            return;
        }

        System.out.println(query);
        List<Place> matchingPlaces = places.stream()
                .filter(place -> LocationTools.Sunday(place.getName(), query) != -1 ||
                        LocationTools.Sunday(place.getType(), query) != -1)
                .collect(Collectors.toList());
        System.out.println(matchingPlaces);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), matchingPlaces);
    }
    private void handleNearbyPlaces(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Place placeRequest = objectMapper.readValue(req.getInputStream(), Place.class);
        Integer sourceId = placeRequest.getId();

        if (sourceId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required");
            return;
        }

        List<Place> nearbyPlaces = GraphTools.floyd(graph, places.size(), sourceId, places);

        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), nearbyPlaces);
    }

    private void handlePlanRouteTime(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Time");
        RouteRequest routeRequest = objectMapper.readValue(req.getInputStream(), RouteRequest.class);
        List<String> waypointsName = routeRequest.getWaypoints();
        List<String> pathNames = new ArrayList<>();
        String startName = routeRequest.getStart();
        int startId = getPlaceIdByName(routeRequest.getStart());
        int endId = getPlaceIdByName(routeRequest.getEnd());
        System.out.println(startId);
        System.out.println(endId);
        System.out.println(waypointsName);
        List<Integer>mid=new ArrayList<>();
        for(int i=0;i<waypointsName.size();i++)
        {
            int midId = getPlaceIdByName(waypointsName.get(i));
            mid.add(midId);
        }
        mid.add(endId);
        if(!waypointsName.isEmpty())
        {
            pathNames = GraphTools.multiplepoint(places,mid,graph.Matrix2,startName);
        }
        else {
            pathNames = GraphTools.dijkstra4(places, graph.Matrix2, startId, endId);
        }
        // 获取路径名字列表

        System.out.println(pathNames);
        // 从 places 中查询到对应的 Place 对象
        List<Place> pathPlaces = pathNames.stream()
                .map(name -> getPlaceByName(name))
                .collect(Collectors.toList());

        // 设置响应内容类型为 JSON
        resp.setContentType("application/json");
        // 将 pathPlaces 序列化并写入响应输出流
        objectMapper.writeValue(resp.getOutputStream(), pathPlaces);
    }
    private void handlePlanRoute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Distance");
        RouteRequest routeRequest = objectMapper.readValue(req.getInputStream(), RouteRequest.class);
        List<String> waypointsName = routeRequest.getWaypoints();
        List<String> pathNames = new ArrayList<>();
        String startName = routeRequest.getStart();
        int startId = getPlaceIdByName(routeRequest.getStart());
        int endId = getPlaceIdByName(routeRequest.getEnd());
        System.out.println(startId);
        System.out.println(endId);
        System.out.println(waypointsName);
        List<Integer>mid=new ArrayList<>();
        for(int i=0;i<waypointsName.size();i++)
        {
            int midId = getPlaceIdByName(waypointsName.get(i));
            mid.add(midId);
        }
        mid.add(endId);
        if(!waypointsName.isEmpty())
        {
            pathNames = GraphTools.multiplepoint(places,mid,graph.Matrix1,startName);
        }
        else {
            pathNames = GraphTools.dijkstra4(places, graph.Matrix1, startId, endId);
        }
        // 获取路径名字列表

        System.out.println(pathNames);
        // 从 places 中查询到对应的 Place 对象
        List<Place> pathPlaces = pathNames.stream()
                .map(name -> getPlaceByName(name))
                .collect(Collectors.toList());

        // 设置响应内容类型为 JSON
        resp.setContentType("application/json");
        // 将 pathPlaces 序列化并写入响应输出流
        objectMapper.writeValue(resp.getOutputStream(), pathPlaces);
    }

    private Place getPlaceByName(String name) {
        // 从 places 中查找对应的 Place 对象
        return places.stream()
                .filter(place -> place.getName().equals(name))
                .findFirst()
                .orElse(null); // 这里可以根据实际需求决定是否抛出异常或返回一个默认值
    }
    private int getPlaceIdByName(String name) {
        return places.stream()
                .filter(place -> place.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Place not found: " + name))
                .getId();
    }
    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8088"); // 根据前端运行的地址修改
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
