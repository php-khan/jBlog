package controllers;

import models.Blog;
import models.Comment;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.util.List;

public class Auth extends Controller
{
	
	/*
	 * Keep track of current user
	 */
	public static User curUser   = null;
	static Form<User> loginForm  = Form.form(User.class);
	static Form<Blog> blogForm   = Form.form(Blog.class);
    static Form<User> signupForm = Form.form(User.class);

	
	/*
	 * Security function is done here.
	 * 
	 * When cookie does match it returns curUser as the instance of an User, or null otherwise
	 */
	public static User curUser() {
		try{
		String cookie =session("connected");
		if(curUser!=null){
			if (!cookie.equals(""+curUser.id)){
				curUser=null;
			}
		}
		return curUser;
		} catch(Exception e) {
            return null;
        }
	}
    
    /*
     * Serve the sign up page
     * route: GET /singup
     */
    public static Result signup() {
    	return ok(views.html.signup.render(signupForm));
    }


    /*
     * Create new user based on signup form submission
     * route: POST /singup
     */
    public static Result signupFormProcess() {
    	Form<User> filledForm = signupForm.bindFromRequest();
          if(filledForm.hasErrors()) {
            return badRequest("very bad");
          } else {
             User c= filledForm.get();
             User.create(c);
            return redirect(routes.Auth.login());
        }
    }

    /*
     * Serve the Login Form
     * route /loginpage
     */
    public static Result login() {
        return ok(views.html.loginpage.render(loginForm));
    }

    /*
     * Processing Login user
     *
     * route: Post /login
     */
    public static Result loginFormProcess() {
        Form<User> filledForm = loginForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest("Somethin is went wrong");
          } else {
             User c= filledForm.get();
             curUser=User.verify(c.username, c.password);
             if (curUser != null) {
                 session("connected", curUser.id.toString());
                 return redirect(routes.Application.index());
             }

             return ok("bad man nanana");
          }
    }

    /*
     * Resposible for Logout
     * It basically Remove the session and redirect to login page
     *
     * route: GET /logout
     */
    public static Result logout() {
    	session().remove("connected");
    	return redirect(routes.Auth.login());
    }
}
