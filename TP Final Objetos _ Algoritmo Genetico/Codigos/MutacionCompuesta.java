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