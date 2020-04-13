package dao;

import model.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDao {
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    //проверить наличие имени и пароля
    public boolean validateClient(String name, String password) throws SQLException {
        boolean yes = true;
        if (name == null || password == null) {
            yes = false;
        }
        return yes;
    }

    public User getClientByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("select * from bank_client where name='" + name + "'");
        User bankClient = null;
        while (result.next()) {
            bankClient = new User(result.getLong(1), result.getString(2),
                    result.getString(3), result.getLong(4));
        }

        result.close();
        stmt.close();
        return bankClient;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> Userslist = new LinkedList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client ");
        ResultSet result = stmt.getResultSet();
        while (result.next()) {
            Userslist.add(new User(result.getLong(1), result.getString(2),
                    result.getString(3), result.getLong(4)));
        }

        return Userslist;
    }

    public void deleteUser(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("delete * from bank_client where name='" + name + "'");
        stmt.close();
    }

    public void updateUser(String name, String password, Long transactValue) throws SQLException {
        if (validateClient(name, password)) {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("select money from bank_client where name='" + name + "'");
            result.next();
            Long balanceOfMoney = result.getLong("money");
            result.close();
            stmt.close();
            if (balanceOfMoney > transactValue) transactValue = -transactValue;

            try {
                String updata = "update  bank_client set money=? where name='" + name + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(updata);
                preparedStatement.setLong(1, transactValue);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println();
                e.printStackTrace();
            }

        } else {
            System.out.println(" не пройдена валидация при updateClientsMoney");
        }
    }

    public void addUser(User user) throws SQLException {
//проверить наличие имени и пароля
        if (!validateClient(user.getName(), user.getPassword())) {
            System.out.println("!!! Не прошло валидацию!!!");
            return;
        }
        Statement stmt1 = connection.createStatement();
        ResultSet result1 = stmt1.executeQuery("select * from bank_client ");
        String nameClient = user.getName();
        PreparedStatement stmt = connection
                .prepareStatement("insert into  `bank_client`(name, password, money) values(?,?,?)");
        List nameList = new LinkedList();

        while (result1.next()) {
            nameList.add(result1.getString(2));
        }
        if (!nameList.contains(nameClient)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setLong(3, user.getMoney());
            stmt.executeUpdate();
            result1.close();
            stmt.close();
        } else {
            System.out.println("В базе есть такой клиент");
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists user_db (id bigint auto_increment," +
                " name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS user_db");
        stmt.close();
    }

}