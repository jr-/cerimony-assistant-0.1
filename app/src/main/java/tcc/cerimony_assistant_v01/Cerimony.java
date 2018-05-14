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
    private String finalTime;
    private String local;
    private boolean confirmRequirements = false;
    private boolean isAborted = false;
    private List<Participant> participants = new ArrayList<Participant>();
    private List<Step> steps = new ArrayList<Step>();
    private List<String> requirements = new ArrayList<String>();
    private String folderName;
    private AbortedCeremony abortedCeremony;
    private int currentStepNumber = 0;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCurrentStepNumber() {
        return currentStepNumber;
    }

    public void setCurrentStepNumber(int currentStepNumber) {
        this.currentStepNumber = currentStepNumber;
    }

    public Cerimony() {}

    public AbortedCeremony getAbortedCeremony() {
        return abortedCeremony;
    }

    public void setAbortedCeremony(AbortedCeremony abortedCeremony) {
        this.abortedCeremony = abortedCeremony;
    }

    public boolean isAborted() {return isAborted;}

    public void setAborted(boolean isAborted) {this.isAborted = isAborted;}

    public boolean isConfirmRequirements() {return confirmRequirements;}

    public void setConfirmRequirements(boolean confirmRequirements) {this.confirmRequirements = confirmRequirements;}

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

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

    public List<String> getRequirements() { return requirements; }

    public void setRequirements(List<String> requirements) { this.requirements = requirements; }

    public String toXML() {

        String cerimony = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
                  + "<cerimony>\r\n"
                  + "\t<name>" + name + "</name>\r\n"
                  + "\t<short_name>" + shortName + "</short_name>\r\n"
                  + "\t<local>" + local + "</local>\r\n"
                  + "\t<requirements>\r\n";

        for(int i = 0; i < requirements.size(); i++) {
            cerimony += "\t\t<item>" + requirements.get(i) + "</item>\r\n";
        }


        cerimony += "\t</requirements>\r\n"
                  + "\t<steps>\r\n";
        for(int i = 0; i < steps.size(); i++) {
            cerimony += "\t\t<step " + "name=\"" + steps.get(i).getSName() + "\">\r\n"
                      + "\t\t\t<description>" + steps.get(i).getDescription() + "</description>\r\n"
                      + "\t\t\t<input>" + steps.get(i).getInput() + "</input>\r\n"
                      + "\t\t\t<output>" + steps.get(i).getOutput() + "</output>\r\n"
                      + "\t\t\t<observation>" + steps.get(i).getObservation() + "</observation>\r\n"
                      + "\t\t</step>\r\n";
        }
        cerimony += "\t</steps>\r\n"
                  + "</cerimony>\r\n";
        return cerimony;
    }

    public String toTXT() {

        String ceremony = name + "\r\n"
                + "Local: " + local + "\r\n"
                + "Resultado: ";
        if(isAborted) {
            ceremony += "A cerimônia foi abortada pelo motivo " + abortedCeremony.getReason() + " no passo " + abortedCeremony.getStep_number()
                      + " às " + finalTime + " do dia " + finalDate + "." + "\r\n";
        } else {
            ceremony += "A cerimônia terminou com sucesso" + "\r\n";
        }

        ceremony += "Data de início: " + initialDate + "\r\n"
                  + "Horário de início: " + initialTime + "\r\n"
                  + "Data de término: " + finalDate + "\r\n"
                  + "Horário de término: " + finalTime + "\r\n"
                  + "Requisitos confirmados:";

        if(requirements.size() > 0) {
            if(requirements.size() > 2) {
                for(int i = 0; i < requirements.size()-2; i++) {
                    ceremony += " " + requirements.get(i) + ",";
                }
            }
            if(requirements.size() != 1) {
                ceremony += " " + requirements.get(requirements.size()-2) + " e";
            }
            ceremony += " " + requirements.get(requirements.size()-1) + ".";
        }
        ceremony += "\r\n";
        if(participants.size() > 0) {
            ceremony += "Participantes confirmados:\r\n";
            for(int i = 0; i < participants.size(); i++) {
                Participant cp = participants.get(i);
                ceremony += cp.getPName() + " - " + cp.getCargo() + " - " + cp.getUnidade() + " - " + cp.getEmail() + "\r\n";
            }
        }
        ceremony += "-----------------------------------------------------------------------------------------------\r\n";
        if(steps.size() > 0) {
            int loop_size = steps.size();
            if(isAborted) {
                loop_size = abortedCeremony.getStep_number() + 1;
            }
            for(int i = 0; i < loop_size; i++) {
                Step cs = steps.get(i);
                ceremony += "Passo " + i + ": " + cs.getSName() + " - " + cs.getTime() + "\r\n"
                        + "Descrição: " + cs.getDescription() + "\r\n"
                        + "Entrada: " + cs.getInput() + "\r\n"
                        + "Saída: " + cs.getOutput() + "\r\n"
                        + "Observações: " + cs.getObservation() + "\r\n"
                        + "Anotações: " + cs.getNotes() + "\r\n"
                        + "Evidências: " + cs.getEvidence() + "\r\n";
                ceremony += "-----------------------------------------------------------------------------------------------\r\n";
            }

        }
        return ceremony;
    }
}
