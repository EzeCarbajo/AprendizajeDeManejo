public class Sensor implements Dibujable{
  private PVector p1 = new PVector(0,0);
  private PVector p2 = new PVector(0,0);
  ArrayList<PVector> puntosI = new ArrayList<PVector> ();
  
  public Sensor(PVector punto, PVector direccion){
      p1 = punto;
      p2 = direccion;
  }
  
  
  public void setPos(PVector punto, PVector punto2){
     this.p1 = punto;
     this.p2 = punto2;
  }
  
  public void draw(PVector desplazamiento){
    stroke(192,192,192);
    line(p1.x+desplazamiento.x,p1.y+desplazamiento.y, p2.x+desplazamiento.x,p2.y+desplazamiento.y);  
    stroke(0);
    for(int i=0; i<puntosI.size(); i++)
      ellipse(puntosI.get(i).x + desplazamiento.x,puntosI.get(i).y + desplazamiento.y , 10,10);
    
  }
  
  public void mover(Auto anclaje, PVector punto, PVector direccion){
      PVector pos = anclaje.getPos(); // guardo la posicion del anclaje
      int angulo = anclaje.getAngulo(); // guardo el angulo del anclaje
      translate(pos.x, pos.y);
      this.p1.x = punto.x;
      this.p1.y = punto.y;
      this.p2.x = punto.x + direccion.x;
      this.p2.y = punto.y + direccion.y;
      this.p1.rotate(radians(angulo-270));
      this.p2.rotate(radians(angulo-270));
      translate(-pos.x, -pos.y);
      this.p1.add(pos);
      this.p2.add(pos);
  }
  
  public double calcularValor(Colisionable r){
      ArrayList<PVector> puntos = r.calcularIntersecciones(p1,p2); //la ruta me da todos los puntos que interseca el segmento que define el sensor
      this.puntosI = puntos;
      PVector segmento = new PVector (0,0) , aux = new PVector(0,0);
      
      // construyo el segmento del sensor, suponiendo que no hay interseccion con la calle
      segmento.x = p2.x - p1.x;
      segmento.y = p2.y - p1.y;
      
      
      if( puntos.size() > 0 ){
        for(int i=0; i<puntos.size(); i++){
            aux.x = puntos.get(i).x - p1.x;
            aux.y = puntos.get(i).y - p1.y;
              if(aux.mag() < segmento.mag()){ //busco las intersecciones con la calle y me quedo con la de menor tamaño
                segmento.x = aux.x;
                segmento.y = aux.y;
              }
        }
      }
    
    return segmento.mag();
  }
}