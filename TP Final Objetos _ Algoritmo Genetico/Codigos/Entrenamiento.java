import java.util.*;
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
     MutacionCompuesta m = new MutacionCompuesta(new MutacionDual(muertos.get(1).getIndividuo(),muertos.get(0).getIndividuo(),  mRate),new MutacionTotalRandom(), 0.7);
     ArrayList<Individuo> inds = new ArrayList<Individuo>();
     for(int i=2; i<muertos.size(); i++)
       inds.add(muertos.get(i).getIndividuo());
     m.mutar(inds);
   }
}