package ed;

import javax.swing.JOptionPane;

public class TablasResuelve extends Thread {
    public String formu;
    public String atomicas;
    
    public void run() {
        String aux;
        
        //DatosDeTablaV muestra = new DatosDeTablaV();
        TablasVG formula = new TablasVG();

        formula.FormulaS = formu; 

        aux = "("+formula.FormulaS+")";

        boolean v = formula.verifica();            
        if((v==true)&&(formula.buscaBicon()==false)) {
            formula.tabla();        
            atomicas = formula.atomicas;
            formula.Resultados = formula.Resultados.replace(" ","");
            
            String reng = "#\tResultados\n";
            for(int r = 0 ; r<formula.renglones; r++) {
                String a =(Integer.toBinaryString(formula.renglones-(r+1)));
                a=a.replace("0","F");
                a=a.replace("1", "T");
                if((formula.renglones-(r+1)==0)||(formula.renglones-(r+1)==1))
                    for(int i=0;i<(formula.N_atomicas-1);i++)
                        a="F"+a;
                if((formula.renglones-(r+1)==2)||(formula.renglones-(r+1)==3))
                    for(int i=0;i<(formula.N_atomicas-2);i++)
                        a="F"+a;
                if((formula.renglones-(r+1)==4)||(formula.renglones-(r+1)==5)||(formula.renglones-(r+1)==6)||(formula.renglones-(r+1)==7))
                    for(int i=0;i<(formula.N_atomicas-3);i++)
                        a="F"+a;
                if((formula.renglones-(r+1)==8)||(formula.renglones-(r+1)==9)||(formula.renglones-(r+1)==10)||(formula.renglones-(r+1)==11)||(formula.renglones-(r+1)==12)||(formula.renglones-(r+1)==13)||(formula.renglones-(r+1)==14)||(formula.renglones-(r+1)==15))
                    for(int i=0;i<(formula.N_atomicas-4);i++)
                        a="F"+a;
                reng = reng + (formula.renglones-(r+1))+"\t"+ a +"\t    "+formula.Resultados.charAt(r)+"\n";
                }
            boolean Ta = formula.verificaTautologia();
            boolean Con = formula.verificacontradiccion();
            
            String tipoo = "";
            if((Ta==true)&&(Con==false))
                tipoo = "Tautología";
            else if((Ta==false)&&(Con==true))
                tipoo = "Contradicción";
            else
                tipoo = "Contingencia";
            
            setDatos(formula.FormulaS,formula.atomicas,reng,tipoo);
            //muestra.setVisible(true);
                                    
        }
        else if((v==false)&&(formula.buscaBicon()==false)) {  
            //TestSwing.setVisible(false);
            //TestSwing.setEnabled(false);        
        }
        
        if((v==true)&&(formula.buscaBicon()==true)) {    
            TablasVG formula2 = new TablasVG();
            
            //String For = aux;
            String For = formula.FormulaS;
            formula.FormulaS = formula.FormulaS.replace("<=>",")=(");
            int bic = 0;
            
            for(int i = 0; i<formula.FormulaS.length(); i++)
                if(formula.FormulaS.charAt(i)=='=')
                    bic = i;
            
            formula2.FormulaS = formula.FormulaS.substring(0,bic);
            formula.FormulaS = formula.FormulaS.substring(bic+1,formula.longitud);
            String auxx = formula.FormulaS;
            formula.FormulaS = formula2.FormulaS;
            formula2.FormulaS = auxx;
            formula.FormulaS = formula.FormulaS.replace(">","->");
            formula2.FormulaS = formula2.FormulaS.replace(">","->");
            boolean a = formula.verifica();
            a = formula2.verifica();
            String total = "";
            
            formula.tabla();
            formula2.tabla();
                    
            formula.Resultados = formula.Resultados.replace(" ","");
            formula2.Resultados = formula2.Resultados.replace(" ","");
            boolean cambio = false;
            if(formula.FormulaS.length()<formula2.FormulaS.length()) {
                auxx = formula.FormulaS;
                formula.FormulaS = formula2.FormulaS;
                formula2.FormulaS = auxx;
                cambio = true;
            }
            
            int div = formula.Resultados.length()/formula2.Resultados.length();
            
            for(int i = 0 ; i < div; i++)
                formula2.Resultados = formula2.Resultados + formula2.Resultados;
                        
            for(int i = 0 ;i<formula.Resultados.length() ; i++) {
                if(formula.Resultados.charAt(i)==formula2.Resultados.charAt(i))
                    total = total + "T";
                else
                    total = total + "F";
            }
            
            if(cambio==true) {
                auxx = formula.Resultados;
                formula.Resultados = formula2.Resultados;
                formula2.Resultados = auxx;                
            }
            
            formula.Resultados = formula.Resultados.replace(" ","");
            formula2.Resultados = formula2.Resultados.replace(" ","");
            
            total = total.replace(" ","");
            int tot = total.length();
            String Tot = "";
            
            for(int i= 0; i<tot ; i++)
                Tot = Tot + " "+(i)+ "\t"+formula.Resultados.charAt(i)+"\t"+total.charAt(i)+"\t"+formula2.Resultados.charAt(i)+" \n";            
            
            boolean Ta = formula.verificaTautologia();
            boolean Con = formula.verificacontradiccion();
            
            String tipoo = "";
            Ta = checaTautologia(total);
            Con = checaContradiccion(total);            
            
            if((Ta==true)&&(Con==false))
                tipoo = "Tautología";
            else if((Ta==false)&&(Con==true))
                tipoo = "Contradicción";
            else
                tipoo = "Contingencia";                        
            
            setDatosC(For,formula.atomicas,formula.FormulaS,formula2.FormulaS,Tot,tipoo);                
            //muestra.setVisible(true);
        }                     
    }
   
    
    
