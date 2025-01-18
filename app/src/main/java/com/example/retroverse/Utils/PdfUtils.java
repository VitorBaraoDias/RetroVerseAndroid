package com.example.retroverse.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtils {
    public static void createPdfFromView(View view, String fileName) {
   //converte o layout em bitmap
    Bitmap bitmap = captureViewAsBitmap(view);

    //DIMENSOES
    PdfDocument pdfDocument = new PdfDocument();
    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
    PdfDocument.Page page = pdfDocument.startPage(pageInfo);


    //CRIA PDF COM BASE DO BITMAP
    Canvas canvas = page.getCanvas();
    canvas.drawBitmap(bitmap, 0, 0, null);
    pdfDocument.finishPage(page);

    File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".pdf");
    try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
        pdfDocument.writeTo(fos);
    } catch (IOException e) {
        e.printStackTrace();
    }
    pdfDocument.close();
}

    private static Bitmap captureViewAsBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

}
