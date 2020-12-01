package client.awt;

public class StringUtils {


    public static String repeat(String value, int count){
        if(count <= 0){
            return value;
        }

        if(value.isEmpty()){
            return value;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < count; i++){
            stringBuilder.append(value);
        }

        return stringBuilder.toString();
    }

    public static String repeatAndPad(String value, int count, int maxCount){
        if(count <= 0){
            return value;
        }

        if(value.isEmpty()){
            return value;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < count; i++){
            stringBuilder.append(value);
        }

        if(count < maxCount){
            for(int i = count; i < maxCount; i++){
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
}
