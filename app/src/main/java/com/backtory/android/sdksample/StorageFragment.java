package com.backtory.android.sdksample;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.backtory.androidsdk.internal.BacktoryCallBack;
import com.backtory.androidsdk.internal.BacktoryFile;
import com.backtory.androidsdk.internal.BulkOperation;
import com.backtory.androidsdk.internal.Request;
import com.backtory.androidsdk.model.BacktoryResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.backtory.android.sdksample.FileUtil.appendRenamedToFileName;
import static com.backtory.android.sdksample.FileUtil.getFileName;

/**
 * @author Alireza Farahani
 */
public class StorageFragment extends MainActivity.AbsFragment {
  private EditText pathEditText, filAddressEditText;
  private Switch replaceExistingSwitch;
  private List<String> uploadedFiles;
  private static final int READ_REQUEST_CODE = 46; // or any int you like :)

  @Override
  protected int[] getButtonsId() {
    return new int[]{
        R.id.button_delete, R.id.button_rename,
        R.id.button_upload_single, R.id.button_upload_multiple};
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    pathEditText = (EditText) getView().findViewById(R.id.edit_text_path);
    filAddressEditText = (EditText) getView().findViewById(R.id.edit_text_file_address);
    replaceExistingSwitch = (Switch) getView().findViewById(R.id.switch_upload_replacing);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_storage;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.button_upload_single:
        pickFileFromDevice(false);
        break;
      case R.id.button_upload_multiple:
        pickFileFromDevice(true);
        break;
      case R.id.button_delete:
        deleteFiles();
        break;
      case R.id.button_rename:
        renameFiles();
        break;
    }
  }

  public void uploadMultiple(List<String> addresses) {
    List<Request.UploadRequest> uploadRequests = new ArrayList<>();
    String path = pathEditText.getText().toString().trim();
    for (String address : addresses)
      uploadRequests.add(new BacktoryFile().beginUpload(
          FileUtil.createFileFromContentUri(getActivity(), address),
          path.isEmpty() ? "/" : path,
          replaceExistingSwitch.isChecked()
      ));

    new BulkOperation.BulkUpload(uploadRequests).commitInBackground(bulkUploadCallBack());
  }

  public void uploadSingle(String address) {
    String path = pathEditText.getText().toString().trim();
    new BacktoryFile().beginUpload(
        FileUtil.createFileFromContentUri(getActivity(), address),
        path.isEmpty() ? "/" : path,
        replaceExistingSwitch.isChecked()
    ).commitInBackground(singleUploadCallBack());
  }

  @NonNull
  private BacktoryCallBack<List<String>> bulkUploadCallBack() {
    return new BacktoryCallBack<List<String>>() {
      @Override
      public void onResponse(BacktoryResponse<List<String>> response) {
        if (response.isSuccessful()){
          uploadedFiles = response.body();
        }
        StorageFragment.this.<List<String>>printCallBack().onResponse(response);
      }
    };
  }

  @NonNull
  private BacktoryCallBack<String> singleUploadCallBack() {
    return new BacktoryCallBack<String>() {
      @Override
      public void onResponse(BacktoryResponse<String> response) {
        if (response.isSuccessful()){
          uploadedFiles = Collections.singletonList(response.body());
        }
        StorageFragment.this.<String>printCallBack().onResponse(response);
      }
    };
  }

  void deleteFiles() {
    String resourceToDelete = filAddressEditText.getText().toString().trim();

    if (!resourceToDelete.isEmpty())
      new BacktoryFile().beginDelete(resourceToDelete, true).commitInBackground(this.<Void>printCallBack());

    else {
      List<Request.DeleteRequest> deleteRequests = new ArrayList<>(uploadedFiles.size());
      for (String savedUrl : uploadedFiles)
        deleteRequests.add(new BacktoryFile().beginDelete(savedUrl, true));

      new BulkOperation.BulkDelete(deleteRequests).commitInBackground(this.<Void>printCallBack());
    }
  }

  void renameFiles() {
    String resourceToDelete = filAddressEditText.getText().toString().trim();

    if (!resourceToDelete.isEmpty())
      new BacktoryFile().beginRename(resourceToDelete, appendRenamedToFileName(getFileName(resourceToDelete))).
          commitInBackground(this.<String>printCallBack());

    else {
      List<Request.RenameRequest> renameRequests = new ArrayList<>(uploadedFiles.size());
      for (String savedUrl : uploadedFiles)
        renameRequests.add(new BacktoryFile().beginRename(savedUrl, appendRenamedToFileName(getFileName(savedUrl))));

      new BulkOperation.BulkRename(renameRequests).commitInBackground(this.<List<String>>printCallBack());
    }
  }



  private void pickFileFromDevice(boolean multipleSelect) {
    // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
    // browser.
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

    // Filter to only show results that can be "opened", such as a
    // file (as opposed to a list of contacts or timezones)
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    // Filter to show only images, using the image MIME data type.
    // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
    // To search for all documents available via installed storage providers,
    // it would be "*/*".
    intent.setType("*/*");

    if (multipleSelect)
      intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // The ACTION_OPEN_DOCUMENT intent was sent with the request code
    // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
    // response to some other intent, and the code below shouldn't run at all.

    if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
      List<String> addresses = new ArrayList<>();
      if (data.getClipData() != null) {
        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
          ClipData.Item item = data.getClipData().getItemAt(i);
          Uri uri = item.getUri();
          addresses.add(uri.toString());
        }
        uploadMultiple(addresses);
      } else { // only one file selected http://stackoverflow.com/questions/21071098/select-multiple-images-from-android-how-to-get-uris#comment62968158_26336150
        uploadSingle(data.getData().toString());
      }
    }
  }
}
