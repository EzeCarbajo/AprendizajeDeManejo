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