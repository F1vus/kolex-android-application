package edu.at.kolex.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import edu.at.kolex.adapter.StopSegmentAdapter;
import edu.at.kolex.model.Ticket;

public final class PdfUtil {

    private PdfUtil() {
    }

    public interface PdfCreateCallback {
        void onSuccess(@NonNull Uri uri);
        void onError(@NonNull String message);
    }

    public static void createAndSaveTicketPdf(
            @NonNull Context context,
            @NonNull Ticket ticket,
            @Nullable List<StopSegmentAdapter.Segment> segments,
            @NonNull PdfCreateCallback callback
    ) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Uri uri = savePdfToDownloads(context, ticket, segments);
                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(uri));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(
                        () -> {
                            callback.onError("Nie udało się utworzyć PDF");
                            Log.e("PdfUtil", "The PDF could not be created", e);
                        }
                );
            }
        });
    }

    private static Uri savePdfToDownloads(
            Context context,
            Ticket ticket,
            @Nullable List<StopSegmentAdapter.Segment> segments
    ) throws IOException {

        String fileName = "ticket_" + ticket.getId() + ".pdf";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
        values.put(MediaStore.Downloads.IS_PENDING, 1);


        Uri collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        ContentResolver resolver = context.getContentResolver();
        Uri itemUri = resolver.insert(collection, values);

        if (itemUri == null) {
            throw new IOException("Nie można utworzyć pliku w Downloads");
        }

        try (ParcelFileDescriptor pfd = resolver.openFileDescriptor(itemUri, "w")) {
            if (pfd == null) {
                throw new IOException("Nie można otworzyć pliku do zapisu");
            }

            FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());
            writePdf(context, ticket, segments, out);
            out.flush();
        }

        values.clear();
        values.put(MediaStore.Downloads.IS_PENDING, 0);
        resolver.update(itemUri, values, null, null);

        return itemUri;
    }

    private static void writePdf(
            Context context,
            Ticket ticket,
            @Nullable List<StopSegmentAdapter.Segment> segments,
            OutputStream outputStream
    ) throws IOException {

        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();
        Paint linePaint = new Paint();
        Paint qrPaint = new Paint();

        titlePaint.setTextSize(22f);
        titlePaint.setFakeBoldText(true);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setAntiAlias(true);

        paint.setTextSize(14f);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        linePaint.setColor(Color.LTGRAY);
        linePaint.setStrokeWidth(2f);

        qrPaint.setColor(Color.BLACK);
        qrPaint.setAntiAlias(true);

        int pageWidth = 595;   // A4 in px-ish for PDF canvas
        int pageHeight = 842;
        int x = 40;
        int y = 50;
        int lineHeight = 22;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Locale pl = new Locale("pl", "PL");

        canvas.drawText("Bilet kolejowy", x, y, titlePaint);
        y += 20;
        canvas.drawLine(x, y, pageWidth - x, y, linePaint);
        y += 35;

        drawLine(canvas, paint, x, y, "Ticket ID:", String.valueOf(ticket.getId()));
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Pasażer:", safe(ticket.getProfileName()));
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Pociąg:", safe(ticket.getTrainName()));
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Skąd:", safe(ticket.getFromStationName()));
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Dokąd:", safe(ticket.getToStationName()));
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Miejsce:", ticket.getSeatNumber() != null ? String.valueOf(ticket.getSeatNumber()) : "-");
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Cena:", ticket.getPrice() != null ? String.format(pl, "%.2f zł", ticket.getPrice()) : "-");
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Wyjazd:", ticket.getActualDeparture() != null ? ticket.getActualDeparture().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "-");
        y += lineHeight;

        drawLine(canvas, paint, x, y, "Przyjazd:", ticket.getActualArrival() != null ? ticket.getActualArrival().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "-");
        y += lineHeight;

        if (ticket.getActualDeparture() != null && ticket.getActualArrival() != null) {
            Duration duration = Duration.between(ticket.getActualDeparture(), ticket.getActualArrival());
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            drawLine(canvas, paint, x, y, "Czas w podróży:", hours + "h " + minutes + "m");
            y += lineHeight;
        }

        y += 12;
        canvas.drawLine(x, y, pageWidth - x, y, linePaint);
        y += 28;

        canvas.drawText("Trasa", x, y, titlePaint);
        y += 20;

        if (segments != null && !segments.isEmpty()) {
            for (StopSegmentAdapter.Segment segment : segments) {
                String text = segment.depTime + "  " + segment.depStation
                        + "  →  " + segment.arrTime + "  " + segment.arrStation;
                canvas.drawText(text, x, y, paint);
                y += lineHeight;
                if (y > pageHeight - 180) break;
            }
        } else {
            canvas.drawText("Brak danych o segmentach trasy", x, y, paint);
            y += lineHeight;
        }

        Bitmap qrBitmap = createQrBitmap("ticket:" + ticket.getId(), 220, 220);
        if (qrBitmap != null) {
            int qrLeft = pageWidth - x - 220;
            int qrTop = pageHeight - 280;
            canvas.drawBitmap(qrBitmap, qrLeft, qrTop, null);

            canvas.drawText("QR ticket ID", qrLeft, qrTop - 12, paint);
        }

        document.finishPage(page);

        document.writeTo(outputStream);
        document.close();
    }

    private static void drawLine(Canvas canvas, Paint paint, int x, int y, String label, String value) {
        canvas.drawText(label, x, y, paint);
        canvas.drawText(value, x + 180, y, paint);
    }

    private static String safe(String value) {
        return value != null ? value : "-";
    }

    @Nullable
    private static Bitmap createQrBitmap(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}