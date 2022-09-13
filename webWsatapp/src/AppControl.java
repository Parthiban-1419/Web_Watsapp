import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/app")
public class AppControl extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
    	HttpSession session = req.getSession();
        
        session.setAttribute("userId",req.getRemoteUser());
        session.setAttribute("scheme",req.getScheme());
        session.setAttribute("serverName",req.getServerName());
        session.setAttribute("serverPort",req.getServerPort()+"");
        System.out.println("user" + req.getRemoteUser());
//        RequestDispatcher rd = req.getRequestDispatcher("index.html");
//        rd.forward(req, res);
        out.print("{'user' : '" + req.getRemoteUser() + "'}");
        
    }
}