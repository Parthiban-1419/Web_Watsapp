package ord.example;

public class User
{
  static User obj = new User();
  String user = "";
  
  public void setUser(String user)
  {
    this.user = user;
  }
  
  public String getUser()
  {
    return user;
  }
  
  public static User getObj()
  {
    return obj;
  }
}