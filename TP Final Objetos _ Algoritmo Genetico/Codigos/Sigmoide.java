
public class Sigmoide implements FuncionActivacion {

	public Sigmoide() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * retorna el valor que resulta de evaluar la funcion sigmoide en el valor x
	 * @param x
	 */
	@Override
	public double calcularValor(double x) {
		return 1/(1+Math.exp(-x));
	}

}
