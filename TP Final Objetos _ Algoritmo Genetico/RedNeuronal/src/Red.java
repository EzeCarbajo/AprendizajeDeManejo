import java.util.ArrayList;

public class Red implements ElemRedNeuronal {
	private ArrayList<Capa> capas; //las capas que la red contiene
	
	public Red() {
		capas = new ArrayList<Capa>();
	}
	
	public ElemRedNeuronal clone(){
		Red salida = new Red();
		for(int i=0; i<capas.size(); i++)
			salida.addCapa((Capa) capas.get(i).clone());
		return salida;
	}
	
	/**
	 * retorna la salida de la red, dada una entrada.
	 */
	@Override
	public ArrayList<Double> getActivacion(ArrayList<Double> entradas) {
		if(capas.size() > 0)
			return capas.get(capas.size()-1).getActivacion(entradas);
		else 
			return new ArrayList<Double>();
	}
	/**
	 * retorna todos los parametros de la red en orden, segun capas y neuronas,
	 * desde la primer capa oculta hasta la capa de salida.
	 */

	@Override
	public ArrayList<Double> getParametros() {
		ArrayList<Double> parametros= new ArrayList<Double>();
		
		for(int i=0; i<capas.size(); i++)
			parametros.addAll(capas.get(i).getParametros());
		return parametros;	
	}

	/**
	 * permite modificar un parametro de la red por un valor dado
	 */
	@Override
	public void modificarParametro(int nroP, double valorP) {
		for(int i=0; i<capas.size(); i++){
			int cantParametrosCapaI = capas.get(i).getCantParametros();
			if(nroP < cantParametrosCapaI){
				capas.get(i).modificarParametro(nroP, valorP);
				break;
			}
			else 
				nroP -= cantParametrosCapaI;
		}
	}
	
	/**
	 * retorna la cantidad total de parametros de la red
	 */
	@Override
	public int getCantParametros() {
		int suma = 0;s
		for(int i=0; i<capas.size(); i++){
			suma+= capas.get(i).getCantParametros();
		}
		return suma;
	}
	/**
	 * se agregara a la ultima capa de la red
	 * @param c
	 */
	public void addCapa(Capa c){
		capas.add(c);
		if(capas.size() > 1)
			c.setCapaAnterior(capas.get(capas.size()-2));
	}
	public void setCantEntradas(int C){
		if(capas != null){
			capas.get(0).setCantEntradas(C);
			for(int i=1; i<capas.size(); i++)
				capas.get(i).setCantEntradas(capas.get(i-1).getCantNeuronas());
		}
	}

}
