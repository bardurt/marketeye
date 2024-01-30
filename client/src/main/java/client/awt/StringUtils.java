package client.awt;

public class StringUtils {

    public static String repeat(String value, int count){
        if(count <= 0){
            return value;
        }

        if(value.isEmpty()){
            return value;
        }

        return value.repeat(count);
    }

    public static String repeatAndPad(String value, int count, int maxCount){
        if(count <= 0){
            return value;
        }

        if(value.isEmpty()){
            return value;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(value.repeat(count));

        if(count < maxCount){
            stringBuilder.append(" ".repeat(maxCount - count));
        }

        return stringBuilder.toString();
    }
}
