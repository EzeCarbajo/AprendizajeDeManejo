public class MutacionTotalRandom implements FormaMutacion{
      public MutacionTotalRandom(){
       
      }
      public void mutar(ArrayList<Individuo> inds){
           for(int i=0; i<inds.size(); i++){
             inds.get(i).setRed(inds.get(i).getRed().clone()); //al clonarla, solo clona la estructura pero los parametros son generados todos aleatorios
           }
      }
   }