    public boolean checaTautologia(String ress) {
        int a = 0;
        a = ress.length();
        String rr = "";
        
        for(int i = 0 ; i < a ; i++)
            if(ress.charAt(i)!=' ')
                rr = rr + ress.charAt(i);
        
        ress = ress.replace(" ","");

        for(int i = 0 ;i < ress.length();i++)
            if(ress.charAt(i)!='T')
                return false;
        return true;
    }
   
    public boolean checaContradiccion(String ress) {
        int a = 0;
        a = ress.length();
        String rr = "";
        
        for(int i = 0 ; i < a ; i++)
            if(ress.charAt(i)!=' ')
                rr = rr + ress.charAt(i);
        
        ress = ress.replace(" ","");

        for(int i = 0 ;i < ress.length();i++)
            if(ress.charAt(i)!='F')
                return false;
        return true;
    }
    
    public void setDatos(String Formula, String Atomicas, String Resultados, String Tipo) {    
        //this.pnlRes.setVisible(true);
        Formula = Formula.replace(""," ");
        Formula = Formula.replace(">","->");
        Formula = Formula.replace("=","<=>");
        
        Atomicas = Atomicas.replace("",",  ");
        Atomicas = Atomicas.substring(3,Atomicas.length()-3);
        Atomicas = Atomicas + ".";
        
        
        guiNueva.tablaRes = Resultados;
        guiNueva.tablaAtom = Atomicas;
        guiNueva.tablaTipo = Tipo;
        
        //txtAtomicas.setText(Atomicas);
        //txtFormula.setText(Formula);        
        
        //txtAResultados.setText(Resultados);
        //lblTipo2.setText(Tipo);
        
    }
    
      
    public void setDatosC(String FormulaG, String Atomicas,String Formula1, String Formula2, String Total, String tipo) {
        //this.txtFormula.setText(FormulaG);
        //this.txtAtomicas.setText(Atomicas);
        //this.lblFormula1.setText(Formula1);
        //this.lblFormula2.setText(Formula2);
        //this.txtACompara.setText(Total);
        //this.pnlCompara.setVisible(true);
        //this.lblTipo2.setText(tipo);
    }
   
}
