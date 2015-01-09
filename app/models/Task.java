package models;

import java.lang.String;
import java.util.*;
import java.util.Date;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import javax.persistence.*;

@Entity
public class Task extends Model {

    @Id
    public Long id;


    @Required
    public Date PlanDate;
    public String PlanName;
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

    public Date getPlanDate() {
        return PlanDate;
    }

    public String getPlanName() {
        return PlanName;
    }

    public String getGift() {
        return Gift;
    }

    public void setPlanDate(Date planDate) {
        PlanDate = planDate;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public void setGift(String gift) {
        Gift = gift;
    }
}