package com.example.android.DesignPatternsDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.example.android.R;

import java.util.HashMap;
import java.util.Map;
//解释器模式 https://www.jianshu.com/p/9bfd8493f32c
public class InterpreterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpreter);
        PlayContext context = new PlayContext();
        context.setText("T 500 O 2 E 0.5 G 0.5 A 3 E 0.5 G 0.5 D 3 E 0.5 G 0.5 A 0.5 O 3 C 1 O 2 A 0.5 G 1 C 0.5 E 0.5 D 3 ");
        Expression expression = null;
        try {
            while (context.getText().length() > 0) {
                String string = context.getText().substring(0, 1);
                switch (string) {
                    case "O":
                        expression = new Scale();// 为O时,实例化音阶
                        break;
                    case "T":
                        expression = new Speed();
                        break;
                    case "C":
                    case "D":
                    case "E":
                    case "F":
                    case "G":
                    case "A":
                    case "B":
                    case "P":
                        expression = new Note();// 实例化音符
                        break;
                    default:
                        break;
                }
                expression.Interpret(context);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}



/**
 * 表达式类(AbstractExpression)
 */
 abstract class Expression {

    public void Interpret(PlayContext context) {
        if (context.getText().length() == 0) {
            return;
        } else {
            String playKey = context.getText().substring(0, 1);
            context.setText(context.getText().substring(2));
            double playValue = Double.valueOf(context.getText().substring(0, 1).trim());
            context.setText(context.getText().substring(context.getText().indexOf(" ") + 1));
            Excute(playKey, playValue);
        }

    }

    public abstract void Excute(String key, double value);
}

 class Note extends Expression {
    @Override
    public void Excute(String key, double value) {
        String note = "";
        switch (key){
            case "C":
                note = "1";
                break;
            case "D":
                note = "2";
                break;
            case "E":
                note = "3";
                break;
            case "F":
                note = "4";
                break;
            case "G":
                note = "5";
                break;
            case "A":
                note = "6";
                break;
            case "B":
                note = "7";
                break;

        }

        System.out.print(note+" ");
    }
}

 class Scale extends Expression {
    @Override
    public void Excute(String key, double value) {
        String scale = "";
        switch ((int) value){
            case 1:
                scale = "低音";
                break;
            case 2:
                scale = "中音";
                break;
            case 3:
                scale = "高音";
                break;

        }

        System.out.print(scale+" ");
    }
}

class Speed extends Expression{

    @Override
    public void Excute(String key, double value) {
        String speed;
        if(value<500){
            speed="快速";
        }else if(value>=1000){
            speed="慢速";
        }else{
            speed="中速";
        }
        System.out.print(speed+" ");
    }

}

 class PlayContext {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}