import optionsParser.OptionsParser;

/**
 * Created by erick on 10/07/16.
 */
public class Main {
    public static void main(String[] args) {
        String[] optionsArray1 = {"name", "Adolf", "is_alive", "level", "13", "age", "37"};
        String[] optionsArray2 = {"name", "Alice", "is_alive", "age", "13"};
        String[] optionsArray3 = {"name", "Bob", "level", "11", "age", "33"};
        String[] optionsArray4 = {"is_alive", "age", "42", "health", "67.3"};

        OptionsParser optionsParser = new OptionsParser(TestOptions.class, true, true);

        TestOptions options1 = (TestOptions) optionsParser.parse(optionsArray1);
        TestOptions options2 = (TestOptions) optionsParser.parse(optionsArray2);
        TestOptions options3 = (TestOptions) optionsParser.parse(optionsArray3);
        TestOptions options4 = (TestOptions) optionsParser.parse(optionsArray4);

        System.out.println(options1);
        System.out.println(options2);
        System.out.println(options3);
        System.out.println(options4);
    }
}
