package tcc.cerimony_assistant_v01;

/**
 * Created by zr on 31/01/17.
 */

public class Participant {
    private String id;
    private boolean isEditor;
    private boolean isCreator;
    private String name;
    private String cargo;
    private String unidade;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean isEditor) {
        this.isEditor = isEditor;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean isCreator) {
        this.isCreator = isCreator;
    }

    public String getPName() {
        return name;
    }

    public void setPName(String name) {
        this.name = name;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
