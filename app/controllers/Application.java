package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.Task;
import static play.data.Form.form;
import play.libs.Json;
import play.mvc.BodyParser;

import java.lang.Exception;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.System;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller {

    static Form<Task> taskForm = form(Task.class);

    public static Result index() {
        return redirect(routes.Application.tasks());

    }


    public static Result tasks() {
       // DateTime firstDayOfMonth = new DateTime().withDayOfMonth(1);
       // DateTime lastDayOfMonth = new DateTime().dayOfMonth().withMaximumValue();
//        Calendar firstDayOfMonth = new GregorianCalendar();
//        Calendar lastDayOfMonth = new GregorianCalendar();
//        firstDayOfMonth.add(Calendar.DAY_OF_MONTH,-5);
//        lastDayOfMonth.add(Calendar.DAY_OF_MONTH,1);

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
            System.out.println(filledForm);

        if (filledForm.hasErrors()) {
            System.out.println("newTask error");
            System.out.println(filledForm);
            return badRequest(
                    views.html.index.render(Task.all(), filledForm)
            );
        } else {
            System.out.println("newTask ok");
            Task.create(filledForm.get());
            System.out.println("new Task created");
            return redirect(routes.Application.tasks());
        }
    }

    public static Result editTask(Long id){
        // Найти его в базе
        Task t = Task.finder.ref(id);
        // Cоздать форму поверх этого объекта
        Form<Task> filledForm = taskForm.fill(t);
        // отрисовать шаблорн edit
        return ok(
                views.html.edit.render(filledForm ));

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

    public static Result tasks2(){
        List<Task> tasks = Task.finder.where()
                .orderBy("Birthday asc")
                .findList();

        JsonNode tasksListJSON = Json.toJson(tasks);

        ObjectNode result = Json.newObject();

        result.put("objects", tasksListJSON);
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result updTask2(){
        JsonNode request = request().body().asJson();
        System.out.println(request);
        return TODO;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result createTask2(){
        JsonNode request = request().body().asJson();
        System.out.println(request);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        try{
            date       = dateFormat.parse ( request.findPath("birthday").textValue() );
        } catch (Exception e){
            System.out.println(e);
        }

//        date = dateFormat.parse(request.findPath("birthday").toString());
//
//        System.out.println(date.toString());

        models.Task task = new Task();
            task.setBirthday(date);
            task.setName(request.findPath("name").textValue());
            task.setGift(request.findPath("gift").textValue());
            task.setGiftStatus(request.findPath("giftStatus").textValue());

        try {
            task.save();
        } catch (Exception e){
            System.out.println(e);
        }

        JsonNode newTask = Json.toJson(task);

        System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQ" + newTask);


        System.out.println(newTask);

        ObjectNode result = Json.newObject();

        result.put("newTask", newTask);
        return ok(result);
    }

    public static Result delTask2(Long id) {
        Task.delete(id);
        return redirect(routes.Application.tasks());
    }


    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        controllers.routes.javascript.Application.tasks2(),
                        controllers.routes.javascript.Application.createTask2(),
                        controllers.routes.javascript.Application.delTask2(),
                        controllers.routes.javascript.Application.updTask2()

                )
        );
    }

}