package ru.dmitryvinogradov.StateMachine;

public class CurrentState {
    private States nowState;

    public CurrentState() {
        this.nowState = States.DEFAULT;
    }

    public States getNowState() {
        return nowState;
    }

    public void setDefaultState() {
        this.nowState = States.DEFAULT;
    }

    public void setAddingTaskState(){
        this.nowState = States.ADDINGTASK;
    }


}
