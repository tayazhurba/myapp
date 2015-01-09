package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat extends Model {
    public void dateFormat() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        System.out.println(format1.format("PlanDate"));
    }
}