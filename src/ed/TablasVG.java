package ed;

import javax.swing.JOptionPane;

public class TablasVG 
{
    public String FormulaS;
    private String formaux;
    
    public int longitud=0;
    private int longaux =0;
    
    public int N_atomicas = 0;
    public int renglones = 0;        
    
    public String atomicas = "";
    public String Resultados ="";
    
    public String FNPD ="";
    public String FNPC ="";
    public boolean bicond = false;   
    public int bicon = 0 ;
    
    private char[][] Mat_p = new char[32770][15];          
  
        
    public boolean buscaBicon()    {
        for(int i=0 ; i< longitud; i++)
            if(FormulaS.charAt(i)=='=')
            {
                FormulaS = FormulaS.replace("<=>",")=(");                
                return true;
            }
        return false;
    }    
    public void resetF()    {
        FormulaS = "";
        formaux = "";    
        longitud=0;
        longaux =0;
        N_atomicas = 0;
        renglones = 0;            
        atomicas = "";    
        Resultados ="";                            
    }        
    public void buscaBicon2()    {
        for(int i = 0 ; i<longitud ; i++)
            if(FormulaS.charAt(i)=='=')
                bicon = i;
    }    
    public boolean verifica()    {        
        FormulaS = FormulaS.replace(" ","");                
        longitud=FormulaS.length();     
                     
        if(this.longitud==0)
        {
            JOptionPane.showMessageDialog(null,"Debes ingresar una fórmula proposicional");
            return false;
	}
        
        FormulaS = '(' + FormulaS + ')';
        longitud=FormulaS.length();     

       
        if (simbolos()==false)
        {
            JOptionPane.showMessageDialog(null,"Símbolos erroneos");            
            return false;
        }
        
        if(sintaxis()==false)
        {
            JOptionPane.showMessageDialog(null,"Sintaxis erronea");           
            return false;
        }                    
        
        if(parentesisCorrec()==false)
        {
            JOptionPane.showMessageDialog(null,"Paréntesis incorrectos");
            return false;  
        }
        
        FormulaS = FormulaS.replace("<->","-");    
        FormulaS = FormulaS.replace("->",">");                                       
        
        longitud=FormulaS.length();         
       return true;
    }   
    private boolean simbolos()    {
        int i;	
	char form;

	for(i=0;i<longitud;i++)
	{
            form = FormulaS.charAt(i);
            if((form=='7')||(form=='=')||(form=='<')||(form == '-')||(form=='>')||(form=='v')||(form=='^')||(form=='(')||(form==')'))
                continue;
            if((form>='A')&&(form<='Z'))
                continue;		
            return false;						
	}			
	return true;
    }        
    private boolean sintaxis()    {       
        int i;
        int longitud=this.longitud;
	char Ant, Act, Sig;
        
        for(i=1;i<longitud-1;i++)
	{	            
            Ant = FormulaS.charAt(i-1);
            Act = FormulaS.charAt(i);
            Sig = FormulaS.charAt(i+1);
            //JOptionPane.showMessageDialog(null,"ANT: " + Ant +"\nACT: "+Act+"\nSIG: "+Sig);
            if( (Act == Sig) && ( (Act != '(') && (Act != ')') && (Act != '7') ) )    
                return false;
            
            if((Act == '(')&&((Sig == 'v')||(Sig == '^')||(Sig == '-')||(Sig == '>')||(Sig == '=')))            
                return false;
            
            if(((Act == '(')&&(Sig == ')'))||((Act == ')')&&(Sig == '(')))
                return false;            
			
            if((Act == ')')&&  (  (Sig == '7') ||  ((Sig >= 'A')&&(Sig <= 'Z')))    )
                return false;
            
		    
            if( ((Act >= 'A')&&(Act <= 'Z')) && ( (Sig == '7')||(Sig == '>')||(Sig == '=')||(Sig == '(')))            
                return false;            
		    
            if(    ((Act >= 'A')&&(Act <= 'Z'))   &&((Ant == '=')||(Ant == ')'))   )                      
                return false;
            
	}	  
        return true;
    }           
    private boolean parentesisCorrec()    {
        int i=0;                              //REGRESA 1 SI ES LA MIMA CANTIDAD, 0 LO CONTRARIO     
        int abre=0, cierra=0;    
        char formulaI; 
        
        for(i=0;i<longitud;i++)                   
        {
            formulaI = FormulaS.charAt(i);
            if(formulaI=='(')
                abre+=1;
            if(formulaI==')')
                cierra+=1;
        }
        if(abre!=cierra)
            return false;        
        return true;     
    }        
    public void tabla()    {
        N_atomicas = numAtomicas();  //ai mismo ordena las atomicas en orden alfabetico        
        renglones=(int)(Math.pow(2,N_atomicas));
        llenarMatP();	                  
        
        for(int i =0;i<renglones;i++)
        {            
            boolean res = evaluarRenglon(i);              
            
            if(res==false)
                Resultados = Resultados +"F  ";
            else
                Resultados = Resultados +"T  ";
        }	        
    }    
    private void llenarMatP()    {		
        int i, j, Dif;
        int renglones=this.renglones;        
        
        for(i=0;i<renglones;i++)
            for(j=0;j<this.N_atomicas;j++)
                this.Mat_p[i][j]='F';

        for(j=0;j<=this.N_atomicas;j++)
        {
            Dif=(int)(Math.pow(2,this.N_atomicas-j-1));
            int aux1=0;
            for(i=0;i<this.renglones;i++)
            {
                aux1+=1;
                if(aux1<=Dif)
                this.Mat_p[i][j]='T';
                if(aux1==(2*Dif))
                aux1=0;
            }
        }
    }    
    private int numAtomicas()    {
        int i,j;
        char auxx;
        int cont=0;  
        int N = longitud;
        
        for( j = 65 ; j <= 90 ; j++ )
            for( i = 0 ; i < N ; i++ )
            { 
                auxx = FormulaS.charAt(i);
                if( (auxx == 70) || (auxx==84) )
                    continue;     
                if(auxx == j)
                {
                    atomicas = atomicas + auxx;
                    cont=cont+1;
                    i=N;              
                }       
            }              
        return cont;		
    }        
    private boolean evaluarRenglon(int renglon)    {
        int j, i;    
        formaux = FormulaS;
        longaux = formaux.length();  
        
        cambiarValores(renglon);
                
        for( i = longaux-1 ; i >= 0 ; i-- )			        
            if(formaux.charAt(i) == '(')			
            {
                for(j=i+1;j<longaux;j++)
                    if(formaux.charAt(j) == ')')
                    {
                        operacionesRenglon(renglon,i,j);  
                        j=longaux+1;
                        i=longaux-2;                          
                    }
            }        
        if(formaux.charAt(0)=='T')
            return true;
        else 
            return false;
    }            
    private void cambiarValores(int renglon)    {
        int i,j;
        
        for(i=0;i<N_atomicas;i++)
            for(j=0;j<longaux;j++)            
                if(atomicas.charAt(i)==formaux.charAt(j))
                {
                    char old1 = formaux.charAt(j);
                    char new1 =  Mat_p[renglon][i];                    
                    formaux = formaux.replace(old1,new1);                      
                }            
    }        
    private void operacionesRenglon(int renglon,int inicio, int cierra)    {           
            if(inicio+2 == cierra)
            {                   
                formaux = formaux.replace("(T)","T");  
                formaux = formaux.replace("(F)","F");
                formaux = formaux.replace(" ","");
                longaux = formaux.length();                
                return;
            }	
                        
            for(int i = inicio ; i < cierra ; i++)                          
                if(formaux.charAt(i)=='7')  //Negado
                {                    
                    negacion(renglon,i);  
                    return;
                }

            for(int i = inicio ; i < cierra ; i++)    
                if(formaux.charAt(i)=='>')
                {              
                    condicional(renglon,i);
                    return;
                }                
                
             for(int i = inicio ; i < cierra ; i++)   
                if(formaux.charAt(i)=='^')
                {
                    conjuncion(renglon,i);
                    return;
                }
                
              for(int i = inicio ; i < cierra ; i++)  
                if(formaux.charAt(i)=='v')                    
                {  
                    disyuncion(renglon,i);
                    return;
                }                
              
              for(int i = inicio ; i < cierra ; i++)  
                if(formaux.charAt(i)=='-')                    
                {  
                    bicondicional(renglon,i);
                    return;
                }
            
    }        
    private void negacion(int renglon, int neg)    {                           
        formaux = formaux.replace("7T","F");
        formaux = formaux.replace("7F","T");        
        formaux = formaux.replace(" ","");            
        longaux = formaux.length();                 
    }        
    private void bicondicional(int renglon, int bic)    {
        String Ini = formaux.substring(0,bic-1);
        String Med = formaux.substring(bic-1,bic+2);
        String Fin = formaux.substring(bic+2,longaux);        
        
        if(Med.charAt(0)==Med.charAt(2))
            Med = "T";
        else
            Med = "F";
        
        formaux = Ini + Med + Fin;                
        formaux = formaux.replace(" ","");
        longaux = formaux.length();                                                
    }        
    private void condicional(int renglon, int cond)    {              
        String Ini = formaux.substring(0,cond-1);
        String Med = formaux.substring(cond-1,cond+2);
        String Fin = formaux.substring(cond+2,longaux);        
        
        if((Med.charAt(0)=='T')&&(Med.charAt(2)=='F'))
            Med = "F" ;
        else
            Med = "T";
        
        formaux = Ini + Med + Fin;        
        formaux = formaux.replace(" ","");
        longaux = formaux.length();                                        
    }        
    private void conjuncion(int renglon, int conj)    {                
        String Ini = formaux.substring(0,conj-1);
        String Med = formaux.substring(conj-1,conj+2);
        String Fin = formaux.substring(conj+2,longaux);                
        
        if((Med.charAt(0)=='T')&&(Med.charAt(2)=='T'))
            Med = "T" ;
        else
            Med = "F";
        
        formaux = Ini + Med + Fin;         
        formaux = formaux.replace(" ","");
        longaux = formaux.length();             
    }    
    private void disyuncion(int renglon, int dis)    {                   
        String Ini = formaux.substring(0,dis-1);
        String Med = formaux.substring(dis-1,dis+2);
        String Fin = formaux.substring(dis+2,longaux);
                
        if((Med.charAt(0)=='T')||(Med.charAt(2)=='T'))
            Med = "T" ;
        else
            Med = "F";        
               
        formaux = Ini + Med + Fin;           
        formaux = formaux.replace(" ","");
        longaux = formaux.length();            
    }                
    public void FNP()    {
        int FC =0 , FD = 0;
        int n;
        String result = Resultados.replaceAll(" ","");
        int i;
        
        for(i = 0 ; i<renglones; i++)        
            if(result.charAt(i)=='T')
                FD+=1;
        FC = renglones - FD;
        
        for(i = 0; i<renglones ; i++)
        {
            n=renglones-i-1;
            if(result.charAt(i)=='T')            
            {
                obtFNPD(i);
                FD -=1;
                if(FD>0)
                    FNPD = FNPD + "[" + n + "]"+"v";
                else
                    FNPD = FNPD + "[" + n + "]"; 
            }            
            else
            {
                obtFNPC(i);                        
                FC -=1;
                if(FC>0)
                    FNPC = FNPC + "[" + n + "]"+ "^";
                else
                    FNPC = FNPC + "[" + n + "]"; 
            }    
        }                
    }    
    private void obtFNPD(int ren)    {
        String valores ="";
        int i;
        
        for(i=0; i< N_atomicas;i++)
            valores = valores + Mat_p[ren][i];
        
        String FNN ="(";        
        
        for(i=0 ; i<N_atomicas ; i++)
        {
            if(valores.charAt(i)=='T')
                FNN = FNN + atomicas.charAt(i);
            else
                FNN = FNN + "7"+atomicas.charAt(i);
            
            FNN = FNN + "^";
        }
        FNN = FNN + ")";
        FNPD = FNPD + FNN;
        FNPD = FNPD.replace("^)",")");        
    }    
    private void obtFNPC(int ren)    {        
        String valores ="";
        int i;
        
        for(i=0; i< N_atomicas;i++)
            valores = valores + Mat_p[ren][i];
        
        String FNN ="(";  
        
        for(i=0 ; i<N_atomicas ; i++)
        {
            if(valores.charAt(i)=='F')
                FNN = FNN + atomicas.charAt(i);
            else
                FNN = FNN + "7"+atomicas.charAt(i);
            
            FNN = FNN + "v";     
        }
        FNN = FNN + ")";
        FNPC = FNPC + FNN;
        FNPC = FNPC.replace("v)",")");
    }    
    public boolean verificaTautologia()    {
        for(int i = 0 ;i < renglones;i++)
            if(Resultados.charAt(i)!='T')
                return false;
        
        return true;
    }
    public boolean verificacontradiccion()    {
         for(int i = 0 ;i < renglones;i++)
            if(Resultados.charAt(i)!='F')
                return false;
        
        return true;
    }
   
}
