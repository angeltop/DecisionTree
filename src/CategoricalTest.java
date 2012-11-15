
public class CategoricalTest extends Test {
	/* If the attribute is categorical we need only its id
	 */
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
