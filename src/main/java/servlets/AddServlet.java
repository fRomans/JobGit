package servlets;

import model.User;
import service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/add", name = "AddServlet")
public class AddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        writer.println("Method GET from AddServlet");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        try {
            userService.createTable();
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(400);
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name;
        String password;
        Long money;
        resp.setContentType("text/html;charset=utf-8");
        name = req.getParameter("name");
        password = req.getParameter("password");
        money =new Long(req.getParameter("money")) ;
        User user = new User(name, password,money);

        try {
            boolean addUser =  new UserService().addUser(user);

            Map<String, Object> pageVariables = new HashMap<>();
            if (addUser){//true по умолчанию
                pageVariables.put("emailReg1", name);

            }else {
                pageVariables.put("emailReg1", "Client not add");
            }
           // resp.getWriter().println(PageGenerator.getInstance()
              //      .getPage("index.jsp", pageVariables));
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter().println(name);
        resp.setStatus(HttpServletResponse.SC_OK);
    }



    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // BankClientService bankClientService = new BankClientService();
        if (req.getPathInfo().contains("all")) {
            try {
                //   bankClientService.cleanUp();
            } catch (Exception e) {
                resp.setStatus(400);
            }
        }
    }

//    private static Map<String, Object> createPageVariablesMap(HttpServletRequest req) {
//        Map<String, Object> pageVariables = new HashMap<>();
//        String email = req.getParameter("email");
//        String password = req.getParameter("password");
//        if (email == null || password == null) {// закрываю ошибку freemarker при null/пустоте
//            System.out.println(" введите логин/пароль");
//            pageVariables.put("emailAuth1", "Введите email");
//            pageVariables.put("passwordAuth1", "Введите password");
//            return pageVariables;
//
//        } else if (email != null & password != null) {
//            pageVariables.put("emailAuth1", email);
//            pageVariables.put("passwordAuth1", password);
//            return pageVariables;
//        }else {
//            pageVariables.put("emailAuth1", "нет email");
//            pageVariables.put("passwordAuth1", "нет password");
//            return pageVariables;
//        }
//    }
}