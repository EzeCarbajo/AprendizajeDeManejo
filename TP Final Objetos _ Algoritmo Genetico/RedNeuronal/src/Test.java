import java.util.ArrayList;

public class Test {

	


		
	
	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
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
		inputs.add(99.9d);
		inputs.add(200.0d);
		inputs.add(99.9d);
		
		
		
		System.out.println(R1.getActivacion(inputs));
	}

}
