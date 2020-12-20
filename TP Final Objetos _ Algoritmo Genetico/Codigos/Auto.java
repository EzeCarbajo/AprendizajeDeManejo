public class Auto implements Dibujable{
  private PVector vel = new PVector(0,0);
  private PVector pos = new PVector(0,0);
  private final int ancho=20,alto=30;
  private ArrayList<Sensor> sensores = new ArrayList<Sensor>();
  private int angulo = 270;
  private color Color = color(255,0,0); // color default rojo 
  private float aceleracion = 0;
  private float giro = 0f;  
  
  public Auto(){
      sensores.add(new Sensor(new PVector(0,0), new PVector(0,0)));
      sensores.add(new Sensor(new PVector(0,0), new PVector(0,0)));
      sensores.add(new Sensor(new PVector(0,0), new PVector(0,0)));
  }
  
  public void setColor(color nuevo){
     this.Color = nuevo; 
  }
  
  public ArrayList<PVector> getPuntosColision(){ 
     ArrayList<PVector> puntosColision = new ArrayList<PVector>();
     PVector fr,fl,br,bl;
     translate(pos.x, pos.y);
     fl = new PVector(- ancho/2, - alto/2);
     fr = new PVector(ancho/2, - alto/2);
     bl = new PVector(- ancho/2, alto/2);
     br = new PVector(ancho/2, alto/2);
     
     fl.rotate(radians(angulo-270));
     fr.rotate(radians(angulo-270));
     bl.rotate(radians(angulo-270));
     br.rotate(radians(angulo-270));
     translate(-pos.x, -pos.y);
     
     fl.add(pos);
     fr.add(pos);
     bl.add(pos);
     br.add(pos);
     
     puntosColision.add(fl); //adelante izq
     puntosColision.add(fr); //adelante der
     puntosColision.add(bl); // atras izq
     puntosColision.add(br); // atras der
     return puntosColision;
  }
  
  public void reset(){
     this.pos.x = 0;
     this.pos.y = 0;
     this.vel.x = 0;
     this.vel.y = 0;
     this.giro = 0;
     this.angulo = 270;
     actualizarSensores();
     this.Color = color(255,0,0);
  }
  
  public void setGirar(float giro){
    this.giro = giro;
  }
  
  public void setAcelerar(float aceleracion){
    this.aceleracion = aceleracion;
  }
  
  
  public void detener(){
     this.vel.x = 0; this.vel.y= 0;}
     
     
  
  private void actualizarSensores(){    
    sensores.get(0).mover(this, new PVector(-ancho/2, -alto/2), new PVector(-100,-100));
    sensores.get(1).mover(this, new PVector(0, -alto/2), new PVector(0,-200));
    sensores.get(2).mover(this, new PVector(ancho/2, -alto/2), new PVector(100,-100));
  }
  
  public void mover(){ 
    vel.rotate(radians(270-angulo));
    vel.add(0,-0.1*aceleracion);
    
    if(vel.mag() > 0.4){
      angulo -= giro*vel.mag();
    }
    
    vel.rotate(radians(angulo-270));
    vel.limit(5);
    
    pos.add(vel);
    
    // muevo los sensores
    actualizarSensores(); 
    
  }
  
  public PVector getPos(){
     return pos; 
  }
  
  public int getAngulo(){
     return this.angulo; 
  }
  
  public void draw(PVector desplazamiento){
    stroke(0);
    pushMatrix();
      fill(Color);
      translate(pos.x + desplazamiento.x, pos.y + desplazamiento.y);
      rotate(radians(angulo-270));
      rect(-(ancho/2), -(alto/2), ancho, alto);
    popMatrix();  
    for(int i=0; i<sensores.size(); i++)
        sensores.get(i).draw(desplazamiento);
    
    
  }
  
  public ArrayList<Double> getValoresSensor(Colisionable ruta){
     ArrayList<Double> salida = new ArrayList<Double>();
     for(int i=0; i < sensores.size(); i++)
       salida.add(sensores.get(i).calcularValor(ruta));
     
     return salida;
  }
}