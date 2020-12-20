public interface FormaMutacion{
   public abstract void mutar(ArrayList<Individuo> inds); 
}

public class MutacionRandom implements FormaMutacion{
  private float mutationRate;
   public MutacionRandom(float mutationRate){
      this.mutationRate = mutationRate;
   }
  
   
   /**
   Modifica parametros del individuo de manera aleatoria, segun mutationRate*/
   private void mutar(Individuo i){
      int tamanoMutacion = Math.round(i.getRed().getCantParametros() * mutationRate);  // funcion piso
      int parametroInicial = int(Math.round(Math.random() * i.getRed().getCantParametros()));
      ArrayList<Double> params = i.getRed().getParametros();
      while(parametroInicial <= tamanoMutacion){
        double variacion = Math.random() * 2 - 1;
        i.getRed().modificarParametro(parametroInicial % i.getRed().getCantParametros() , params.get(parametroInicial % params.size()) + variacion);
        parametroInicial++;
      }
   }
   
   public void mutar(ArrayList<Individuo> inds){
      for(int i=0; i<inds.size(); i++)
        mutar(inds.get(i));
   }
}

public class MutacionDual implements FormaMutacion{
   private Individuo i1,i2;
   private MutacionRandom m;
   public MutacionDual(Individuo i1, Individuo i2, float mutationRate){
      if(i1.getRed().getCantParametros() == i2.getRed().getCantParametros()){
      this.i1 = i1;
      this.i2 = i2;
      m = new MutacionRandom(mutationRate);
     }else{
       System.out.println("Error: Individuos incompatibles...");
     }
   }
   
   public MutacionDual(float mutationRate){
     m = new MutacionRandom(mutationRate);
   }
   
   public void setIndividuos(Individuo i1, Individuo i2){
     if(i1.getRed().getCantParametros() == i2.getRed().getCantParametros()){
      this.i1 = i1;
      this.i2 = i2;
     }else{
       System.out.println("Error: Individuos incompatibles...");
     }
   }
   
   private void mutar(Individuo i){
      int max = i1.getRed().getCantParametros(); //obtengo la cantidad total de parametros
      int tamanoMutacion = (int) Math.round(Math.random() * max/2d); //obtengo una longitud de modificacion que sea a lo sumo la mitad de los parametros 
      int nroParametro = max;
      while(nroParametro + tamanoMutacion > max){
          nroParametro = (int) Math.round(Math.random() * max);  //mientras que el tamaño de la mutacion + el primer parametro sean mayores que el maximo (voy a querer modificar un parametro fuera de rango)
      }                                                          // sigo buscando hasta que quiera modificar todos parametros validos
        
      Double [] parametros1 = new Double[max];
      Double [] parametros2 = new Double[max];
      
      parametros1 = i1.getRed().getParametros().toArray(parametros1);
      parametros2 = i2.getRed().getParametros().toArray(parametros2);
      for(int j=0 ;j<max; j++){
         //modifico todos los parametros del individuo, usando los de i2 e i1
         if( j >= nroParametro && j <=(nroParametro+tamanoMutacion))
            i.getRed().modificarParametro(j,parametros1[j]);
         else
            i.getRed().modificarParametro(j,parametros2[j]);
      }
   }
   
   public  void mutar(ArrayList<Individuo> inds){
      for(int i=0; i<inds.size(); i++){
        mutar(inds.get(i));
      }   
      m.mutar(inds);   
   }
}

public class MutacionCompuesta implements FormaMutacion{
   private FormaMutacion m1,m2;
   private float porcentaje_m1;
   /**Tiene dos mutaciones y un porcentaje de individuos a los que se le aplica m1.
   (1-porcentaje_m1) sera el porcentaje al que se le aplique m2. Primero se aplica m1, luego m2*/
   public MutacionCompuesta(FormaMutacion m1, FormaMutacion m2, float porcentaje_m1){
      this.m1 = m1;
      this.m2 = m2;
      this.porcentaje_m1 = porcentaje_m1;
   }
   
   public MutacionCompuesta(){
   }
   /*Setea dos mutaciones y un porcentaje de individuos a los que se le aplica m1.
   1-porcentaje_m1 sera el porcentaje al que se le aplique m2. Primero se aplica m1, luego m2**/
   public void setMutaciones(FormaMutacion m1, FormaMutacion m2, float porcentaje_m1){
      this.m1 = m1;
      this.m2 = m2;
      this.porcentaje_m1 = porcentaje_m1;
   }
   
   /*Muta los primeros porcentaje_m1 individuos con m1, los demas con m2**/
   public void mutar(ArrayList<Individuo> inds){
      int cantIndM1 = Math.round(inds.size() * this.porcentaje_m1);
      m1.mutar(new ArrayList<Individuo>(inds.subList(0,cantIndM1)));
      m2.mutar(new ArrayList<Individuo>(inds.subList(cantIndM1+1,inds.size())));
   }
}
   
public class MutacionTotalRandom implements FormaMutacion{
  public MutacionTotalRandom(){
       
  }
  public void mutar(ArrayList<Individuo> inds){
    for(int i=0; i<inds.size(); i++){
      inds.get(i).setRed(inds.get(i).getRed().clone()); //al clonarla, solo clona la estructura pero los parametros son generados todos aleatorios
    }
  }
}
