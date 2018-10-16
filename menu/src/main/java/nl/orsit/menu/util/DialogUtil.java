package nl.orsit.menu.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import nl.orsit.base.ScrollingTableItem;
import nl.orsit.menu.R;

public abstract class DialogUtil {

    abstract public Resources getResources();

    abstract public Context getContext();

    abstract public void action();

    public void addRows(TableLayout ll, View label, View view) {
        addRows(ll, label, view, true);
    }

    public Button createCleanButton(String text) {
        Button button = new Button(getContext());
        button.setBackgroundResource(R.drawable.rounded_button_shape);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setText(text);
        return button;
    }

    public void addText(ScrollingTableItem item, boolean viewOnly, TableLayout tableLayout) {
        addInput(item, viewOnly, tableLayout, InputType.TYPE_CLASS_TEXT);
    }
    public void addNumber(ScrollingTableItem item, boolean viewOnly, TableLayout tableLayout) {
        addInput(item, viewOnly, tableLayout, InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }
    public void addMultiText(ScrollingTableItem item, boolean viewOnly, TableLayout tableLayout) {
        View labelView = createLabel(item.getLabel());
        if (item.getValue() != null) {
            View view = createTextView(item.getValue());
            view.setMinimumHeight(200);
            addRows(tableLayout, labelView, view, false);
        } else {
            if (!viewOnly) {
                View inputView = createInputView(item.getId(), InputType.TYPE_CLASS_TEXT, item.getValue());
                inputView.setMinimumHeight(200);
                addRows(tableLayout, labelView, inputView, false);
            }
        }

    }

    public void prepareHeader(TableLayout logHeader) {
        ViewGroup.LayoutParams hlp = logHeader.getLayoutParams();
        hlp.height = 100;
        logHeader.setLayoutParams(hlp);


    }

    public void prepareFooter(String buttonText, TableLayout footer, final AlertDialog alertDialog, boolean viewOnly) {
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



    public void resizeDialog(Dialog dialog, View centerView, View headerView, View footerView) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        // dialog
        dialog.getWindow().setLayout(width, height);
        width -= 30;

        // scrollview
        ViewGroup.LayoutParams lpScrollView = centerView.getLayoutParams();

        // logheader
        ViewGroup.LayoutParams lpHeader = headerView.getLayoutParams();
        lpHeader.width = width;
        headerView.setLayoutParams(lpHeader);

        // logfooter
        ViewGroup.LayoutParams lpFooter = footerView.getLayoutParams();
        lpFooter.width = width;
        footerView.setLayoutParams(lpFooter);

        lpScrollView.width = width;
        lpScrollView.height = height - (lpHeader.height + lpFooter.height);
        centerView.setLayoutParams(lpScrollView);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);


    }

    public void resizeHeaderDialog(Dialog dialog, View header) {
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

    public void resizeEmptyDialog(Dialog dialog, View view) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);
//        width -= 30;
//        height -= 30;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public void resizeFooterDialog(Dialog dialog, View view, Button button) {
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

    public TextView createLabel(String text) {
        TextView label = new TextView(getContext());
        label.setText(text);
        return label;
    }

    /** PRIVATE METHODS */
    private void addRows(TableLayout ll, View label, View view, boolean singleRow) {
        TableRow row = new TableRow(getContext());
        ll.addView(prepareRow(row));
        if (singleRow) {
            row.addView(prepareCell(label, 1));
            row.addView(prepareCell(view, 1));
        } else {
            row.addView(prepareCell(label, 2));
            TableRow secondRow = new TableRow(getContext());
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




    private void addInput(ScrollingTableItem item, boolean viewOnly, TableLayout tableLayout, int type) {
        View label = createLabel(item.getLabel());
        if (item.getValue() != null) {
            View view = createTextView(item.getValue());
            addRows(tableLayout, label, view);
        } else {
            if (!viewOnly) {
                View view = createInputView(item.getId(), InputType.TYPE_CLASS_TEXT, item.getValue());
                addRows(tableLayout, label, view);
            }
        }

    }

}
