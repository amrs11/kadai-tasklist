package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            Task t = new Task(); //model:Taskのインスタンスを生成（DBとやりとりするため）

            String content = request.getParameter("content"); //フォームに入力された"content"の内容を格納
            t.setContent(content); //DBとやりとりするmodel：Taskにcontent内容をセット　※※IDは自動採番されているため記述なし※※

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreated_at(currentTime);
            t.setUpdated_at(currentTime); //日時情報もセット

            em.persist(t); //model：Taskにセットした内容をDBに登録（永続化）
            em.getTransaction().commit(); // トランザクション（同じデータに対する他のユーザーからのアクセスを遮断）でDBにcommit
            em.close(); // em閉じる

            response.sendRedirect(request.getContextPath() + "/index"); // create処理が終わったのでトップページに遷移
        }
    }

}
