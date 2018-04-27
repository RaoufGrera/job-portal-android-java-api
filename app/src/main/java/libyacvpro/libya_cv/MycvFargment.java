package libyacvpro.libya_cv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.view.View.OnClickListener;


import static android.content.Context.MODE_PRIVATE;


public class MycvFargment extends Fragment {

    TokenManager tokenManager;


    public final String TAG="MycvFargment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mycv_fargment, container, false);
        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
           // finish();
        }


        CardView b = (CardView) rootView.findViewById(R.id.btn_movepage);
        CardView btnEdu = (CardView) rootView.findViewById(R.id.btn_Education);
        CardView btn_Exp = (CardView) rootView.findViewById(R.id.btn_Exp);
        CardView btn_lang = (CardView) rootView.findViewById(R.id.btn_lang);
        CardView btnHobby = (CardView) rootView.findViewById(R.id.btnHobby);
        CardView btnTrain = (CardView) rootView.findViewById(R.id.btnTrain);
        CardView btnInfo = (CardView) rootView.findViewById(R.id.btnInfo);
        CardView btnSpec = (CardView) rootView.findViewById(R.id.btnSpec);
        CardView btnCert = (CardView) rootView.findViewById(R.id.btnCert);
        CardView btnPDF = (CardView) rootView.findViewById(R.id.btnPDF);
        CardView btnRefresh = (CardView) rootView.findViewById(R.id.btnRefresh);
        CardView btnSkills = (CardView) rootView.findViewById(R.id.btnSkills);
        btnSkills.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), MapsActivity.class));}});
        btnPDF.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), SeekerPDFActivity.class));}});
        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), RefreshActivity.class));}});
        btnCert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), CertificateActivity.class));}});
        btnSpec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), SpecialtyActivity.class));}});
        btnInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), InfoActivity.class));}});
        btnTrain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), TrainingActivity.class));}});
        btnHobby.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), HobbyActivity.class));}});
        btn_lang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), LanguageActivity.class));}});
        btn_Exp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), ExperienceActivity.class));}});
        btnEdu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), EducationActivity.class));}});
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), SeekerActivity.class));}});

        return rootView;
    }
    public void onResume() {
        super.onResume();
        getActivity().setTitle("السيرة الذاتية");
    }
}
