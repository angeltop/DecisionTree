
public class CategoricalTest extends Test {

	public CategoricalTest(Attribute testAttribute) {
		super(testAttribute);
	}

	@Override
	public String printTest() {
		return Integer.toString(testAttribute.getId());
	}

}
