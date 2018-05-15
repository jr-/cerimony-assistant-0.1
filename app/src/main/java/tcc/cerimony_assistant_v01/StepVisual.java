package tcc.cerimony_assistant_v01;

/**
 * Created by zr on 14/05/18.
 */

public class StepVisual {
    private String name;
    private boolean executed;

    public StepVisual(String name, boolean executed) {
        this.name = name;
        this.executed = executed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
