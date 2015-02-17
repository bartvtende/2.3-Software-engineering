public class SnelheidOefening {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GetalRij gr = new GetalRij(100000, 200000);
		System.out.println("Starttijd A: " + tijd());
		System.out.println(gr.zitErinA(0));
		System.out.println("Eindtijd A: " + tijd());
		System.out.println("Starttijd B: " + tijd());
		System.out.println(gr.zitErinB(0));
		System.out.println("Eindtijd B: " + tijd());
		gr.sorteer();
		int getallenLength = gr.getGetallen().length;
		System.out.println("Starttijd C: " + tijd());
		System.out.println(gr.zitErinC(0, 0, getallenLength-1));
		System.out.println("Eindtijd C: " + tijd());

	}

	// Hulpmethode voor tijdsbepaling
	private static long tijd() {
		return System.currentTimeMillis();
	}

}
