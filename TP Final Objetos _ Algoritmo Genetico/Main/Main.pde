
Ruta r1 = new Ruta();
Auto a = new Auto();
Camara cam;
Entrenamiento e;
ArrayList<Dibujable> elements = new ArrayList<Dibujable>();


void setup(){ //  SETUP se ejecuta solo una vez, al comienzo
   size(800,800);
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
    e = new Entrenamiento(R1, 70, r1, 4000,0.4);
    e.reset();
    a = e.getMejor().getAuto();
    cam = new Camara(a);
}

void draw(){ //  DRAW se ejecuta una vez por frame

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

void keyPressed(){
  if(key == 't'){
     e.terminarEntrenamiento(); 
  }
  
}
