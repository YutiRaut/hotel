package com.example.hotel.Servlet;

import com.example.hotel.Common.Constant;
import com.example.hotel.Common.ErrorUtil;
import com.example.hotel.DAO.DAOException;
import com.example.hotel.DAO.UserDao;
import com.example.hotel.Model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    ErrorUtil errorUtil = new ErrorUtil();


    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("username");
        String password = request.getParameter("password");

        User user = null;
        try {
            user = UserDao.checkUserCredentials(name, password);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("CurrentUser", user);
            if (!(user.isVerified())) {
                request.getRequestDispatcher("VerifyUser.jsp").forward(request,response);
            } else {
                checkUserRole(request, response);
            }
        } else {
            errorUtil.addErrorMessage("Invalid Credentials!!");
            request.setAttribute("InvalidError", errorUtil);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("Login.jsp");
            requestDispatcher.forward(request, response);

        }


    }

    public void checkUserRole(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("CurrentUser");

        if (user.getRole() == Constant.SYSTEM_ADMIN) {
            request.getRequestDispatcher("index.jsp").forward(request,response);
        } else if (user.getRole() == Constant.HOTEL_ADMIN) {
            request.getRequestDispatcher("Welcome.jsp").forward(request,response);
        } else if (user.getRole() == Constant.Customer) {
            request.getRequestDispatcher("index.jsp").forward(request,response);
        }

    }


}

