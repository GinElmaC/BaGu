package com.GinElmaC;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
//        MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactory();
//        UserMapper userMapper = mySqlSessionFactory.getMapper(UserMapper.class);
//        User user = userMapper.getUserById(1);
        User user = jdbc(1);
        System.out.println(user.toString());
    }

    /**
     * 传统的JDBC
     * @param id
     * @return
     */
    private static User jdbc(int id){
        String jdbcURL = "jdbc:mysql://127.0.0.1:3306/test";
        String dbuser = "root";
        String password = "1998229wang";

        String sql = "select id,name,age from user where id = ?";

        try {
            Connection conn = DriverManager.getConnection(jdbcURL,dbuser,password);
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                User user = new User(res.getString("name"),res.getInt("age"),res.getInt("id"));
                return user;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
