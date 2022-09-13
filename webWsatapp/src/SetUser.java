import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ord.example.User;

@WebServlet({"/log-in"})
public class SetUser
  extends HttpServlet
{
  public void service(HttpServletRequest req, HttpServletResponse res)
    throws IOException
  {
    String user = req.getRemoteUser();
    PrintWriter out = res.getWriter();
    
    out.print(user);
    User u = User.getObj();
    u.setUser(user);
  }
}