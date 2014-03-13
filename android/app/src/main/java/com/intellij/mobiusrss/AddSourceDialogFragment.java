package com.intellij.mobiusrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
* Created by ekudel on 3/13/14.
*/
public class AddSourceDialogFragment extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Activity activity = getActivity();
    @SuppressWarnings("ConstantConditions")
    final EditText input = new EditText(activity);
    input.setInputType(EditorInfo.TYPE_TEXT_VARIATION_URI);

    return new AlertDialog.Builder(activity).setMessage(getString(R.string.input_url))
        .setView(input)
        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            final Editable text = input.getText();

            if (text != null) {
              final String url = text.toString();

              if (!url.isEmpty()) {
                ((ChooseSourceActivity) activity).doAddSource(url);
              }
            }
          }
        })
        .setNegativeButton(getString(R.string.cancel), null)
        .create();
  }
}
