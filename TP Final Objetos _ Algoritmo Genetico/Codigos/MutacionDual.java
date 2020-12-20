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