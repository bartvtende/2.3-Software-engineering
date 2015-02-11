package arrayException;

public class Start {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {5, 6, 7};
		int[] b = {2, 5};
		
		try {
			int[] product = addArrays(a, b);
			for(int i = 0; i<product.length; i++){
				System.out.println(product[i]);
			}
		} catch (ArraySizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static int[] addArrays(int[] arrayA, int[] arrayB) throws ArraySizeException{
		
		if(arrayA.length == arrayB.length){
			int[] product = new int[arrayA.length];
			for(int i = 0; i<arrayA.length; i++){
				product[i] = arrayA[i]+arrayB[i];
			}
			return product;
		}
		else{
			throw new ArraySizeException("De lengtes van de arrays zijn niet gelijk. Lengte 1:" + arrayA.length + ". Lengte 2:" + arrayB.length +".");
		}
		
	}
}
