package ed;

import javax.swing.JOptionPane;

public class ObtieneFNP extends Thread {
    public String form;
    
    public void run() {
        
        TablasVG formula = new TablasVG();           
        
        formula.FormulaS = form; 
        
        boolean v = formula.verifica();        

        if(v==true) {        

            formula.tabla();        
            formula.FNP();  

            formula.FNPD = formula.FNPD.replace(" ","");                
            formula.FNPD = formula.FNPD.replace(")v("," ) v ( ");        
            formula.FNPC = formula.FNPC.replace(" ","");
            formula.FNPC = formula.FNPC.replace(")^("," ) ^ ( ");
            
            setDatos(form,formula.atomicas,formula.FNPD,formula.FNPC);            
        } else
            JOptionPane.showMessageDialog(null,"Fórmula incorrecta");         
    }
    
    public void setDatos(String Formula, String Atomicas, String FNPD, String FNPC) {
        guiNueva.fnAtom = Atomicas;
        guiNueva.fnCP = FNPC;
        guiNueva.fnDP = FNPD;       
    }
    
}
