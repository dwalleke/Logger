package nl.orsit.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import nl.orsit.logger.R;

abstract public class TabFragment extends SpinnerFragment {

    abstract protected void action();

    protected void addRows(TableLayout ll, Context dialogContext, View label, View view) {
        addRows(ll, dialogContext, label, view, true);
    }

    protected Button createCleanButton(String text) {
        Button button = new Button(getContext());
        button.setBackgroundResource(R.drawable.rounded_button_shape);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setText(text);
        return button;
    }

    protected void addText(ScrollingTableItem item, boolean viewOnly, Context dialogContext, TableLayout tableLayout) {
        addInput(item, viewOnly, dialogContext, tableLayout, InputType.TYPE_CLASS_TEXT);
    }
    protected void addNumber(ScrollingTableItem item, boolean viewOnly, Context dialogContext, TableLayout tableLayout) {
        addInput(item, viewOnly, dialogContext, tableLayout, InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }
    protected void addMultiText(ScrollingTableItem item, boolean viewOnly, Context dialogContext, TableLayout tableLayout) {
        View labelView = createLabel(item.getLabel());
        if (item.getValue() != null) {
            View view = createTextView(item.getValue());
            view.setMinimumHeight(200);
            addRows(tableLayout, dialogContext, labelView, view, false);
        } else {
            if (!viewOnly) {
                View inputView = createInputView(item.getId(), InputType.TYPE_CLASS_TEXT, item.getValue());
                inputView.setMinimumHeight(200);
                addRows(tableLayout, dialogContext, labelView, inputView, false);
            }
        }

    }

    protected void prepareHeader(TableLayout logHeader) {
        ViewGroup.LayoutParams hlp = logHeader.getLayoutParams();
        hlp.height = 100;
        logHeader.setLayoutParams(hlp);


    }

    protected void prepareFooter(String buttonText, TableLayout footer, final AlertDialog alertDialog, boolean viewOnly) {
        TableRow row = new TableRow(getContext());
        if (!viewOnly) {
            final Button toevoegen = createCleanButton(buttonText);
            toevoegen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    action();
                }
            });
            Button annuleren = createCleanButton("annuleren");
            annuleren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            row.addView(toevoegen);
            row.addView(annuleren);
        } else {
            Button annuleren = createCleanButton("sluiten");
            annuleren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            row.addView(annuleren);
        }
        footer.addView(row);

    }



    protected void resizeDialog(Dialog dialog, ScrollView scrollView, View logHeader, View logFooter) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        // dialog
        dialog.getWindow().setLayout(width, height);
        width -= 30;

        // scrollview
        ViewGroup.LayoutParams lpScrollView = scrollView.getLayoutParams();

        // logheader
        ViewGroup.LayoutParams lpHeader = logHeader.getLayoutParams();
        lpHeader.width = width;
        logHeader.setLayoutParams(lpHeader);

        // logfooter
        ViewGroup.LayoutParams lpFooter = logFooter.getLayoutParams();
        lpFooter.width = width;
        logFooter.setLayoutParams(lpFooter);

        lpScrollView.width = width;
        lpScrollView.height = height - (lpHeader.height + lpFooter.height);
        scrollView.setLayoutParams(lpScrollView);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);


    }

    protected void resizeHeaderDialog(Dialog dialog, View header) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        // dialog
        dialog.getWindow().setLayout(width, height);
        width -= 30;

        // logheader
        ViewGroup.LayoutParams lpHeader = header.getLayoutParams();
        lpHeader.width = width;
        header.setLayoutParams(lpHeader);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);


    }

    protected void resizeEmptyDialog(Dialog dialog) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);
    }

    protected void resizeFooterDialog(Dialog dialog, View view, Button button) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);
        width -= 30;
        int buttonHeight = 180;

        ViewGroup.LayoutParams lpTextureView = view.getLayoutParams();
        ViewGroup.LayoutParams lpButtonView = button.getLayoutParams();
        lpTextureView.width = width;
        lpTextureView.height = height - buttonHeight - 40;
        lpButtonView.width = width;
        lpButtonView.height = buttonHeight;

        view.setLayoutParams(lpTextureView);
        button.setLayoutParams(lpButtonView);
        // dialog
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);
    }

    protected TextView createLabel(String text) {
        TextView label = new TextView(getContext());
        label.setText(text);
        return label;
    }

    /** PRIVATE METHODS */
    private void addRows(TableLayout ll, Context dialogContext, View label, View view, boolean singleRow) {
        TableRow row = new TableRow(dialogContext);
        ll.addView(prepareRow(row));
        if (singleRow) {
            row.addView(prepareCell(label, 1));
            row.addView(prepareCell(view, 1));
        } else {
            row.addView(prepareCell(label, 2));
            TableRow secondRow = new TableRow(dialogContext);
            prepareRow(secondRow);
            secondRow.addView(prepareCell(view, 2));
            ll.addView(secondRow);
        }

    }

    private View createTextView(String value) {
        TextView text = new TextView(getContext());
        text.setBackgroundResource(R.drawable.rounded_view_shape);
        text.setText(value);
        return text;
    }

    private View createInputView(String id, int inputType, String value) {
        TextInputEditText input = new TextInputEditText(getContext());
        input.setId(Integer.parseInt(id));
        input.setInputType(inputType);
        if (value != null) {
            input.setText(value);
        }
        input.setBackgroundResource(R.drawable.rounded_edit_shape);
        return input;


    }

    private TableRow prepareRow(TableRow row) {
        row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        row.setPadding(0, 25, 0, 0);
        return row;
    }

    private View prepareCell(View view, int span) {
        view.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
        view.setPadding(25, 5, 25, 5);
        TableRow.LayoutParams lp = (TableRow.LayoutParams) view.getLayoutParams();
        lp.span = span;
        view.setLayoutParams(lp);
        return view;
    }




    private void addInput(ScrollingTableItem item, boolean viewOnly, Context dialogContext, TableLayout tableLayout, int type) {
        View label = createLabel(item.getLabel());
        if (item.getValue() != null) {
            View view = createTextView(item.getValue());
            addRows(tableLayout, dialogContext, label, view);
        } else {
            if (!viewOnly) {
                View view = createInputView(item.getId(), InputType.TYPE_CLASS_TEXT, item.getValue());
                addRows(tableLayout, dialogContext, label, view);
            }
        }

    }



}
