package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat  {
    public void dateFormat() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        //PlanDate = format1.format(System.currentTimeMillis());
        //System.out.println(format1.format("PlanDate"));
        System.out.println(format1.format(System.currentTimeMillis()));
    }
}