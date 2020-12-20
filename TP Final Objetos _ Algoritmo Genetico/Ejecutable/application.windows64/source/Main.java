import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {


Ruta r1 = new Ruta();
Auto a = new Auto();
Camara cam;
Entrenamiento e;
ArrayList<Dibujable> elements = new ArrayList<Dibujable>();


public void setup(){ //  SETUP se ejecuta solo una vez, al comienzo
   
   /*Creacion del moelo de la red neuronal*/
   r1.cargarCalles("Calles.txt");
   elements.add(r1); //elements es el conjunto de elementos del entorno
   ArrayList<Neurona> neurons = new ArrayList<Neurona>();
    /*capa 1*/
    Neurona n1 = new Neurona(new TangenteHiperbolica());
    Neurona n2 = new Neurona(new TangenteHiperbolica());
    Neurona n3 = new Neurona(new TangenteHiperbolica());
    
    neurons.add(n1);
    neurons.add(n2);
    neurons.add(n3);
    
    Capa c1 = new Capa(neurons);
    ArrayList<Double> inputs = new ArrayList<Double>();
    inputs.add(0.1d);
    inputs.add(2d);
    
    /*capa 2*/
    Neurona n4 = new Neurona(new TangenteHiperbolica());
    Neurona n5 = new Neurona(new TangenteHiperbolica());
    
    neurons = new ArrayList<Neurona> ();
    neurons.add(n4);
    neurons.add(n5);
    
    Capa c2 = new Capa(neurons);
    
    Red R1 = new Red();
    
    R1.addCapa(c1);
    R1.addCapa(c2);
    R1.setCantEntradas(3);
    /*creacion del entrenamiento con la red, la ruta, el tiempo maximo y el mutation rate*/
    e = new Entrenamiento(R1, 70, r1, 4000,0.4f);
    e.reset();
    a = e.getMejor().getAuto();
    cam = new Camara(a);
}

public void draw(){ //  DRAW se ejecuta una vez por frame

   background(255);
   cam.setEnfoque(e.getMejor().getAuto()); // obtengo el mejor auto del entrenamiento
   textSize(20);
   fill(0);
   text("Generacion: "+e.getNroGeneracion(),10,30);
   text("Fitness Score: "+e.getMejor().getIndividuo().getPuntos(), 10, 60);
   
   e.getMejor().getAuto().setColor(color(0,255,0)); // cambio el color del mejor auto
   cam.draw(elements); //dibujo todos los elementos
   cam.draw(e.getAutos()); //dibujo todos los autos del entrenamiento
   e.update(); //actualizo el estado del entrenamiento
   System.out.println(" punto  " +e.getMejor().getIndividuo().getPuntos());   
}

public void keyPressed(){
  if(key == 't'){
     e.terminarEntrenamiento(); 
  }
  
}
public class Auto implements Dibujable{
  private PVector vel = new PVector(0,0);
  private PVector pos = new PVector(0,0);
  private final int ancho=20,alto=30;
  private ArrayList<Sensor> sensores = new ArrayList<Sensor>();
  private int angulo = 270;
  private int Color = color(255,0,0); // color default rojo 
  private float aceleracion = 0;
  private float giro = 0f;  
  
  public Auto(){
      sensores.add(new Sensor(new PVector(0,0), new PVector(0,0)));
      sensores.add(new Sensor(new PVector(0,0), new PVector(0,0)));
      sensores.add(new Sensor(new PVector(0,0), new PVector(0,0)));
  }
  
  public void setColor(int nuevo){
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
    vel.add(0,-0.1f*aceleracion);
    
    if(vel.mag() > 0.4f){
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
public class Camara{
   private Auto enfoque;
   PVector desplazamiento = new PVector(0,0);
   
   public Camara(Auto d){
     this.enfoque = d;
   }
   
   public void setEnfoque(Auto d){
      this.enfoque = d; 
   }
   
   public PVector getDespl(){
    PVector res = new PVector((width/2) - enfoque.getPos().x, (height/2) - enfoque.getPos().y);
    return res;
   }
   
   public void draw(ArrayList<Dibujable> visibles){
     desplazamiento = getDespl();
       for(int i=0; i<visibles.size(); i++)
         visibles.get(i).draw(desplazamiento); // aca voy a tener la ruta, y los autos que no enfoco
       enfoque.draw(desplazamiento);
   }
}
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
           aux[j].x = PApplet.parseInt(punto[0]);
           aux[j].y = PApplet.parseInt(punto[1]);
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

public class Entrenamiento{
   private ArrayList<Dupla> vivos;
   private ArrayList<Dupla> muertos;
   private Red tipoRed;
   private Ruta ruta;
   private int tiempo = 0;
   private int t_max;
   private int nro_generacion = 0;
   private float mRate;

   public Entrenamiento(Red tipoRed, int tamanoPoblacion, Ruta ruta, int t_max, float mRate){
       this.ruta = ruta;
       this.tipoRed = tipoRed;
       vivos = new ArrayList<Dupla>(tamanoPoblacion);
       muertos = new ArrayList<Dupla>(tamanoPoblacion);
       
       //creo todos los individuos con redes que tengan esa misma estructura
       //creo su respectivo auto y los agrego a vivos
       for(int i=0; i<tamanoPoblacion; i++){
          Individuo i1 = new Individuo(tipoRed.clone());
          Auto a = new Auto();
          Dupla d = new Dupla(i1,a);
          vivos.add(d);
       }
       this.t_max = t_max;
       this.mRate = mRate;
   }
   
   public int getNroGeneracion(){
      return this.nro_generacion; 
   }
   /**
   resetea el puntaje de los individuos, pone sus autos en (0,0)*/
   private void reset(){
      for(int i=0; i<muertos.size(); i++){
         muertos.get(i).getIndividuo().setPuntos(0.0d);
         muertos.get(i).getIndividuo().setCantCheckpoints(0);
         muertos.get(i).getAuto().reset();
         vivos.add(muertos.get(i));
      }   
      muertos.clear();
   }
   
   private Dupla buscarMejor(ArrayList<Dupla> duplas){
      Dupla salida = duplas.get(0);
      for(int i=1; i< duplas.size(); i++)
        if(duplas.get(i).getIndividuo().getPuntos() > salida.getIndividuo().getPuntos())
          salida = duplas.get(i);
      return salida;
   }  
   
   /**Retorna la mejor dupla del entrenamiento, teniendo en cuenta los individuos vivos y los muertos*/
   public Dupla getMejor(){
      if(vivos.size() > 0 && muertos.size() > 0){
         Dupla mV = buscarMejor(vivos);
         Dupla mM = buscarMejor(muertos);
         if(mV.getIndividuo().getPuntos() > mM.getIndividuo().getPuntos())
           return mV;
         return mM;
      }else{
         if(vivos.size() > 0) return buscarMejor(vivos);
         return buscarMejor(muertos);
      }
      
   }
   /*Calcula los puntos de un individuo, basandose en la ubicacion del auto, las distancias y la cantidad
     de checkpoints pasados **/
   private void calcularPuntos(Individuo ind, Auto auto){
     int cantCP = ind.getCantCheckpoints();
     Checkpoint actual = ruta.getCP(cantCP), siguiente = ruta.getCP(cantCP + 1), anterior = ruta.getCP(cantCP - 1);
     // si la distancia del auto al siguiente cp es menor que la distancia entre los cp's, pase un checkpoint
     if(Geom.distanciaEuclidea(auto.getPos(), siguiente.getPos()) < Geom.distanciaEuclidea(actual.getPos(), siguiente.getPos())){ 
       //actualizo los checkpoints...
       cantCP++;
       anterior = actual;
       actual = siguiente;
       siguiente = ruta.getCP(cantCP);
       ind.setCantCheckpoints(cantCP);
     }
     double porcentajeParcial = 1 - (Geom.distanciaEuclidea(auto.getPos(), actual.getPos()) / Geom.distanciaEuclidea(anterior.getPos(), actual.getPos()));
     ind.setPuntos(cantCP+porcentajeParcial);
   }
   
   /**Retorna todos los autos del entrenamiento, vivos y muertos*/
   public ArrayList<Dibujable> getAutos(){
      ArrayList<Dibujable> salida = new ArrayList<Dibujable>();
      for(int i=0; i<vivos.size(); i++)
        salida.add(vivos.get(i).getAuto());
      for(int i=0; i<muertos.size(); i++)
        salida.add(muertos.get(i).getAuto());
      return salida;
   }
   
   /**Actualiza el estado actual del entrenamiento*/
   public void update(){
  
      tiempo++;
      System.out.println(tiempo);
      if(!vivos.isEmpty() && tiempo < t_max){
        for(int i=0; i<vivos.size(); i++){
            Auto autoAct = vivos.get(i).getAuto();
            Individuo indivAct = vivos.get(i).getIndividuo();
            ArrayList<Double> valoresSensores = autoAct.getValoresSensor(ruta);
            ArrayList<Double> salidaRed = indivAct.getRed().getActivacion(valoresSensores);
            double acelerar = salidaRed.get(0), girar = salidaRed.get(1);
            autoAct.setAcelerar((float) acelerar);
            autoAct.setGirar((float)girar);
            autoAct.mover();
            calcularPuntos(indivAct,autoAct);
            //actualizar puntos
            if(ruta.colision(autoAct.getPuntosColision())){ // si choco, lo saco de la lista vivos y lo pongo en muertos
              muertos.add(vivos.get(i));
              vivos.remove(i);
            }
        }
        
       for(int i=0; i<muertos.size(); i++)
            calcularPuntos(muertos.get(i).getIndividuo(),muertos.get(i).getAuto());
      }else{
        terminarEntrenamiento();
      }
   }
   
   /**Termina el entrenamiento, poniendo a todos los indivduos en muertos, mutandolos y reseteando sus condiciones.
     aumenta el numero de generacion y setea el tiempo en 0*/
   public void terminarEntrenamiento(){
      for(int i=0; i<vivos.size(); i++){
         System.out.println("se mato un ind");
         muertos.add(vivos.get(i));
      }
      vivos.clear();
      Collections.sort(muertos); 
      mutarIndividuos();
      reset();  
      tiempo = 0;
      nro_generacion++;
   }
   
   private void mutarIndividuos(){
     MutacionCompuesta m = new MutacionCompuesta(new MutacionDual(muertos.get(1).getIndividuo(),muertos.get(0).getIndividuo(),  mRate),new MutacionTotalRandom(), 0.7f);
     ArrayList<Individuo> inds = new ArrayList<Individuo>();
     for(int i=2; i<muertos.size(); i++)
       inds.add(muertos.get(i).getIndividuo());
     m.mutar(inds);
   }
}
public interface FormaMutacion{
   public abstract void mutar(ArrayList<Individuo> inds); 
}

public class MutacionRandom implements FormaMutacion{
  private float mutationRate;
   public MutacionRandom(float mutationRate){
      this.mutationRate = mutationRate;
   }
  
   
   /**
   Modifica parametros del individuo de manera aleatoria, segun mutationRate*/
   private void mutar(Individuo i){
      int tamanoMutacion = Math.round(i.getRed().getCantParametros() * mutationRate);  // funcion piso
      int parametroInicial = PApplet.parseInt(Math.round(Math.random() * i.getRed().getCantParametros()));
      ArrayList<Double> params = i.getRed().getParametros();
      while(parametroInicial <= tamanoMutacion){
        double variacion = Math.random() * 2 - 1;
        i.getRed().modificarParametro(parametroInicial % i.getRed().getCantParametros() , params.get(parametroInicial % params.size()) + variacion);
        parametroInicial++;
      }
   }
   
   public void mutar(ArrayList<Individuo> inds){
      for(int i=0; i<inds.size(); i++)
        mutar(inds.get(i));
   }
}

public class MutacionDual implements FormaMutacion{
   private Individuo i1,i2;
   private MutacionRandom m;
   public MutacionDual(Individuo i1, Individuo i2, float mutationRate){
      if(i1.getRed().getCantParametros() == i2.getRed().getCantParametros()){
      this.i1 = i1;
      this.i2 = i2;
      m = new MutacionRandom(mutationRate);
     }else{
       System.out.println("Error: Individuos incompatibles...");
     }
   }
   
   public MutacionDual(float mutationRate){
     m = new MutacionRandom(mutationRate);
   }
   
   public void setIndividuos(Individuo i1, Individuo i2){
     if(i1.getRed().getCantParametros() == i2.getRed().getCantParametros()){
      this.i1 = i1;
      this.i2 = i2;
     }else{
       System.out.println("Error: Individuos incompatibles...");
     }
   }
   
   private void mutar(Individuo i){
      int max = i1.getRed().getCantParametros(); //obtengo la cantidad total de parametros
      int tamanoMutacion = (int) Math.round(Math.random() * max/2d); //obtengo una longitud de modificacion que sea a lo sumo la mitad de los parametros 
      int nroParametro = max;
      while(nroParametro + tamanoMutacion > max){
          nroParametro = (int) Math.round(Math.random() * max);  //mientras que el tamaño de la mutacion + el primer parametro sean mayores que el maximo (voy a querer modificar un parametro fuera de rango)
      }                                                          // sigo buscando hasta que quiera modificar todos parametros validos
        
      Double [] parametros1 = new Double[max];
      Double [] parametros2 = new Double[max];
      
      parametros1 = i1.getRed().getParametros().toArray(parametros1);
      parametros2 = i2.getRed().getParametros().toArray(parametros2);
      for(int j=0 ;j<max; j++){
         //modifico todos los parametros del individuo, usando los de i2 e i1
         if( j >= nroParametro && j <=(nroParametro+tamanoMutacion))
            i.getRed().modificarParametro(j,parametros1[j]);
         else
            i.getRed().modificarParametro(j,parametros2[j]);
      }
   }
   
   public  void mutar(ArrayList<Individuo> inds){
      for(int i=0; i<inds.size(); i++){
        mutar(inds.get(i));
      }   
      m.mutar(inds);   
   }
}

public class MutacionCompuesta implements FormaMutacion{
   private FormaMutacion m1,m2;
   private float porcentaje_m1;
   /**Tiene dos mutaciones y un porcentaje de individuos a los que se le aplica m1.
   (1-porcentaje_m1) sera el porcentaje al que se le aplique m2. Primero se aplica m1, luego m2*/
   public MutacionCompuesta(FormaMutacion m1, FormaMutacion m2, float porcentaje_m1){
      this.m1 = m1;
      this.m2 = m2;
      this.porcentaje_m1 = porcentaje_m1;
   }
   
   public MutacionCompuesta(){
   }
   /*Setea dos mutaciones y un porcentaje de individuos a los que se le aplica m1.
   1-porcentaje_m1 sera el porcentaje al que se le aplique m2. Primero se aplica m1, luego m2**/
   public void setMutaciones(FormaMutacion m1, FormaMutacion m2, float porcentaje_m1){
      this.m1 = m1;
      this.m2 = m2;
      this.porcentaje_m1 = porcentaje_m1;
   }
   
   /*Muta los primeros porcentaje_m1 individuos con m1, los demas con m2**/
   public void mutar(ArrayList<Individuo> inds){
      int cantIndM1 = Math.round(inds.size() * this.porcentaje_m1);
      m1.mutar(new ArrayList<Individuo>(inds.subList(0,cantIndM1)));
      m2.mutar(new ArrayList<Individuo>(inds.subList(cantIndM1+1,inds.size())));
   }
}
   
public class MutacionTotalRandom implements FormaMutacion{
  public MutacionTotalRandom(){
       
  }
  public void mutar(ArrayList<Individuo> inds){
    for(int i=0; i<inds.size(); i++){
      inds.get(i).setRed(inds.get(i).getRed().clone()); //al clonarla, solo clona la estructura pero los parametros son generados todos aleatorios
    }
  }
}
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
  public void settings() {  size(800,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
