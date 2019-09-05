package com.example.admin.techapt;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.admin.techapt.InsertCLanguageQue;
import com.example.admin.techapt.R;

import java.util.ArrayList;


public class QustionAdapter extends BaseAdapter {

    ArrayList<InsertCLanguageQue> arrayList;
    ArrayList<StoreOption> optionList;
    Context context;
    LayoutInflater layoutInflater;
    TextView question_textview;
    RadioButton opa,opb,opc,opd;
    TextView answer_textview;

    public QustionAdapter(ArrayList<InsertCLanguageQue> arrayList,ArrayList<StoreOption> optionList, Context context) {
        this.arrayList = arrayList;
        this.optionList = optionList;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        try {
            Log.e("Size Arrylist QueAdpt", optionList.size() + "");
            vi = layoutInflater.inflate(R.layout.activity_listview_layout, null);
            question_textview = (TextView) vi.findViewById(R.id.questiontextview);
            answer_textview = (TextView) vi.findViewById(R.id.answertextview);
            opa = (RadioButton) vi.findViewById(R.id.Op_A_radio);
            opb = (RadioButton) vi.findViewById(R.id.Op_B_radio);
            opc = (RadioButton) vi.findViewById(R.id.Op_C_radio);
            opd = (RadioButton) vi.findViewById(R.id.Op_D_radio);
            InsertCLanguageQue insertCLanguageQue = (InsertCLanguageQue) getItem(i);
            StoreOption storeOption = optionList.get(i);
            question_textview.setText("Q." + insertCLanguageQue.getQuestion_no() + " " + insertCLanguageQue.getQuestion());
            answer_textview.setText(answer_textview.getText() + " " + insertCLanguageQue.getAnswer());
            opa.setText(insertCLanguageQue.getA());
            if (opa.getText().equals(storeOption.getOption())) {
                opa.setChecked(true);
                if(opa.getText().equals(insertCLanguageQue.getAnswer()))
                    opa.setTextColor(Color.GREEN);
                else
                    opa.setTextColor(Color.RED);
            }
            opb.setText(insertCLanguageQue.getB());
            if (opb.getText().equals(storeOption.getOption())) {

                opb.setChecked(true);
                if(opb.getText().equals(insertCLanguageQue.getAnswer()))
                    opb.setTextColor(Color.GREEN);
                else
                    opb.setTextColor(Color.RED);
            }
            opc.setText(insertCLanguageQue.getC());
            if (opc.getText().equals(storeOption.getOption())) {
                opc.setChecked(true);
                if(opc.getText().equals(insertCLanguageQue.getAnswer()))
                    opc.setTextColor(Color.GREEN);
                else
                    opc.setTextColor(Color.RED);
            }
            opd.setText(insertCLanguageQue.getD());
            if (opd.getText().equals(storeOption.getOption())) {
                opd.setChecked(true);
                if(opd.getText().equals(insertCLanguageQue.getAnswer()))
                    opd.setTextColor(Color.GREEN);
                else
                    opd.setTextColor(Color.RED);
            }
        }
        catch (Exception e){Log.e("Exception: ",e.getMessage());}
        return vi;
    }
}
