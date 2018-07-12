package galapagos.calculatorx1;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceObject {

    private static final String FOMULA = "fomula";
    private static final String RESULT = "result";

    private static SharePreferenceObject yourPreference;
    private SharedPreferences sharedPreferences;

    public static SharePreferenceObject getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new SharePreferenceObject(context);
        }
        return yourPreference;
    }

    private SharePreferenceObject(Context context) {
        sharedPreferences = context.getSharedPreferences("SavePreference",Context.MODE_PRIVATE);
    }

    public void saveDataFomula(String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(FOMULA, value);
        prefsEditor.commit();
    }

    public String getDataFomula() {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(FOMULA, "0");
        }
        return "";
    }


    public void saveDataResult(String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(RESULT, value);
        prefsEditor.commit();
    }

    public String getDataResult() {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(RESULT, "");
        }
        return "";
    }


}
