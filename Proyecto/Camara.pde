public class Camara{
   private Auto enfoque;
   PVector desplazamiento = new PVector(0,0);
   
   public Camara(Auto d){
     this.enfoque = d;
   }
   
   public void setEnfoque(Auto d){
      this.enfoque = d; 
   }
   
   public PVector getDespl(){
    PVector res = new PVector((width/2) - enfoque.getPos().x, (height/2) - enfoque.getPos().y);
    return res;
   }
   
   public void draw(ArrayList<Dibujable> visibles){
     desplazamiento = getDespl();
       for(int i=0; i<visibles.size(); i++)
         visibles.get(i).draw(desplazamiento); // aca voy a tener la ruta, y los autos que no enfoco
       enfoque.draw(desplazamiento);
   }
}
