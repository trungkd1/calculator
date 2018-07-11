package galapagos.calculatorx1;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorView extends LinearLayout{

    private TextView mtxtResult;
    private TextView mtxtFomula;

    private Set<String> numbers;
    private Set<String> operators;

    public CalculatorView(Context context) {
        super(context);
        init();
    }

    public CalculatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初期の値を設定する　
     */
    private void init() {
        inflate(getContext(), R.layout.calculator_view, this);
        mtxtResult = (TextView)findViewById(R.id.result);
        mtxtFomula = (TextView)findViewById(R.id.formula);
        initNumbers();
    }

    public void onClickButton(View view) {
        Button clicked = (Button) view;
        String value = clicked.getText().toString();

        if (isNumerical(value) || isOperator(value)) {
            //数値場合は、
            if (!isDefaultResult(mtxtFomula.getText().toString())) {
                value = mtxtFomula.getText().toString() + value;
            }
            mtxtFomula.setText(value);
            ArrayList<Double> arrNumber = Utils.addNumber(value);
            if( arrNumber.size() >= 2 ) {
                mtxtResult.setText(Utils.compute(getContext(), mtxtFomula.getText().toString()));
            }
        } else if (view.getId() == R.id.btn_clear) {
            value = Utils.clearString(mtxtFomula.getText().toString() );
            mtxtFomula.setText(value);
            ArrayList<Double> arrNumber = Utils.addNumber(value);
            ArrayList<String> arrOperation = Utils.addOperation(value);
            if( arrNumber.size() >= 2 ) {
                mtxtResult.setText(Utils.compute(getContext(), mtxtFomula.getText().toString()));
            } else if ( arrNumber.size() < 2 && arrOperation.size() == 0) {
                mtxtResult.setText( mtxtFomula.getText().toString() );
            }

        } else if (view.getId() == R.id.btn_equals) {
            String result = mtxtResult.getText().toString();
            String str = result.replace(",", "");
            mtxtFomula.setText(str);
            mtxtResult.setText(null);

        }  else if (view.getId() == R.id.btn_root) {
            value = getContext().getString(R.string.result_default);
            mtxtFomula.setText(value);
            mtxtResult.setText(null);

        }
    }

    /**
     * 「１－９」という比較の値を保存する
     */
    private void initNumbers() {
        numbers = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            numbers.add(Integer.toString(i));
        }
    }

    /**
     * 算定値を保存する
     */
    private void initOperators() {
        operators = new HashSet<String>();
        String[] ops = { "+", "-", "*", "/","%","."};
        for (String operator : ops) {
            operators.add(operator);
        }
    }

    /**
     * 入力する値をチェックするのは、数値か文字か
     *
     * @param value  値を入力する
     *
     * @return true if value is numerical
     */
    private boolean isNumerical(String value) {
        if (numbers == null) {
            initNumbers();
        }
        return numbers.contains(value);
    }

    /**
     * オペレーターを使うことをチェックする.
     *
     * @param value
     *            button value
     * @return true if value is operator
     */
    private boolean isOperator(String value) {
        if (operators == null) {
            initOperators();
        }
        return operators.contains(value);
    }

    /**
     * デフォルトをチェックする
     *
     * @param value
     *            result value
     * @return true if result is default
     */
    private boolean isDefaultResult(String value) {
        return value.equals(getContext().getString(R.string.result_default));
    }

}
