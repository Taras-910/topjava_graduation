package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.web.dish.DishController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class MenuServlet extends HttpServlet {
    private Logger log = LoggerFactory.getLogger(MenuServlet.class);
    private DishController dishController;
    private ClassPathXmlApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        dishController = context.getBean(DishController.class);
    }

    @Override
    public void destroy() {
        context.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
/*
        Dish dish = new Dish(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            dishController.create(dish);
        } else {
            dishController.update(meal, getId(request));
        }
*/
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete" -> {
                int id = getId(request);
 //               dishController.delete(id);
                response.sendRedirect("meals");
            }
            case "create", "update" -> {
                final Dish dish = "create".equals(action) ?
                        new Dish(null, null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toLocalDate(), 0.0F) :
                        dishController.get(getId(request),          getId(request));
                request.setAttribute("dish", dish);
                request.getRequestDispatcher("/dishForm.jsp").forward(request, response);
            }
            case "filter" -> {
                LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
                LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
                LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
                LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
//                request.setAttribute("dishes", dishController.getBetween(startDate, startTime, endDate, endTime));
                request.getRequestDispatcher("/menus.jsp").forward(request, response);
            }
            default -> {
//                request.setAttribute("dishes", dishController.getAll());
                request.getRequestDispatcher("/menus.jsp").forward(request, response);
            }
        }
    }

    private int getId (HttpServletRequest request){
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
