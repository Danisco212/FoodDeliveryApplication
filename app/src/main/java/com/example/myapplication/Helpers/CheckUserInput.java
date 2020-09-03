package com.example.myapplication.Helpers;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

public class CheckUserInput {

    public boolean correctInput(EditText editText){
        return !editText.getText().toString().equals("");
    }

    public boolean correctInputs(EditText editText, EditText editText1){
        if (correctInput(editText) && correctInput(editText1)){
            return true;
        }else{
            return false;
        }
    }

    public boolean correctInputs(EditText editText, EditText editText1, EditText editText2){
        return (correctInputs(editText, editText1) && correctInput(editText2));
    }

    public boolean validEmail(EditText email){
        return Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();
    }

    public boolean passwordLength(EditText password){
        return password.getText().toString().trim().length()>=6;
    }
}
