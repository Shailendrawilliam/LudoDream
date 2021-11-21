package com.example.indianludobattle.MyUtil;

import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
    Timer timer;
    String clock;
    public Reminder(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds*1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Time's up!");
            clock="click cycle";
            timer.cancel(); //Terminate the timer thread
        }
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }
}
