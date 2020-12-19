public class Dupla implements Comparable<Dupla>{
  private Individuo ind;
  private Auto auto;
  
  public Dupla(Individuo ind, Auto auto){
    this.ind = ind;
    this.auto = auto;
  }
  public Auto getAuto(){
    return auto;
  }
  
  public Individuo getIndividuo(){
    return ind;
  }
  
  public int compareTo(Dupla comp){
    return ind.compareTo(comp.getIndividuo());
  }
}
