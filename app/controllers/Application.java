package controllers;

import java.util.List;

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
	 * Security function is done here.
	 * 
	 * When cookie does match it returns curUser as the instance of an User, or null otherwise
	 */
	public static User curUser(){
		try{
		String cookie =session("connected");//request().cookies().get("cookie").value();
		if(curUser!=null){
			if (!cookie.equals(""+curUser.id)){
				curUser=null;
			}
		}
		return curUser;
		}catch(Exception e){return null;}
	}
	
	
	/*
	 * Route: /
	 * Shorthadn: Landing page
	 * Description: It shows first 5 Published blog posts.
	 * 
	 * Currently it has no forms.
	 */
	
	
	
    public static Result index() {
    	return page(new Long(1) );
    }
	/*
	 * Route: /pages/(int)
	 * Shorthads: Landing page
	 * 
	 * Description: 
	 */
	
    public static Result page(Long i) {
    	i=i-1;
    	List pBlogs= Blog.published();
        return ok(
        		views.html.index.render(
        				pBlogs.subList((int) (i*5),pBlogs.size()>(i+1)*5?(int)(i+1)*5:(int)pBlogs.size())
        				,new Long((int)Math.ceil(pBlogs.size()/5.0)))
        );
    }
    
    
    
    /*
     * It is basic admin page, contains ever
     */
    
    public static Result admin() {
        return ok(views.html.admin.render(Blog.unpublished(),blogForm,loginForm,curUser(),Blog.published(), Blog.unpublished(),User.find.all()));
    }
    
    /*
     * Landing page post a new blog
     */
    public static Result post() {
    	Form<Blog> filledForm = blogForm.bindFromRequest();
  	  if(filledForm.hasErrors()||curUser()==null) {
  	    return  badRequest(
  	    		
  	      //views.html.index.render(Blog.all()) //,blogForm, loginForm, curUser())
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
  	    		views.html.singleedit.render(Blog.oneblog(id).get(0),blogForm,id,loginForm,curUser())
  	    );
  	  } else {
  		 Blog c= filledForm.get();
  		 c.update(id);
  		 
  	    return redirect(routes.Application.index());
    }
    }
    

    
    
    
    }
