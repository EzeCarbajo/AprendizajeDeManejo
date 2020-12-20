public interface Dibujable{
    public abstract void draw(PVector desplazamiento);
}
/************************************************************************************/
public interface Colisionable extends Dibujable{
  public abstract boolean colision(ArrayList<PVector> puntos);
  public abstract void draw(PVector desplazamiento);
  public ArrayList<PVector> calcularIntersecciones(PVector p1, PVector p2);
}
/************************************************************************************/
public class Calle implements Colisionable{
  private PVector a1, a2, b1, b2;
  private int margen = 5;
  public Calle(PVector a1,PVector a2,PVector b1,PVector b2){
    this.a1 = a1;
    this.a2 = a2;
    this.b1 = b1;
    this.b2 = b2;
  }
  
  public PVector [] getExtremos(){
    PVector [] salida = new PVector[4];
    salida[0] = new PVector(a1.x,a1.y);
    salida[1] = new PVector(a2.x,a2.y);
    salida[2] = new PVector(b1.x,b1.y);
    salida[3] = new PVector(b2.x,b2.y);
    return salida;
  }
  
  public boolean colision(ArrayList<PVector> puntos){
     for(PVector punto  : puntos){
         if(Geom.distanciaSegmento(punto,a1,a2) < margen || Geom.distanciaSegmento(punto, b1,b2) < margen)
           return true;
     }
     return false;
  }
  public void draw(PVector desplazamiento){
     // dibujamos los dos segmentos de la calle
     strokeWeight(3);
     line(a1.x + desplazamiento.x, a1.y + desplazamiento.y, a2.x+ desplazamiento.x, a2.y + desplazamiento.y);
     line(b1.x + desplazamiento.x, b1.y + desplazamiento.y, b2.x+ desplazamiento.x, b2.y + desplazamiento.y);
     strokeWeight(1);
  }
  public ArrayList<PVector> calcularIntersecciones(PVector p1, PVector p2){
    PVector punto1 = Geom.puntoInterseccion(p1,p2,a1,a2);
    PVector punto2 = Geom.puntoInterseccion(p1,p2,b1,b2); // calculo los puntos de interseccion con ambos segmentos de la calle
    ArrayList<PVector> salida = new ArrayList<PVector>();
    if(punto1 != null)
      salida.add(punto1);
    if(punto2 != null) 
      salida.add(punto2);
    return salida;
  }
}

public class Ruta implements Colisionable{
  private ArrayList<Colisionable> objetos = new ArrayList<Colisionable>(); 
  private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint> ();
  
  public Ruta(ArrayList<Colisionable> objetos){
    this.objetos = objetos;  
  }
  
  public Ruta(){  
  }
  
 
  
  public boolean colision(ArrayList<PVector> puntos){
      for(Colisionable objeto : objetos)
        if(objeto.colision(puntos))
          return true;
      return false;
  }
  
  public void addElemento(Colisionable e){
       objetos.add(e);
  }
  
  public void draw(PVector desplazamiento){
      for(int i=0; i<objetos.size(); i++)
        objetos.get(i).draw(desplazamiento);
      for(int i=0; i<checkpoints.size(); i++)
        checkpoints.get(i).draw(desplazamiento);
      
  }
  
  public ArrayList<PVector> calcularIntersecciones(PVector p1, PVector p2){ // calcula intersecciones  del segmento, definido por p1 y p2, con el resto de las calles
      ArrayList<PVector> salida = new ArrayList<PVector>();
      for(int i=0; i<objetos.size(); i++)
        salida.addAll(objetos.get(i).calcularIntersecciones(p1,p2));
      return salida;
  }
  
  public void addCalle(Calle c){
     PVector [] puntos_checkpoint = c.getExtremos();
     PVector pos_checkpoint = new PVector(0,0);
     pos_checkpoint.x = (puntos_checkpoint[0].x + puntos_checkpoint[1].x + puntos_checkpoint[2].x + puntos_checkpoint[3].x)/4;
     pos_checkpoint.y = (puntos_checkpoint[0].y + puntos_checkpoint[1].y + puntos_checkpoint[2].y + puntos_checkpoint[3].y)/4; // calculo el centro de la calle
     Checkpoint checkpoint_c = new Checkpoint(pos_checkpoint);
     checkpoints.add(checkpoint_c);
     objetos.add(c);
  }
  
  public void cargarCalles(String rutaArchivo){ 
     PVector[] aux = new PVector[4];
     for(int i=0; i<4; i++)
       aux[i] = new PVector(10,10);
     
     String [] lineas = loadStrings(rutaArchivo);
     for(int i=0; i<lineas.length; i++){
        String [] pvectores= split(lineas[i], ";"); 
        for(int j=0; j<pvectores.length; j++){
           String [] punto = split(pvectores[j], ",");
           aux[j].x = int(punto[0]);
           aux[j].y = int(punto[1]);
        }
        Calle c = new Calle(new PVector(aux[0].x, aux[0].y), new PVector(aux[1].x, aux[1].y),new PVector(aux[2].x, aux[2].y),new PVector(aux[3].x, aux[3].y));
        addCalle(c);
     }  
     
  }
  
  public Checkpoint getCP(int i){
    // esto es para que tome la lista de checkpoints como una lista circular
    if(i < 0)
      i = Math.floorMod(i, checkpoints.size());
    else
      i = i % checkpoints.size();
    return checkpoints.get(i);
  }
}
