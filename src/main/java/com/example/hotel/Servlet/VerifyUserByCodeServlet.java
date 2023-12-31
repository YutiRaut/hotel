package com.example.hotel.Servlet;

import com.example.hotel.DAO.DAOException;
import com.example.hotel.DAO.UserDao;
import com.example.hotel.Model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VerifyUserByCodeServlet extends HttpServlet {

    UserDao loginDao = new UserDao();
    LoginServlet loginServlet = new LoginServlet();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("CurrentUser");
        String code = req.getParameter("VerificationCode");

        if (code.equals(user.getVerificationCode())) {
            try {
                loginDao.updateUserIsVerified(user.setVerified(true), user.getEmail());
            } catch (DAOException e) {
                e.printStackTrace();
            }
            loginServlet.checkUserRole(req, resp);
        } else {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("VerifyUser.jsp");
            requestDispatcher.forward(req,resp);

        }

    }
}
