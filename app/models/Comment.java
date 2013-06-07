package models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import controllers.Application;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Comment")
public class Comment extends Model {
	@Id
	Long id;
	
	
	@ManyToOne
	public Blog parentBlog;
	
	@Required
	public String body;
	@Required
	public Date create_time;
	
	@ManyToOne
	public User author;
	
	Comment(){
		 
		create_time=new Date();
	}
	
	public static Finder<Long,Comment> find = new Finder(
		    Long.class, Comment.class
		  );
  
  public static List<Comment> all() {
    return find.all();
  }
  public static List<Comment> oneblog(Long id){
	  List tempList=new <Comment> LinkedList();
	  tempList.add(find.byId(id));
	  return tempList; 
  }
  
  
  public static void create(Comment newpost, Long blogId) {
	  newpost.author=Application.curUser();
	  newpost.parentBlog=Blog.oneblog(blogId).get(0);
	  newpost.save();
  }
  
  public static void delete(Long id) {
	  find.ref(id).delete();
  }
	
}
