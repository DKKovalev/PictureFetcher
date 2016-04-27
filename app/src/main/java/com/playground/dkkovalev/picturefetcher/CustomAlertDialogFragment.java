package com.playground.dkkovalev.picturefetcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by d.kovalev on 27.04.2016.
 */
public class CustomAlertDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(title);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    public static CustomAlertDialogFragment newInstance(String title) {
        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        customAlertDialogFragment.setArguments(args);
        customAlertDialogFragment.setCancelable(false);
        return customAlertDialogFragment;
    }
}
