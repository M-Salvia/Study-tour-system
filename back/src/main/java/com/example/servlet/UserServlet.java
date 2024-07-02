package com.example.servlet;

import com.example.model.User;
import com.example.dao.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        String pathInfo = req.getPathInfo();
        resp.setContentType("text/html");

        if ("/register".equals(pathInfo)) {
            resp.getWriter().write(
                    "<html><body>" +
                            "<form action='/api/register' method='post'>" +
                            "Username: <input type='text' name='username' /><br>" +
                            "Password: <input type='password' name='password' /><br>" +
                            "<input type='submit' value='Register' />" +
                            "</form>" +
                            "</body></html>"
            );
        } else if ("/login".equals(pathInfo)) {
            resp.getWriter().write(
                    "<html><body>" +
                            "<form action='/api/login' method='post'>" +
                            "Username: <input type='text' name='username' /><br>" +
                            "Password: <input type='password' name='password' /><br>" +
                            "<input type='submit' value='Login' />" +
                            "</form>" +
                            "</body></html>"
            );
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown API path");
        }
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
                case "/register":
                    handleRegister(req, resp);
                    break;
                case "/login":
                    handleLogin(req, resp);
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

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        User user = objectMapper.readValue(req.getInputStream(), User.class);
        if (userDao.getUserByUsername(user.getUsername()) != null) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Username already exists");
            return;
        }

        userDao.saveUser(user);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), user);
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        User loginRequest = objectMapper.readValue(req.getInputStream(), User.class);
        User user = userDao.getUserByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), user);
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
        }
    }

    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8088"); // 根据前端运行的地址修改
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
