package com.example.dailydone;

public class StoreRoutineData {
    String routineTitle, routineDesc, routineExecuteDate;

    public StoreRoutineData() {

    }

    public StoreRoutineData(String routineTitle, String routineDesc, String routineExecuteDate) {
        this.routineTitle = routineTitle;
        this.routineDesc = routineDesc;
        this.routineExecuteDate = routineExecuteDate;
    }

    public String getRoutineTitle() {
        return routineTitle;
    }

    public void setRoutineTitle(String routineTitle) {
        this.routineTitle = routineTitle;
    }

    public String getRoutineDesc() {
        return routineDesc;
    }

    public void setRoutineDesc(String routineDesc) {
        this.routineDesc = routineDesc;
    }

    public String getRoutineExecuteDate() {
        return routineExecuteDate;
    }

    public void setRoutineExecuteDate(String routineExecuteDate) {
        this.routineExecuteDate = routineExecuteDate;
    }
}
