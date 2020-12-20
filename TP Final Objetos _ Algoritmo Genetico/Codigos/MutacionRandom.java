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