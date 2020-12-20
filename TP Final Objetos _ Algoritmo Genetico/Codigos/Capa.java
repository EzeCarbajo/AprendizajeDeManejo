import java.util.ArrayList;


public class Capa implements ElemRedNeuronal {
	private ArrayList<Neurona> neuronas;
	private Capa anterior;
	/**
	 * Crea la capa con las neuronas correspondientes
	 * @param neuronas
	 */
	public Capa(ArrayList<Neurona> neuronas) {
		this.neuronas = neuronas;
	}
	/**
	 * Clona la capa y todas sus neuronas, pero solo su estructura.*
	 */
	public ElemRedNeuronal clone(){
		ArrayList<Neurona> neus = new ArrayList<Neurona>();
		for(int i=0; i<neuronas.size(); i++)
			neus.add((Neurona) neuronas.get(i).clone());
		
		return new Capa(neus);
	}
	
	/**
	 *Como las entradas de la capa son las activaciones de la capa anterior, entradas sera el conjunto de valores
	 *para la capa final, es decir, aquella capa cuya anterior sea nula. 
	 */
	@Override
	public ArrayList<Double> getActivacion(ArrayList<Double> entradas) { 
		ArrayList<Double> salida = new ArrayList<Double>();
		if(anterior != null){
			ArrayList<Double> activacionesAnterior = anterior.getActivacion(entradas);
			for(int i=0; i<neuronas.size(); i++)
				salida.addAll(neuronas.get(i).getActivacion(activacionesAnterior)); // si tengo anterior, le pido sus activaciones
		}else{
			for(int i=0; i<neuronas.size(); i++)
				salida.addAll(neuronas.get(i).getActivacion(entradas)); // si no tengo anterior, calculo mis activaciones en base a las entradas que me pasan
		}
		
		return salida;	
	}
	
	/**
	 * @param c la Capa anterior a asignar
	 * 
	 * */
	public void setCapaAnterior(Capa c){
		anterior = c;
	}
	/**
	 * Retorna todos los parametros de las neuronas que contiene, en orden
	 * @return
	 */
	public ArrayList<Double> getParametros() {
		ArrayList<Double> parametros= new ArrayList<Double>();
		for(int i=0; i<neuronas.size(); i++)
			parametros.addAll(neuronas.get(i).getParametros());
		
		return parametros;
	}
	/**
	 * Retorna la cantidad de neuronas que contiene la capa
	 * @return
	 */
	public int getCantNeuronas(){return neuronas.size();}
	/**
	 * Modifica un parametro de la capa, asignandole un valor dado.
	 */
	public void modificarParametro(int nroP, double valorP) {
		for(int i=0; i<neuronas.size(); i++){
			int cantParametrosNeuronaI = neuronas.get(i).getCantParametros();
			if(nroP < cantParametrosNeuronaI){
				neuronas.get(i).modificarParametro(nroP, valorP);
				break;
			}
			else
				nroP-= cantParametrosNeuronaI;
		}

	}
	/**
	 * Retorna la cantidad de parametros total de la neurona, asumiendo
	 * que todas estas son iguales.
	 */
	public int getCantParametros() {
		if(neuronas.size() > 0)
			return this.getCantNeuronas() * neuronas.get(0).getCantParametros();  // todo esto anda asumiendo que todas las neuronas de la capa son iguales
		return 0;
	}
	
	public void setCantEntradas(int C){
		for(int i=0; i<neuronas.size(); i++)
			neuronas.get(i).setCantEntradas(C);
	}

}
