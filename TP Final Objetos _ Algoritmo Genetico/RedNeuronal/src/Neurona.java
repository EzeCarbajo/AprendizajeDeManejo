import java.util.ArrayList;

public class Neurona implements ElemRedNeuronal {
	private FuncionActivacion activation;
	private Double [] weights;
	private Double bias;
	public Neurona(FuncionActivacion activation) {
		this.activation= activation;
		this.bias= Math.random(); 
		weights = new Double[1];
	}
	/**
	 * Clona la neurona solo basandose en la estructura*/
	public ElemRedNeuronal clone(){
		Neurona salida = new Neurona(this.activation);
		salida.setCantEntradas(weights.length);
		return salida;
	}
	
	
	/**
	 * Este metodo permite pre-setear la cantidad de parametros que necesita la red
	 * pero si la cantidad de entradas que utilizamos es diferente, se auto calculara
	 * al llamar getActivacion(...) segun el tamaño de las entradas
	 * @param C
	 */
	public void setCantEntradas(int C){  
		this.weights = new Double[C];
		for(int i=0; i<C; i++)
			weights[i]= Math.random()*2-1; // valores entre -1 y 1
	}
	/**
	 * Retorna la activacion de la neurona para una salida dada.
	 * Ademas, recalcula la cantidad necesaria de parametros para
	 * poder calcular la activacion, segun el tamaño de la entrada
	 */
	public ArrayList<Double> getActivacion(ArrayList<Double> entradas) {
		if(weights.length != entradas.size())
			this.setCantEntradas(entradas.size()); // si mi cantidad de parametros no concuerda con la cantidad de entradas, lo corrijo
		
		Double suma = 0.0;
		for(int i=0; i<entradas.size(); i++)
			suma+=entradas.get(i) * weights[i];
		
		ArrayList<Double> salida = new ArrayList<Double>();
		salida.add(activation.calcularValor(suma + bias));
		
		return salida;
	}
	/**
	 * Retorna todos los parametros de la neurona, esto es
	 * todos los weights en orden y el bias al final.
	 */
	public ArrayList<Double> getParametros() {
		ArrayList<Double> parametros = new ArrayList<Double>();
		for(int i=0; i<weights.length; i++)
			parametros.add(weights[i]);
		
		parametros.add(bias);
		
		return parametros;
	}
	/**
	 * Modifica un parametro de la neurona por un valor dado
	 * @param nroP
	 * @param valorP
	 */
	public void modificarParametro(int nroP, double valorP) {
		if(nroP < weights.length)
			weights[nroP] = valorP;
		
		if(nroP == weights.length)
			bias = valorP;
	}
	/**
	 * 
	 * Retorna la cantidad de parametros de la neurona. Dado que no conzco
	 * el tamaño de mi entrada hasta que ejecuto getActivacion(...), esto siempre 
	 * me dara la cantidad de parametros que uso en su ultima ejecucion de dicho 
	 * metodo
	 */
	public int getCantParametros() {
		return 1 + weights.length;
	}
	
}
