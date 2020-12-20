public class Individuo implements Comparable<Individuo>{
  Red red;
  double puntos = 0;
  int cantCheckpoints = 0;
  public Individuo(Red red){
    this.red = red;
  }
  
  public double getPuntos(){
    return puntos;
  }
  
  public void setPuntos(double puntos){
    this.puntos = puntos;
  }
  
  public Red getRed(){
    return red;
  }
  
  public void setRed(Red r){
    this.red = r;
  }
  
  public int compareTo(Individuo comp){
    double puntComp = comp.getPuntos();
    if (puntos < puntComp)
      return 1;
    else if(puntos == puntComp)
      return 0;
    else
      return -1;
  }
  
  public void setCantCheckpoints(int cant){
     this.cantCheckpoints = cant; 
  }
  
  public int getCantCheckpoints(){
     return this.cantCheckpoints;
  }
    
}
