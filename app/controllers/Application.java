package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.Task;
import static play.data.Form.form;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.System;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Application extends Controller {

    static Form<Task> taskForm = form(Task.class);

    public static Result index() {
        return redirect(routes.Application.tasks());

    }


    public static Result tasks() {
       // DateTime firstDayOfMonth = new DateTime().withDayOfMonth(1);
       // DateTime lastDayOfMonth = new DateTime().dayOfMonth().withMaximumValue();
        Calendar firstDayOfMonth = new GregorianCalendar();
        Calendar lastDayOfMonth = new GregorianCalendar();
        firstDayOfMonth.add(Calendar.DAY_OF_MONTH,-5);
        lastDayOfMonth.add(Calendar.DAY_OF_MONTH,1);

        List<Task> tasks = Task.finder.where()
//               .between("Birthday", firstDayOfMonth, lastDayOfMonth)
               .orderBy("Birthday asc")
               .findList();
        return ok(views.html.index.render(tasks, taskForm));
    }

    public static Calendar parseTimestamp(String timestamp) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    public static Result newTask() {
        System.out.println("newTask");
        Form<Task> filledForm = taskForm.bindFromRequest();

//        if(filledForm.hasErrors()) {
//            System.out.println("newTask error");
//            System.out.println(filledForm);
//            return badRequest(
//                    views.html.index.render(Task.all(), filledForm)
//            );
//        } else {
//            System.out.println("newTask ok");
//            Task.create(filledForm.get());
//            System.out.println("new Task created");
//            return redirect(routes.Application.tasks());
            DynamicForm requestData = Form.form().bindFromRequest();

            String cal = requestData.get("Birthday");
            System.out.println(cal);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            models.Task task = new models.Task();
            task.setName(requestData.get("Name"));
            task.setBirthday(sdf.format(parseTimestamp(cal)));
            task.setGift(requestData.get("Gift"));
            task.setGiftStatus(requestData.get("GiftStatus"));

            task.save();
        } catch(Exception e) {
            System.out.println(e.toString());
        }


        return redirect(routes.Application.tasks());
//        }

    }

    public static Result editTask(Long id){
        // Найти его в базе
        Task t = Task.finder.ref(id);
        // Cоздать форму поверх этого объекта
        //    Form<Task> filledForm = taskForm.fill(t);
        // отрисовать шаблорн edit
        return ok(
                views.html.edit.render(taskForm.fill(t)));

    }

    public static Result updateTask() {
        Form<Task> filledForm = taskForm.bindFromRequest();
        System.out.println(filledForm.get().Name);
        if(filledForm.hasErrors()) {
            return badRequest(
                    views.html.edit.render(filledForm)
            );
        }
        else {
            Task task = filledForm.get();
            task.update();
            return redirect(routes.Application.tasks());
        }
    }

    public static Result deleteTask(Long id) {
        Task.delete(id);
        return redirect(routes.Application.tasks());
    }


}