package tcc.cerimony_assistant_v01;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zr on 31/01/17.
 */

public class Cerimony {
    private String name;
    private String shortName;
    private String creationDate;
    private String initialDate;
    private String finalDate;
    private String initialTime;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    private String finalTime;

    public String getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(String initialTime) {
        this.initialTime = initialTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    private String local;
    private List<Participant> participants = new ArrayList<Participant>();
    private List<Step> steps = new ArrayList<Step>();

    public Cerimony() {}

    public String getCName() {
        return name;
    }

    public void setCName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String toXML() {

        String cerimony = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                  + "<cerimony>\n"
                  + "\t<name>" + name + "</name>\n"
                  + "\t<short_name>" + shortName + "</short_name>\n"
                  + "\t<creation_date>" + creationDate + "</creation_date>\n"
                  + "\t<initial_date>" + initialDate + "</initial_date>\n"
                  + "\t<final_date>" + finalDate + "</final_date>\n"
                  + "\t<initial_time>" + initialTime + "</initial_time>\n"
                  + "\t<final_time>" + finalTime + "</final_time>\n"
                  + "\t<local>" + local + "</local>\n";

        for(int i = 0; i < participants.size(); i++) {
            cerimony += "\t<participant>\n"
                      + "\t\t<name>" + participants.get(i).getPName() + "</name>\n"
                      + "\t\t<cargo>" + participants.get(i).getCargo() + "</cargo>\n"
                      + "\t\t<unidade>" + participants.get(i).getUnidade() + "</unidade>\n"
                      + "\t\t<email>" + participants.get(i).getEmail() + "</email>\n"
                      + "\t</participant>\n";
        }
        cerimony += "\t<steps>\n";
        for(int i = 0; i < steps.size(); i++) {
            cerimony += "\t\t<step " + "time=\"" + steps.get(i).getTime() + "\">\n"
                      + "\t\t\t<description>" + steps.get(i).getDescription() + "</description>\n"
                      + "\t\t\t<inputs>\n";
            for(int j = 0; j < steps.get(i).getInputs().size(); j++) {
                cerimony += "\t\t\t\t<input type=\"" + steps.get(i).getInputs().get(j).getType()
                         + "\" resume=\"" + steps.get(i).getInputs().get(j).getResume() + "\">"
                         + steps.get(i).getInputs().get(j).getText() + "</input>\n";
            }
            cerimony += "\t\t\t</inputs>\n"
                      + "\t\t\t<outputs>\n";
            for(int k = 0; k < steps.get(i).getOutput().size(); k++) {
                cerimony += "\t\t\t\t<output>" + steps.get(i).getOutput().get(k) + "</output>\n";
            }
            cerimony += "\t\t\t</outputs>\n"
                      + "\t\t\t<observation>" + steps.get(i).getObservation() + "</observation>\n"
                      + "\t\t</step>\n";
        }
        cerimony += "\t</steps>\n"
                  + "</cerimony>\n";
        return cerimony;
    }
}
