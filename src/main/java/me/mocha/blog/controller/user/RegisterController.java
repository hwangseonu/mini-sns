package me.mocha.blog.controller.user;

import me.mocha.blog.model.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet({"/register"})
public class RegisterController extends HttpServlet {

    private final UserService userService = new UserService();
    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        if (strEmpty(username) || strEmpty(password) || strEmpty(nickname)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/register.jsp");
            req.setAttribute("message", "빈칸이 있습니다.");
            dispatcher.forward(req, res);
            return;
        }

        try {
            if (userService.createUser(username, password, nickname)) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
            } else {
                throw new RuntimeException("register failed!");
            }
        } catch (Exception ex) {
            log.warning("register error - " + ex.getMessage());
            RequestDispatcher dispatcher = req.getRequestDispatcher("/login.jsp");
            req.setAttribute("message", ex.getMessage());
            dispatcher.forward(req, res);
        }
    }

    private boolean strEmpty(String msg) {
        return msg == null || msg.isEmpty();
    }

}
