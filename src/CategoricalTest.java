
public class CategoricalTest extends Test {

	public CategoricalTest(Attribute testAttribute) {
		super(testAttribute);
	}

	@Override
	public String printTest() {
		if(testAttribute!=null)
			return Integer.toString(testAttribute.getId());
		return "null";
		
	}

}
