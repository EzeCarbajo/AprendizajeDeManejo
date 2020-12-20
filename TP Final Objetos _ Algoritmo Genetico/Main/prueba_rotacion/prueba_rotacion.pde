PVector a = new PVector(10,10);
PVector b = new PVector(20,20);

void setup(){
  size(900,900);
}

void draw(){
  background(255);
  translate(450,450);
  line(a.x,a.y,b.x,b.y);
  a.rotate(radians(90));
  b.rotate(radians(90));
  System.out.println(a);
  System.out.println(b);
  line(a.x,a.y,b.x,b.y);
  while(true){
  }
}
