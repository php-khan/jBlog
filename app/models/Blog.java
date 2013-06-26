package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import scala.xml.Text;

import javax.persistence.*;

import com.avaje.ebean.Expr;

import controllers.Application;

@Entity
public class Blog extends Model{
  
  @Id  
  public Long id;
  
  @Required
  public String title;
  
  @Required
  public Date create_time;
  
  @Required
  public boolean publish;
  
  @javax.persistence.Column(columnDefinition="TEXT")
  @Required
  public String content;
  
  @ManyToOne
  public User author;
  
  
  Blog(){
	  
	  create_time=new Date();
  }
  

  @OneToMany(cascade=CascadeType.PERSIST,mappedBy = "parentBlog")
  public List <Comment> comments;
  
  
  public static Finder<Long,Blog> find = new Finder(
		    Long.class, Blog.class
		  );
  
  public static List<Blog> all() {
    return find.all();
  }
  public static List<Blog> published() {
	    return find.where(Expr.eq("publish",new Boolean(true))).findList();
	  }
  public static List<Blog> unpublished() {
	    return find.where(Expr.eq("publish",new Boolean(false))).findList();
	  }
  
  
  public static List<Blog> oneblog(Long id){
	  List tempList=new <Blog> LinkedList();
	  tempList.add(find.byId(id));
	  return tempList; 
  }
  
  
  public static void create(Blog newpost) {
	  newpost.author=Application.curUser();
	  newpost.save();
  }
  
  public static void delete(Long id) {
	  find.ref(id).delete();
  }
    
}