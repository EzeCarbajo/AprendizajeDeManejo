public interface Colisionable extends Dibujable{
  public abstract boolean colision(ArrayList<PVector> puntos);
  public abstract void draw(PVector desplazamiento);
  public ArrayList<PVector> calcularIntersecciones(PVector p1, PVector p2);
}