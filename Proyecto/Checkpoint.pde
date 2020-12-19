public class Checkpoint implements Dibujable{
   PVector pos = new PVector(0,0);
   
   public Checkpoint(PVector posCheckpoint){
       this.pos = posCheckpoint;
   }
   
   public void setPos(PVector pos){
     this.pos= pos;
   }
   
   public PVector getPos(){
      return this.pos; 
   }
   
   public void draw(PVector desplazamiento){
      fill(0,255,0);
      ellipse(pos.x+desplazamiento.x, pos.y+desplazamiento.y, 5,5);
   }
}
