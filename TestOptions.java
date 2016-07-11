import optionsParser.Option;
import optionsParser.Options;

/**
 * Created by erick on 09/07/16.
 */
public class TestOptions extends Options {

    @Option
    private String name;

    @Option (
            isRequired = true,
            name = "is_alive"
    )
    private Boolean isAlive;

    @Option (
            isRequired = true
    )
    private int age;

    @Option (
            defaultValue = "1"
    )
    private Integer level;

    @Option (
            defaultValue = "13.13"
    )
    private Double health;


    private boolean thisMustBeTrue = true;


    public String toString() {
        String result = "";

        result += "\t Name: " + name + "\n";
        result += "\t IsAlive?: " + isAlive + "\n";
        result += "\t Age: " + age + "\n";
        result += "\t Level: " + level + "\n";
        result += "\t Health: " + health + "\n";
        result += "\t thisMustBeTrue: " + thisMustBeTrue + "\n";

        return result;
    }
}
