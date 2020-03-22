package com.bantoo.babooo.Controller.SignUpActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bantoo.babooo.R;

public class ApprCodePage extends Fragment {

    ImageView infoBtn;
    EditText apprCodeET;
    String apprCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_apprcode,container,false);

        infoBtn = rootView.findViewById(R.id.infoBtn);
        apprCodeET = rootView.findViewById(R.id.apprCodeET);

        handleButton();

        apprCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                apprCode = apprCodeET.getText().toString();

                if (apprCode.isEmpty()){
                    apprCodeET.setError("Harus diisi");
                }
                else if (apprCode.length() < 4){
                    apprCodeET.setError("Periksa kembali approval code anda");
                }
                else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("apprCode", apprCode).commit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        return rootView;
    }

    public void handleButton(){
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertInfo();
            }
        });
    }

    public void showAlertInfo(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Approval Code");
        alertDialogBuilder
                .setMessage("Approval code didapat saat anda melakukan proses registrasi dikantor Bantoo," +
                        " anda perlu memberikan beberapa dokumen pendukung untuk melengkapi proses" +
                        " registrasi")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
