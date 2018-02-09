package tcc.cerimony_assistant_v01;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zr on 03/06/17.
 */

public class CCerimonies {
    private static CCerimonies cc = null;
    private Map<String, Cerimony> lc;
    private String selectedCerimonyName;

    private CCerimonies() {}

    public void setCerimonies(List<String> cerimoniesFileName){
        lc = new HashMap<>();
        for(int i = 0; i < cerimoniesFileName.size(); i++){
            Cerimony c = new Cerimony();
            lc.put(cerimoniesFileName.get(i), c);
        }
    }

    public void setSelectedCerimony(String cerimonyName) {
        selectedCerimonyName = cerimonyName;
    }

    public Cerimony getSelectedCerimony() {
        return lc.get(selectedCerimonyName);
    }

    public static CCerimonies getInstance(){
        if(cc == null)
            cc = new CCerimonies();

        return cc;
    };

    public Map<String, Cerimony> getCerimonies() {
        return lc;
    }
}
