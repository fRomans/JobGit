package service;

import dao.UserDao;
import model.User;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class UserService {
     private final Connection connection;

    public UserService() {
            this.connection = UserService.getMysqlConnection();
    }


        public List<User> getAllUsers() throws SQLException {
        UserDao dao = getUserDao();
        return dao.getAllUsers();
    }

    public boolean deleteUser(String name) throws SQLException {
        UserDao dao = getUserDao();
        dao.deleteUser(name);
        if (name.equals(dao.getClientByName(name))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addUser(User user) throws SQLException {
        UserDao dao = getUserDao();
        try {
            //connection.setAutoCommit(false);
            dao.addUser(user);//отменил setAutoCommit(false),чтобы изменения тут же фиксировались в базе
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }

        }

        List<User> user2 = getAllUsers();
        for (User user1 : user2) {
            if (user.getPassword().equals(user1.getPassword()) && user.getName().equals(user1.getName()) && user.getMoney().equals(user1.getMoney())) {
                return true;//значит такой клиент(user) появился в базе
            }
        }
        return false;
    }


    public void cleanUp()  {
        UserDao dao = getUserDao();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            System.out.println("!!Ошибка cleanUp!!"+e);
        }
    }

    public void createTable()  {
        UserDao dao = getUserDao();
        try {
            dao.createTable();
        } catch (SQLException e) {
            System.out.println("!!Ошибка createTable!!"+e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=root").       //password
                    append("&serverTimezone=UTC");   //setup server time
            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

        private static UserDao getUserDao () {
            return new UserDao(getMysqlConnection());
        }
    }
