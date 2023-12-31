package com.example.hotel.DAO;



import com.example.hotel.Model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoleDao {

    public List<Role> getRole() throws SQLException {
        try {
            List<Role> role = new ArrayList<>();
            Statement statement = DbConnection.getInstance().getMainConnection().createStatement();
            ResultSet rs = statement.executeQuery("select * From role ");
            while (rs.next()) {
                Role role1 = new Role();
                role1.setRole(rs.getInt(1));
                role1.setRoleName(rs.getString(2));
                role.add(role1);
            }
            return role;
        } catch (SQLException ex) {
            throw new SQLException("Something went wrong...", ex);
        }


    }
}
