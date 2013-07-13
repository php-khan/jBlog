package models;
import javax.validation.Constraint;
import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

import com.avaje.ebean.ExpressionList;

import controllers.Application;


@Entity
public class User extends Model {
    @Id
	public Long id;
	
	@Column(unique=true)
	@Required
	public String username;
	
	@Required
	public String password;

	
	@Required
	public Date create_date;
	
	
	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="author")
	public List<Comment> comments;

	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="author")
	public List<Blog> blogs;
	
	User(){
		create_date=new Date();
	}
	
	public static User verify(String username, String password){
		return find.where()
	            .eq("username", username)
	            .eq("password", password).findUnique();	
	}
	  
    public static Finder<Long,User> find = new Finder(Long.class, User.class);

    public static void create(User newuser) {

      newuser.save();
    }

   public static void delete(Long id) {
      find.ref(id).delete();
   }

    public static User findById(Long id) {
        return find.byId(id);
    }
}
