
public class TangenteHiperbolica implements FuncionActivacion {

	public TangenteHiperbolica() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double calcularValor(double x) {
		return Math.tanh(x);
	}

}
