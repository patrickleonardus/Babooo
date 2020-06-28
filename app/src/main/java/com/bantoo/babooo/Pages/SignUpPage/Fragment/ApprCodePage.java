package com.bantoo.babooo.Pages.SignUpPage.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApprCodePage extends Fragment {

    ImageView infoBTN;
    EditText codeET1, codeET2, codeET3, codeET4, codeET5, codeET6;
    private String codeTemp1, codeTemp2, codeTemp3, codeTemp4, codeTemp5, codeTemp6;
    private static String apprCode;
    private static boolean correct = false;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dailyMaidReference, monthlyMaidReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_apprcode, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dailyMaidReference = firebaseDatabase.getReference().child("ART");
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        infoBTN = rootView.findViewById(R.id.infoBtn);
        codeET1 = rootView.findViewById(R.id.code1ET);
        codeET2 = rootView.findViewById(R.id.code2ET);
        codeET3 = rootView.findViewById(R.id.code3ET);
        codeET4 = rootView.findViewById(R.id.code4ET);
        codeET5 = rootView.findViewById(R.id.code5ET);
        codeET6 = rootView.findViewById(R.id.code6ET);

        initVar();
        handleButton();
        handleTextbox();

        return rootView;
    }

    public void initVar() {
        codeTemp1 = "";
        codeTemp2 = "";
        codeTemp3 = "";
        codeTemp4 = "";
        codeTemp5 = "";
        codeTemp6 = "";
        correct = false;
    }

    public void handleButton() {
        infoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertInfo();
            }
        });
    }

    public void handleTextbox() {

        codeET1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (codeET1.getText().toString().isEmpty()){
                    codeET1.setError("Harus diisi");
                    editor.putString("apprCode", "N/A").commit();
                    correct = false;
                }
                else {
                    codeTemp1 = codeET1.getText().toString();
                    apprCode = codeTemp1 + codeTemp2 + codeTemp3 + codeTemp4 + codeTemp5 + codeTemp6;
                    codeET2.requestFocus();

                    if(codeTemp1.equals("")||codeTemp2.equals("")||codeTemp3.equals("")||codeTemp4.equals("")||codeTemp5.equals("")||codeTemp6.equals("")){
                        editor.putString("apprCode", "N/A").commit();
                    }
                    else {
                        checkDailyApprovalCode();
                        editor.putString("apprCode", apprCode).commit();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        codeET2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (codeET2.getText().toString().isEmpty()){
                    codeET2.setError("Harus diisi");
                    editor.putString("apprCode", "N/A").commit();
                    correct = false;
                }
                else {
                    codeTemp2 = codeET2.getText().toString();
                    apprCode = codeTemp1 + codeTemp2 + codeTemp3 + codeTemp4 + codeTemp5 + codeTemp6;
                    codeET3.requestFocus();

                    if(codeTemp1.equals("")||codeTemp2.equals("")||codeTemp3.equals("")||codeTemp4.equals("")||codeTemp5.equals("")||codeTemp6.equals("")){
                        editor.putString("apprCode", "N/A").commit();
                    }
                    else {
                        checkDailyApprovalCode();
                        editor.putString("apprCode", apprCode).commit();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        codeET3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (codeET3.getText().toString().isEmpty()){
                    codeET3.setError("Harus diisi");
                    editor.putString("apprCode", "N/A").commit();
                    correct = false;
                }
                else {
                    codeTemp3 = codeET3.getText().toString();
                    apprCode = codeTemp1 + codeTemp2 + codeTemp3 + codeTemp4 + codeTemp5 + codeTemp6;
                    codeET4.requestFocus();

                    if(codeTemp1.equals("")||codeTemp2.equals("")||codeTemp3.equals("")||codeTemp4.equals("")||codeTemp5.equals("")||codeTemp6.equals("")){
                        editor.putString("apprCode", "N/A").commit();
                    }
                    else {
                        checkDailyApprovalCode();
                        editor.putString("apprCode", apprCode).commit();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        codeET4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (codeET4.getText().toString().isEmpty()){
                    codeET4.setError("Harus diisi");
                    editor.putString("apprCode", "N/A").commit();
                    correct = false;
                }
                else {
                    codeTemp4 = codeET4.getText().toString();
                    apprCode = codeTemp1 + codeTemp2 + codeTemp3 + codeTemp4 + codeTemp5 + codeTemp6;
                    codeET5.requestFocus();

                    if(codeTemp1.equals("")||codeTemp2.equals("")||codeTemp3.equals("")||codeTemp4.equals("")||codeTemp5.equals("")||codeTemp6.equals("")){
                        editor.putString("apprCode", "N/A").commit();
                    }
                    else {
                        checkDailyApprovalCode();
                        editor.putString("apprCode", apprCode).commit();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        codeET5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (codeET5.getText().toString().isEmpty()){
                    codeET5.setError("Harus diisi");
                    editor.putString("apprCode", "N/A").commit();
                    correct = false;
                }
                else {
                    codeTemp5 = codeET5.getText().toString();
                    apprCode = codeTemp1 + codeTemp2 + codeTemp3 + codeTemp4 + codeTemp5 + codeTemp6;
                    codeET6.requestFocus();

                    if(codeTemp1.equals("")||codeTemp2.equals("")||codeTemp3.equals("")||codeTemp4.equals("")||codeTemp5.equals("")||codeTemp6.equals("")){
                        editor.putString("apprCode", "N/A").commit();
                    }
                    else {
                        checkDailyApprovalCode();
                        editor.putString("apprCode", apprCode).commit();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        codeET6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (codeET6.getText().toString().isEmpty()){
                    codeET6.setError("Harus diisi");
                    editor.putString("apprCode", "N/A").commit();
                    correct = false;
                }
                else {
                    codeTemp6 = codeET6.getText().toString();
                    apprCode = codeTemp1 + codeTemp2 + codeTemp3 + codeTemp4 + codeTemp5 + codeTemp6;

                    if(codeTemp1.equals("")||codeTemp2.equals("")||codeTemp3.equals("")||codeTemp4.equals("")||codeTemp5.equals("")||codeTemp6.equals("")){
                        editor.putString("apprCode", "N/A").commit();
                    }
                    else {
                        checkDailyApprovalCode();
                        editor.putString("apprCode", apprCode).commit();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    public boolean checkApprovalCode() {
        return correct;
    }

    private void checkDailyApprovalCode() {
        dailyMaidReference.orderByChild("approvalCode").equalTo(apprCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(dataSnapshot.exists()) {
                    correct = true;
                    editor.putString("artType", "daily").commit();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        editor.putString("phone", snapshot.child("phoneNumber").getValue().toString()).commit();
                    }
                } else {
                    checkMonthlyApprovalCode();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkMonthlyApprovalCode() {
        monthlyMaidReference.orderByChild("approvalCode").equalTo(apprCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(dataSnapshot.exists()) {
                    correct = true;
                    editor.putString("artType", "monthly").commit();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        editor.putString("phone", snapshot.child("phoneNumber").getValue().toString()).commit();
                    }
                } else {
                    correct = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showAlertInfo() {
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
