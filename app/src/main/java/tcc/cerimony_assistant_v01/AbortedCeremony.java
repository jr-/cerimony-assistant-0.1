package tcc.cerimony_assistant_v01;

/**
 * Created by zr on 27/03/18.
 */

public class AbortedCeremony {
    private String reason = "";
    private int step_number = -1;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStep_number() {
        return step_number;
    }

    public void setStep_number(int step_number) {
        this.step_number = step_number;
    }

}
