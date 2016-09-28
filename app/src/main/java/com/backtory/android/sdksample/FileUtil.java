package com.backtory.android.sdksample;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * @author Alireza Farahani
 */

class FileUtil {
  private static final String DISK_CACHE_SUBDIR = "tempFiles";
  private static final String RENAMED = "_renamed";

  static File createFileFromContentUri(Context context, String fileAbsoluteAddress) {
    Uri contentUri = Uri.parse(fileAbsoluteAddress);
    try {
      File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
      if (!cacheDir.exists()){
        if (!cacheDir.mkdirs()) {
          throw new IllegalStateException("failed to create new directory in cached dir '" + cacheDir.getPath() + "'");
        }
      }
      String fileName = getFileName(context.getContentResolver(), contentUri);
      File file = new File(cacheDir/*context.getCacheDir()*/, fileName);
      InputStream ins = getDocumentInputStream(context.getContentResolver(), contentUri);
      createFileFromInputStream(ins, file);
      return file;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  // from android developers https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
  // Creates a unique subdirectory of the designated app cache directory. Tries to use external
  // but if not mounted, falls back on internal storage.
  private static File getDiskCacheDir(Context context, String uniqueName) {
    // Check if media is mounted or storage is built-in, if so, try and use external cache dir
    // otherwise use internal cache dir
    final String cachePath =
        Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
            !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
            context.getCacheDir().getPath();

    return new File(cachePath + File.separator + uniqueName);
  }

  static void createFileFromInputStream(InputStream ins, File file) throws IOException {
    FileOutputStream output = new FileOutputStream(file);
    int bufferSize = 1024;
    byte[] buffer = new byte[bufferSize];
    int len = 0;
    while ((len = ins.read(buffer)) != -1) {
      output.write(buffer, 0, len);
    }
  }

  private static InputStream getDocumentInputStream(ContentResolver contentResolver, Uri contentUri) throws FileNotFoundException {
    InputStream ins;
    ins = contentResolver.openInputStream(contentUri);
    return ins;
  }

  /**
   * Gets the extension of a file name, like ".png" or ".jpg".
   *
   * @param uri
   * @return Extension including the dot("."); "" if there is no extension;
   * null if uri was null.
   */
  public static String getExtension(String uri) {
    if (uri == null) {
      return null;
    }

    int dot = uri.lastIndexOf(".");
    if (dot >= 0) {
      return uri.substring(dot);
    } else {
      // No extension.
      return "";
    }
  }

  /**
   * e.g. sample.txt
   * @param filePath
   * @return
   */
  static String getFileName(String filePath){
    int separator = filePath.lastIndexOf("/");
    if (separator == -1)
      return filePath;
    return filePath.substring(separator + 1);
  }

  static String appendRenamedToFileName(String originalName){
    int dot = originalName.lastIndexOf('.');
    if (dot == -1)
      return originalName + RENAMED;
    return originalName.substring(0, dot) + RENAMED + originalName.substring(dot);
  }

  static String getFileName(ContentResolver contentResolver, Uri uri) {
    String result = null;
    if (uri.getScheme().equals("content")) {
      Cursor cursor = contentResolver.query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        cursor.close();
      }
    }
    if (result == null) {
      result = uri.getPath();
      int cut = result.lastIndexOf('/');
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }
    return result;
  }
}
