import java.util.ArrayList;

public interface ElemRedNeuronal {
	public ArrayList<Double> getActivacion(ArrayList<Double> entradas);
	public ArrayList<Double> getParametros();
	public void modificarParametro(int nroP, double valorP);
	public int getCantParametros();
	public void setCantEntradas(int C);
	public ElemRedNeuronal clone();
}
