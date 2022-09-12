package info.timestat.statemachine;

import org.springframework.stereotype.Component;

@Component
public class CurrentState {
    private State state;

    public CurrentState() {
        this.state = State.DEFAULT;
    }

    public void setState(State nowState) {
        this.state = nowState;
    }

    public State getState(){
        return state;
    }
}
