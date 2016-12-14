package com.idevelopers.soso;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class InfoFragment extends Fragment implements View.OnClickListener {
    private TextView tvMail;
    private ImageView ivClose;
    private TextView tvClose;
    private TextView tvLink;
    private TextView tvText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_info, container, false);


        initView(view);
        return view;
    }


    private void initView(View view) {

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "customfont.otf");


        tvMail = (TextView) view.findViewById(R.id.tvMail);
        tvMail.setOnClickListener(this);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
        tvClose = (TextView) view.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(this);
        tvClose.setTypeface(tf);
        tvLink = (TextView) view.findViewById(R.id.tvLink);
        tvLink.setOnClickListener(this);
        tvLink.setTypeface(tf);
        tvText = (TextView) view.findViewById(R.id.tvText);
        tvText.setTypeface(tf);
        tvClose.setTypeface(tf);
    }

    public void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
        (getActivity().findViewById(R.id.btnSos)).setVisibility(View.VISIBLE);
        (getActivity().findViewById(R.id.etInput)).setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMail:
                sendMail();
                break;
            case R.id.ivClose:
                closeFragment();
                break;
            case R.id.tvClose:
                closeFragment();
                break;
            case R.id.tvLink:
                evaluation();
                break;

        }
    }

    private void evaluation() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.idevelopers.soso"));
        startActivity(i);

    }

    private void sendMail() {
        //===========================
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"soso@soso.ge"});

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
        }
//===========================
    }
}
