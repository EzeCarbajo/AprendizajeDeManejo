  public static class Geom{
    public static float distanciaRecta(PVector punto, PVector PuntoLinea1, PVector PuntoLinea2){
    if(PuntoLinea1.x != PuntoLinea2.x){
       float a = (PuntoLinea2.y - PuntoLinea1.y) / (PuntoLinea2.x - PuntoLinea1.x);
       float b = ((-PuntoLinea1.x*(PuntoLinea2.y - PuntoLinea1.y)) / (PuntoLinea2.x - PuntoLinea1.x)) + PuntoLinea1.y; 
       
       return abs(a * punto.x - punto.y + b)/(sqrt(pow(a,2) + 1));
    }else
         return abs(punto.x - PuntoLinea1.x);
  }

 public static float distanciaSegmento(PVector Pc, PVector Pa, PVector Pb){
     float u = ((Pc.x - Pa.x)*(Pb.x - Pa.x) + (Pc.y - Pa.y)*(Pb.y - Pa.y))/( pow(Pb.x - Pa.x, 2) + pow(Pb.y - Pa.y, 2) );
     if(u < 0){ 
       return  sqrt( pow(Pc.x - Pa.x, 2f) + pow(Pc.y - Pa.y, 2f) );
     }else if(u > 1){ 
       return  sqrt( pow(Pc.x - Pb.x,2f) + pow(Pc.y - Pb.y,2f) );
     }else if(u <= 1 && u>=0){
       return distanciaRecta(Pc,Pa,Pb);
     }
     
     return -1;
  }
  
  public static PVector puntoInterseccion(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
    float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
    float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
  
    if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
  
      float intersectionX = x1 + (uA * (x2-x1));
      float intersectionY = y1 + (uA * (y2-y1));
  
      return new PVector(intersectionX, intersectionY);
    }
      return null;
  }
  
  public static float distanciaEuclidea(PVector a, PVector b){
    return sqrt(pow(a.x - b.x ,2) + pow(a.y - b.y, 2));
  }
  
  public static PVector puntoInterseccion(PVector p1, PVector p2, PVector p3, PVector p4) {
    return puntoInterseccion(p1.x,p1.y,p2.x,p2.y,p3.x,p3.y,p4.x,p4.y);
  }
}
