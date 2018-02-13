package com.backtory.android.sdksample;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryObject;
import com.backtory.java.internal.BacktoryQuery;
import com.backtory.java.internal.BacktoryResponse;

import java.util.List;

/**
 * Created by mohammad on 5/2/17.
 *
 */
public class DatabaseFragment extends MainActivity.AbsFragment {

    BacktoryObject currentNote;

    private void saveNote() {
        EditText noteTitleEditText = (EditText) getView().findViewById(R.id.edit_text_note_title);
        EditText notePriorityEditText = (EditText) getView().findViewById(R.id.edit_text_note_priority);
        Switch notePinnedSwitch = (Switch) getView().findViewById(R.id.switch_note_pinned);

        if (currentNote == null) {

            currentNote = new BacktoryObject("Note");
            currentNote.put ("title", noteTitleEditText.getText().toString());
            currentNote.put ("priority", Integer.parseInt(notePriorityEditText.getText().toString()));
            currentNote.put ("pinned", notePinnedSwitch.isChecked());

            currentNote.saveInBackground(new BacktoryCallBack<Void>() {
                @Override
                public void onResponse(BacktoryResponse<Void> response) {
                    if (response.isSuccessful())
                        textView.setText("Save was successful.\n" + currentNote.toString());
                    else
                        textView.setText(response.message());
                }
            });
        } else {
            if (!noteTitleEditText.getText().toString().equals(""))
                currentNote.put("title", noteTitleEditText.getText().toString());
            if (!notePriorityEditText.getText().toString().equals(""))
                currentNote.put("priority", Integer.parseInt(notePriorityEditText.getText().toString()));
            currentNote.put("pinned", notePinnedSwitch.isChecked());

            currentNote.saveInBackground(new BacktoryCallBack<Void>() {
                @Override
                public void onResponse(BacktoryResponse<Void> response) {
                    if (response.isSuccessful())
                        textView.setText("Update was successful.\n" + currentNote.toString());
                    else
                        textView.setText(response.message());
                }
            });
        }
    }

    private void deleteNote() {
        if (currentNote == null) {
            textView.setText("No note (BacktoryObject) is available!");
            return;
        }
        currentNote.deleteInBackground(this.<Void>printCallBack());
    }

    private void findAllPinnedNotes() {
        BacktoryQuery pinnedQuery = new BacktoryQuery("Note");
        pinnedQuery.whereEqualTo("pinned", true);
        pinnedQuery.findInBackground(printBacktoryObjectListCallback());
    }

    private void findTodoTitleNotes() {
        BacktoryQuery pinnedQuery = new BacktoryQuery("Note");
        pinnedQuery.whereContains("title", "todo");
        pinnedQuery.findInBackground(printBacktoryObjectListCallback());
    }

    @NonNull
    private BacktoryCallBack<List<BacktoryObject>> printBacktoryObjectListCallback() {
        return new BacktoryCallBack<List<BacktoryObject>>() {
            @Override
            public void onResponse(BacktoryResponse<List<BacktoryObject>> response) {
                if (response.isSuccessful()) {
                    List<BacktoryObject> result = response.body();
                    String ans = "";
                    for (BacktoryObject bo : result) {
                        ans += bo.toString() + "\n";
                    }
                    textView.setText(ans);
                } else {
                    textView.setText("failed; " + response.message());
                }
            }
        };
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.button_save_note, R.id.button_delete_note,
                            R.id.button_all_pinned_notes, R.id.button_todo_title_notes};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_database;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_note:
                saveNote();
                break;
            case R.id.button_delete_note:
                deleteNote();
                break;
            case R.id.button_all_pinned_notes:
                findAllPinnedNotes();
                break;
            case R.id.button_todo_title_notes:
                findTodoTitleNotes();
                break;
        }
    }
}
