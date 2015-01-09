package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.Task;
import static play.data.Form.form;

public class Application extends Controller {

    static Form<Task> taskForm = form(Task.class);

    public static Result index() {
        return redirect(routes.Application.tasks());

    }

    public static Result tasks() {
        return ok(
                views.html.index.render(Task.all(), taskForm)
        );
    }

    public static Result newTask() {
        System.out.println("newTask");
        Form<Task> filledForm = taskForm.bindFromRequest();
        if(filledForm.hasErrors()) {
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
        //    Form<Task> filledForm = taskForm.fill(t);
        // отрисовать шаблорн edit
        return ok(
                views.html.edit.render(taskForm.fill(t)));

    }

    public static Result updateTask() {
        Form<Task> filledForm = taskForm.bindFromRequest();
        System.out.println(filledForm.get().PlanName);
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