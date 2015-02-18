import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GetalRij {
	private int[] getallen;

	public GetalRij(int aantal, int max) {
		// Belangrijke aanname: aantal < max, anders kunnen de getallen niet
		// uniek zijn.
		getallen = new int[aantal];
		vulArrayMetUniekeWaarden(aantal, max);
	}

	private void vulArrayMetUniekeWaarden(int aantal, int max) {
		// Vul een hulplijst met getallen 0, ..., max
		ArrayList hulpLijst = new ArrayList(max);
		for (int i = 0; i < max; i++) {
			hulpLijst.add(i);
		}

		// Stop 'aantal' random waarden in getallen
		Random r = new Random();
		for (int i = 0; i < aantal; i++) {
			// Het omzetten van Integer naar int gaat sinds Java 1.5 automatisch
			// (unboxing).
			int getal = (Integer) (hulpLijst
					.remove(r.nextInt(hulpLijst.size())));
			getallen[i] = getal;
		}
	}

	/**
	 * Opdracht 3 leertaak 2
	 * 
	 * @param zoekWaarde
	 * @return
	 */
	public boolean zitErinA(int zoekWaarde) {
		int counter = 0;
		boolean isFound = false;
		while (counter < getallen.length) {
			if (zoekWaarde == getallen[counter])
				isFound = true;
			counter++;
		}
		return isFound;
	}

	/**
	 * Opdracht 4 leertaak 2
	 * 
	 * @param zoekWaarde
	 * @return
	 */
	public boolean zitErinB(int zoekWaarde) {
		int counter = 0;
		while (counter < getallen.length) {
			if (zoekWaarde == getallen[counter])
				return true;
			counter++;
		}
		return false;
	}

	/**
	 * Binary search algorithm
	 * 
	 * @param zoekWaarde
	 * @param min
	 * @param max
	 * @return
	 */
	public boolean zitErinC(int zoekWaarde, int min, int max) {
		if (max < min) {
			return false;
		}
		else {
			int mid = (min + max) / 2;

			if (getallen[mid] > zoekWaarde)
				return zitErinC(zoekWaarde, min, mid - 1);
			else if (getallen[mid] < zoekWaarde)
				return zitErinC(zoekWaarde, mid + 1, max);
			else
				return true;
		}
	}
 
	public boolean zitErinD(int zoekWaarde) {
		return false;
	}

	public void sorteer() {
		Arrays.sort(getallen);
	}

	public int[] getGetallen() {
		return getallen;
	}

	public void print() {
		for (int i = 0; i < getallen.length; i++)
			System.out.println(getallen[i]);
	}

}
