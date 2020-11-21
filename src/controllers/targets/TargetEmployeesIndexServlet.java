package controllers.targets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class TargetEmployeesIndex
 */
@WebServlet("/target/index")
public class TargetEmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TargetEmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // login_employee = ログインしている社員
        // target_employee = report.employee.id 指定した社員
        // reports = 特定の社員の日報取得
        // follow = フォローしている社員がlogin_employee,フォローされている社員が指定した社員に当てはまるフィールドの数を取得
        // followed = フォローしている社員がlogin_employee,フォローされている社員が指定した社員に当てはまるフィールドのを取得

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Employee target_employee =  em.find(Employee.class, Integer.parseInt(request.getParameter("id")));

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                .setParameter("employee", target_employee)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                .setParameter("employee", target_employee)
                .getSingleResult();

        long follow = (long)em.createNamedQuery("checkFollowerAndFollowed",Long.class)
                .setParameter("login_employee", login_employee)
                .setParameter("target_employee", target_employee)
                .getSingleResult();
        if(follow != 0) {
            Integer followed = (Integer)em.createNamedQuery("getSingleFollow", Integer.class)
                    .setParameter("login_employee", login_employee)
                    .setParameter("target_employee", target_employee)
                    .getSingleResult();
             request.setAttribute("followed", followed);
        }


        em.close();

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        request.setAttribute("target_employee", target_employee);
        request.setAttribute("follow", follow);


        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/target/index.jsp");
        rd.forward(request, response);

    }

}
