package models;

import java.lang.Boolean;
import java.lang.String;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Task extends Model {

    @Id
    public Long id;


    @Required
    public String GiftStatus;
    public Date Birthday;
    public String Name;
    public String Gift;


    public static List<Task> all() {
        return finder.all();
    }

    public static void create(Task task) {
        task.save();
    }

    public static void delete(Long id) {
        finder.ref(id).delete();
    }


    public static Finder<Long,Task> finder = new Finder(
            Long.class, Task.class
    );

    public Date getBirthday() {
        return Birthday;
    }

    public String getName() {
        return Name;
    }

    public String getGift() {
        return Gift;
    }

    public String getGiftStatus() {
        return GiftStatus;
    }

    public void setBirthday(Date birthday) {
        Birthday = birthday;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setGift(String gift) {
        Gift = gift;
    }

    public void setGiftStatus(String giftStatus) {
        GiftStatus = giftStatus;
    }
}