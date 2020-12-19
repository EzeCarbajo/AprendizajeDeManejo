ArrayList<PVector> puntos = new ArrayList<PVector>();
PVector desplazamiento = new PVector(0,0);

public void setup(){
  size(1500,900);
}

public void draw(){
   background(255);
   strokeWeight(3);
   stroke(0);
   ellipse(desplazamiento.x, desplazamiento.y, 4,4);
  
   for(int i=0; i<puntos.size()-1; i+=2)
       line(puntos.get(i).x , puntos.get(i).y , puntos.get(i+1).x , puntos.get(i+1).y ); 
  
     
   
   
   stroke(0,255,0);
   if(puntos.size() % 2  != 0)
     line(mouseX , mouseY , puntos.get(puntos.size()-1).x  , puntos.get(puntos.size()-1).y );
}

public void mouseClicked(){
   PVector aux = new PVector(0,0);
   aux.x = mouseX;
   aux.y = mouseY;
   stroke(0,255,0);
   ellipse(aux.x, aux.y, 10,10);
   puntos.add(aux);
}

void guardar(){// una calle por linea - > 4 puntos por linea
    int i;
    String datos = new String();
    for( i=0; i<puntos.size()-4; i+=4)  // delimito las componentes con ","  , los vectores con ";" y las lineas con  "."
      datos += puntos.get(i).x + "," + puntos.get(i).y + ";" + puntos.get(i+1).x + "," + puntos.get(i+1).y + ";" + puntos.get(i+2).x + "," + puntos.get(i+2).y + ";" + puntos.get(i+3).x + "," + puntos.get(i+3).y + "\n";
    datos += puntos.get(i).x + "," + puntos.get(i).y + ";" + puntos.get(i+1).x + "," + puntos.get(i+1).y + ";" + puntos.get(i+2).x + "," + puntos.get(i+2).y + ";" + puntos.get(i+3).x + "," + puntos.get(i+3).y ;
    String [] lineas = split(datos, "\n");
    saveStrings("Calles.txt", lineas);
}

void keyPressed(){
   if(key == 'z'){
     if(puntos.size() >= 2){
       puntos.remove(puntos.size()-1);
       puntos.remove(puntos.size()-1);
     }
   }
   
   if(key == 'g')
     guardar();
   
   if(key == 'w'){
     for(int i=0; i<puntos.size(); i++)
       puntos.get(i).y+=10;
     desplazamiento.y+=10;
   }
   if(key == 's'){
     for(int i=0; i<puntos.size(); i++)
        puntos.get(i).y-=10;
     desplazamiento.y-=10;
   }
   if(key == 'a'){
     for(int i=0; i<puntos.size(); i++)
       puntos.get(i).x+=10;
     desplazamiento.x+=10;
   }
   if(key == 'd'){
     for(int i=0; i<puntos.size(); i++)
        puntos.get(i).x-=10;  
     desplazamiento.x-=10;
   }
    
}
