package controllers;

import models.Blog;
import models.Comment;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import play.mvc.Http.Cookie;

import views.html.*;

public class Application extends Controller {
	
	
	/*
	 * Keep track of current user
	 */
	public static User curUser=null;
	
	static Form<User> loginForm = Form.form(User.class);
	static Form<Blog> blogForm = Form.form(Blog.class);
	
	/*
	 * check cookies and returns current user
	 */
	public static User curUser(){
		try{
		String cookie =request().cookies().get("cookie").value();
		if(curUser!=null){
			if (!cookie.equals(""+curUser.id)){
				curUser=null;
			}
		}
		return curUser;
		}catch(Exception e){return null;}
	}
	
	
	/*
	 * Landing page, it shows all the blog post, with no comment
	 */
	
    public static Result index() {
        return ok(views.html.index.render(Blog.published(),blogForm,loginForm,curUser()));
    }
    
    public static Result admin() {
        return ok(views.html.admin.render(Blog.unpublished(),blogForm,loginForm,curUser(),Blog.published(), Blog.unpublished()));
    }
    
    /*
     * Landing page post a new blog
     */
    public static Result post() {
    	Form<Blog> filledForm = blogForm.bindFromRequest();
  	  if(filledForm.hasErrors()||curUser()==null) {
  	    return  badRequest(
  	      views.html.index.render(Blog.all(),blogForm, loginForm, curUser())
  	    );
  	  } else {
  		 Blog c= filledForm.get();
  		 
  		 Blog.create(c);
  		 
  	    return redirect(routes.Application.index());
    }
}
    
    /*
     * single page with comments
     */
    static Form<Comment> commentForm = Form.form(Comment.class);
    public static Result single(Long id){
    	return ok(views.html.single.render(Blog.oneblog(id).get(0),commentForm,id,loginForm,curUser()));
    }
    public static Result postComment(Long id) {
    	Form<Comment> filledForm = commentForm.bindFromRequest();
  	  if(filledForm.hasErrors() || curUser()==null) {
  	    return ok("bad"+filledForm.errors());//badRequest(
  	     // views.html.index.render(Blog.find(id),commentForm)
  	    //);
  	  } else {
  		 Comment c= filledForm.get();
  		 
  		 Comment.create(c, id);
  		 
  	    return redirect(routes.Application.index());
    }
  
}
    static Form<User> signupForm = Form.form(User.class);
    
    public static Result editSingle(Long id){
    	blogForm =blogForm.fill(Blog.find.byId(id));
    	return ok(views.html.singleedit.render(Blog.oneblog(id).get(0),blogForm,id,loginForm,curUser()));
    }
    public static Result submitEditSingle(Long id) {
    	Form<Blog> filledForm = blogForm.bindFromRequest();
  	  if(filledForm.hasErrors()||curUser()==null) {
  	    return  badRequest(
  	      views.html.index.render(Blog.all(),blogForm, loginForm, curUser())
  	    );
  	  } else {
  		 Blog c= filledForm.get();
  		 c.update(id);
  		 
  	    return redirect(routes.Application.index());
    }
    }
    
    
    /*
     * User sign up
     */
    public static Result signupPage(){
    	return ok(views.html.signup.render(signupForm,loginForm));
    }
    /*
     * Makes new user
     */
    public static Result signup() {
    	Form<User> filledForm = signupForm.bindFromRequest();
  	  if(filledForm.hasErrors()) {
  	    return badRequest(
  	    		"very bad"
  	      //views.html.index.render(Blog.find(id),commentForm)
  	    );
  	  } else {
  		 User c= filledForm.get();
  		 User.create(c);
  	    return redirect(routes.Application.index());
    }
}
    /*
     * Login user, changes cookies
     */
    public static Result login() {
    	Form<User> filledForm = loginForm.bindFromRequest();
    	  if(filledForm.hasErrors()) {
    	    return badRequest(
    	      //views.html.index.render(Blog.find(id),commentForm)
    	    );
    	  } else {
    		 User c= filledForm.get();
    		 curUser=User.verify(c.username, c.password);
    		 if (curUser!=null){
    			 response().setCookie("cookie" ,""+curUser.id);
    			 return redirect(routes.Application.index());
    		 }
    		 return ok("bad man nanana");
    			 //redirect(routes.Application.index());
      }
    }
    public static Result logout() {
    	response().setCookie("cookie" ,"");
    	return redirect(routes.Application.index());
    }
    
    
    
    }
