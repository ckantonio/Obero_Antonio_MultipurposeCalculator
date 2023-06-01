package com.obero_antonio_multi_purposecalculator;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewResult;
    private StringBuilder expressionBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textView_result);
        expressionBuilder = new StringBuilder();

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        int[] buttonIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
                R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9,
                R.id.button_decimal, R.id.button_add, R.id.button_subtract, R.id.button_multiply,
                R.id.button_divide, R.id.button_open_parenthesis, R.id.button_close_parenthesis,
                R.id.button_square_root, R.id.button_sin, R.id.button_cos, R.id.button_tan,
                R.id.button_percentage
        };

        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(this);
        }

        Button buttonEqual = findViewById(R.id.button_equal);
        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = evaluateExpression(expressionBuilder.toString());
                textViewResult.setText(result);
                expressionBuilder.setLength(0);
            }
        });

        Button buttonClear = findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionBuilder.setLength(0);
                textViewResult.setText("0");
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        expressionBuilder.append(buttonText);
        textViewResult.setText(expressionBuilder.toString());
    }

    private String evaluateExpression(String expression) {
        // Replace this with your desired calculation logic
        // For simplicity, this implementation uses basic string manipulation

        // Replace square root symbol (√) with "sqrt"
        expression = expression.replaceAll("√", "sqrt");

        try {
            // Evaluate the expression
            double result = evaluate(expression);
            return Double.toString(result);
        } catch (Exception e) {
            return "Error";
        }
    }

    private double evaluate(String expression) {
        // Parse and evaluate the expression using basic string manipulation
        // This implementation supports basic arithmetic operations and the "sqrt" function

        // Remove any whitespace from the expression
        expression = expression.replaceAll("\\s+", "");

        // Evaluate square roots using the "sqrt" function
        while (expression.contains("sqrt")) {
            int sqrtStartIndex = expression.indexOf("sqrt");
            int sqrtEndIndex = findMatchingClosingParenthesis(expression, sqrtStartIndex + 4);
            if (sqrtEndIndex == -1) {
                throw new IllegalArgumentException("Invalid expression: Unmatched parentheses");
            }

            String sqrtExpression = expression.substring(sqrtStartIndex + 4, sqrtEndIndex);
            double sqrtResult = Math.sqrt(evaluate(sqrtExpression));
            expression = expression.substring(0, sqrtStartIndex) + sqrtResult + expression.substring(sqrtEndIndex + 1);
        }

        // Evaluate the expression using basic arithmetic operations
        double result = 0;
        char operator = '+';
        int i = 0;

        while (i < expression.length()) {
            char currentChar = expression.charAt(i);
            if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                operator = currentChar;
                i++;
            } else {
                StringBuilder operandBuilder = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    operandBuilder.append(expression.charAt(i));
                    i++;
                }
                double operand = Double.parseDouble(operandBuilder.toString());
                result = performOperation(result, operator, operand);
            }
        }

        return result;
    }

    private double performOperation(double operand1, char operator, double operand2) {
        // Perform the arithmetic operation
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private int findMatchingClosingParenthesis(String expression, int openingParenthesisIndex) {
        // Find the index of the matching closing parenthesis for the given opening parenthesis
        int count = 1;
        int index = openingParenthesisIndex + 1;
        while (index < expression.length()) {
            char c = expression.charAt(index);
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            }

            if (count == 0) {
                return index;
            }

            index++;
        }
        return -1;  // Unmatched parentheses
    }
}
