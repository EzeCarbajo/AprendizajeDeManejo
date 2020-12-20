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