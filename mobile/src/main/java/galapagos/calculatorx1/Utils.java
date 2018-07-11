package galapagos.calculatorx1;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String ADDITION = "+";
    private static final String SUBTRACTION = "-";
    private static final String MULTIPLICATION = "*";
    private static final String DIVISION = "/";
    private static final String DIVIDE_BALANCE = "%";

    public static String clearString(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        if (str.length() == 0) {
            return "0";
        }
        return str;
    }

    /**
     * 文字列からの数字を分析する
     *
     * @param stringInput  値を入力する
     *
     * @return ArrayList<String>
     */
    public static ArrayList<Double> addNumber(String stringInput) {
        ArrayList<Double> arrNumber = new ArrayList<>();
        Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = regex.matcher(stringInput);
        while(matcher.find()){
            arrNumber.add(Double.valueOf(matcher.group(1)));
        }
        return arrNumber;
    }

    /**
     * 文字列からのオペレーターを分析する
     *
     * @param stringInput  値を入力する
     *
     * @return ArrayList<String>
     */
    public static ArrayList<String> addOperation(String stringInput) {
        ArrayList<String> arrOperation = new ArrayList<>();
        char[] cArray = stringInput.toCharArray();
        for (int i = 0; i < cArray.length; i++) {
            switch (cArray[i]) {
                case '+':
                    arrOperation.add(cArray[i] + "");
                    break;
                case '-':
                    arrOperation.add(cArray[i] + "");
                    break;
                case '*':
                    arrOperation.add(cArray[i] + "");
                    break;
                case '/':
                    arrOperation.add(cArray[i] + "");
                    break;
                case '%':
                    arrOperation.add(cArray[i] + "");
                    break;
                default:
                    break;
            }
        }
        return arrOperation;
    }

    /**
     * 精算を実行するメソッド
     *
     * @param context  値を入力する
     * @param inputString  値を入力する
     *
     * @return String
     */
    public static String compute(Context context, String inputString) {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(3);
        double result = 0;
        ArrayList<String> arrOperation = Utils.addOperation(inputString);
        ArrayList<Double> arrNumber = Utils.addNumber(inputString);
        if( arrOperation.size() >= arrNumber.size() + 1 || arrOperation.size() < 1 ){
           return context.getString(R.string.error_format);
        }else {
            for (int i = 0; i < (arrNumber.size() - 1); i++) {
                switch (arrOperation.get(i)) {
                    case ADDITION:
                        if (i == 0) {
                            result = arrNumber.get(i) + arrNumber.get(i + 1);
                        } else {
                            result = result + arrNumber.get(i + 1);
                        }
                        break;
                    case SUBTRACTION:
                        if (i == 0) {
                            result = arrNumber.get(i) - arrNumber.get(i + 1);
                        } else {
                            result = result - arrNumber.get(i + 1);
                        }
                        break;
                    case MULTIPLICATION:
                        if (i == 0) {
                            result = arrNumber.get(i) * arrNumber.get(i + 1);
                        } else {
                            result = result * arrNumber.get(i + 1);
                        }
                        break;
                    case DIVISION:
                        if (i == 0) {
                            result = arrNumber.get(i) / arrNumber.get(i + 1);
                        } else {
                            result = result / arrNumber.get(i + 1);
                        }
                        break;
                    case DIVIDE_BALANCE:
                        if (i == 0) {
                            result = arrNumber.get(i) % arrNumber.get(i + 1);
                        } else {
                            result = result % arrNumber.get(i + 1);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return ( df.format(result)+ "" );
    }

}
