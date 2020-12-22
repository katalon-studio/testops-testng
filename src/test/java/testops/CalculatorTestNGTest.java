package testops;

import static org.testng.Assert.assertEquals;

public class CalculatorTestNGTest {

    private Calculator calculator;

    @BeforeSuite
    public void beforeThisSuite() {
        System.out.println("BEFORE SUITE");
    }

    @BeforeTest
    public void beforeThisTest() {
        System.out.println("BEFORE TEST");
    }

    @BeforeClass
    public void beforeThisClass() {
        System.out.println("BEFORE CLASS");
    }

    @BeforeMethod
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void testAdd() {
        assertEquals(377, calculator.add(365, 12));
        assertEquals(377, calculator.add(12, 365));
    }

    @Test
    public void testSubtract() {
        assertEquals(353, calculator.subtract(365, 12));
        assertEquals(-353, calculator.subtract(12, 365));
    }

    @Test
    public void testMultiply() {
        assertEquals(4380, calculator.multiply(365, 12));
        assertEquals(4380, calculator.multiply(12, 365));
    }

    @Test
    public void testDivide() {
        assertEquals(36.5, calculator.divide(365, 10), 0);
    }

    @Test
    @Parameters({"a", "b", "result"})
    public void testAddParam(@Optional("321") int a, @Optional("123") int b, @Optional("444") int result) {
        assertEquals(result, calculator.add(a, b));
    }
}
