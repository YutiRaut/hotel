package com.example.hotel.Servlet;


import com.example.hotel.Common.EmailValidator;
import com.example.hotel.Common.OtpGenerator;
import com.example.hotel.DAO.DAOException;
import com.example.hotel.DAO.RoleDao;
import com.example.hotel.DAO.UserDao;
import com.example.hotel.Model.Role;
import com.example.hotel.Model.User;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegistrationServlet extends HttpServlet {
    UserDao loginDao = new UserDao();
   OtpGenerator sendOtp = new OtpGenerator();
    RoleDao roleDao = new RoleDao();
    User user = new User();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<Role> roles = roleDao.getRole();
            req.setAttribute("Role", roles);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("Registration.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
EmailValidator emailValidator= new EmailValidator();

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String contact = req.getParameter("contact");
        String password = req.getParameter("password");
        String role = (req.getParameter("role"));
        String verification = req.getParameter("VerificationCode");

        user.setName(name);
        user.setEmail(email);
        user.setContact(contact);
        user.setPassword(password);
        user.setRole(Integer.parseInt(role));
        String code =sendOtp.generatesOtp();
//                        sendOtp.generatesOtp();

        user.setVerificationCode(code);
        try {
            loginDao.addUser(user);
            sendMail(user);
        } catch (DAOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        req.getRequestDispatcher("Login.jsp").forward(req,resp);

    }

    private void fillSignUpMasterData(HttpServletRequest request) throws SQLException {
        RoleDao roleDao = new RoleDao();
        List<Role> roles = roleDao.getRole();
        request.setAttribute("Role", roles);

    }

    private void sendMail(User user) throws MessagingException {
        EmailValidator sendmail = new EmailValidator();
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<H1>").append("Hi, ").append(user.getName()).append(" ").append("</H1>").append("Your verification code :").append(user.getVerificationCode());
        sendmail.sendMail(user.getEmail(), "Traveller:Account verification code", mailContent.toString());
    }


}

