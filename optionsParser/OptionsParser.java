package optionsParser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erick on 09/07/16.
 */

// TODO(erick): Canonize the options 'name'. The user should choose between
//    camel case or underscore separated words
public class OptionsParser {
    private boolean continueOnError;
    private boolean shouldOverrideAccessibility;
    private Class<? extends Options> optionsClass;


    public OptionsParser(Class<? extends Options> optionsClass,
                         boolean continueOnError,
                         boolean shouldOverrideAccessibility) {
//        TODO(erick): Find out how to ensure that optionsClass extends Options
        this.optionsClass = optionsClass;
        this.continueOnError = continueOnError;
        this.shouldOverrideAccessibility = shouldOverrideAccessibility;
    }

    public OptionsParser(Class<? extends Options> optionsClass) {
        this(optionsClass, true, false);
    }

    public Object parse(String[] args) {
        Map<String, Field> requiredFieldMap = new HashMap<>();
        Map<String, Field> optionalFieldMap = new HashMap<>();

        Object result = null;
        try {
            result = optionsClass.newInstance();

            Field[] fields = optionsClass.getDeclaredFields();
            for(Field field : fields) {
//                NOTE(erick): If a field is not marked with a @Option annotation we just ignore it
                if(!field.isAnnotationPresent(Option.class))
                    continue;

                Option annotation = field.getAnnotation(Option.class);
                String fieldName = annotation.name().equals("") ? field.getName() : annotation.name();

                if(annotation.isRequired()) {
                    requiredFieldMap.put(fieldName, field);
                } else {
                    optionalFieldMap.put(fieldName, field);
                }
            }

            ArrayIterator<String> iterator = new ArrayIterator<>(args);
            while (iterator.hasNext()) {
                String optionName = iterator.next();

//                NOTE(erick): First we need to find out in which of the Maps the current
//                  option is. We do it by setting the currentField variable or
//                  breaking/continuing in case of error
//                NOTE(erick): Since we will handle the option, its Field is removed from
//                  the corresponding Map.
                Field currentField;
                if(requiredFieldMap.containsKey(optionName)) {
                    currentField = requiredFieldMap.get(optionName);
                    requiredFieldMap.remove(optionName);
                }
                else if(optionalFieldMap.containsKey(optionName)) {
                    currentField = optionalFieldMap.get(optionName);
                    optionalFieldMap.remove(optionName);
                }
                else {
                    System.err.println("Unknown option: " + optionName);
                    if(continueOnError) {
                        continue;
                    } else {
                        break;
                    }
                }

//                  NOTE(erick): By the point we already know the field we need to set
//                      (read the above comment).
                if(isBooleanField(currentField)) {
                    setBooleanOptionField(result, currentField, true);
                }
                else {
                    String valueString = iterator.next();
                    setObjectField(result, currentField, valueString);
//                        TODO(erick): Decide how errors will be reported from setObjectField and handle it
                }
//            NOTE(erick): Ending of the parsing loop.
            }


            if(!requiredFieldMap.isEmpty()) {
//                TODO(erick): If required options were not filled we have an error.
            }

//            NOTE(erick): For all the remaining optional options, if the user has passed a default value
//              we must set it to the given value.
            for(Field field : optionalFieldMap.values()) {
                Option annotation = field.getAnnotation(Option.class);
                String defaultValue = annotation.defaultValue();

//                NOTE(erick): If a boolean option is left unset we set it to false
                if(isBooleanField(field)) {
                    setBooleanOptionField(result, field, false);
                }
                else if(!defaultValue.equals("")) {
                    setObjectField(result, field, defaultValue);
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void setBooleanOptionField(Object obj, Field field, boolean value) throws IllegalAccessException {
        if(shouldOverrideAccessibility) {
//        NOTE(erick): We need to do this so we can access private Fields
            field.setAccessible(true);
        }

        field.set(obj, value);

        if(shouldOverrideAccessibility) {
//        NOTE(erick): Setting the Accessible flag back to false
            field.setAccessible(false);
        }
    }

    private static boolean isBooleanField(Field field) {
        Class fieldType = field.getType();

        if (fieldType.isAssignableFrom(boolean.class)) return true;
        if (fieldType.isAssignableFrom(Boolean.class)) return true;
        return false;
    }

    private void setObjectField(Object obj, Field field, String valueString) throws IllegalAccessException {
        if(valueString == null || valueString == "") {
//            TODO(erick): Illegal value.
        }

        Class fieldType = field.getType();

        if(shouldOverrideAccessibility) {
//        NOTE(erick): We need to do this so we can access private Fields
            field.setAccessible(true);
        }

        if(fieldType.isAssignableFrom(int.class) ||
                fieldType.isAssignableFrom(Integer.class)) {
            int value = Integer.parseInt(valueString);
            field.set(obj, value);
        }
        else if(fieldType.isAssignableFrom(long.class) ||
                fieldType.isAssignableFrom(Long.class)) {
            long value = Long.parseLong(valueString);
            field.set(obj, value);
        }
        else if(fieldType.isAssignableFrom(short.class) ||
                fieldType.isAssignableFrom(Short.class)) {
            short value = Short.parseShort(valueString);
            field.set(obj, value);
        }

        else if(fieldType.isAssignableFrom(double.class) ||
                fieldType.isAssignableFrom(Double.class)) {
            double value = Double.parseDouble(valueString);
            field.set(obj, value);
        }
        else if(fieldType.isAssignableFrom(float.class) ||
                fieldType.isAssignableFrom(Float.class)) {
            float value = Float.parseFloat(valueString);
            field.set(obj, value);
        }

        else if(fieldType.isAssignableFrom(char.class)) {
            char value = valueString.charAt(0);
            field.setChar(obj, value);
        }

        else if(fieldType.isAssignableFrom(String.class)) {
            field.set(obj, valueString);
        }
        else {
//            TODO(erick): The option type is not supported
            System.err.println("Option type not supported: " + fieldType);
        }

        if(shouldOverrideAccessibility) {
//        NOTE(erick): Setting the Accessible flag back to false
            field.setAccessible(false);
        }
    }
}
