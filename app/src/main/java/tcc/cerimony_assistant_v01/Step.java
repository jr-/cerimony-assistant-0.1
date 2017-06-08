package tcc.cerimony_assistant_v01;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zr on 31/01/17.
 */

public class Step {
    private String id;
    private String name;
    private String description;
    private String inputType;
    private String outputType;
    private String observation;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private List<Input> inputs = new ArrayList<Input>();
    private List<String> output = new ArrayList<String>(); //TODO necessario add os atributos dos outputs e inputs


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSName() {
        return name;
    }

    public void setSName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }
}
