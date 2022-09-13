import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ord.example.User;

@WebServlet({"/get-user"})
public class GetUser
  extends HttpServlet
{
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    User user = User.getObj();
    PrintWriter out = response.getWriter();
    
    out.print(user.getUser());
    
  }
}