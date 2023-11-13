package loketbayar.id;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import loketbayar.id.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HasilRekap extends AppCompatActivity implements PrintingCallback {
    private SharedPreferences boyprefs;
    private String datareport;
    private PdfWriter writer;
    private Context context;
    Printing printing;
    private float subtotal=0,hargalokets=0,fees=0;
    private int ttltrx=0,ttltrx2=0;
    private String user="";
    private RecyclerView rvList;
    private ArrayList<String> kode_produk=new ArrayList<>();
    private ArrayList<String> inputs=new ArrayList<>();
    private ArrayList<String> keterangan=new ArrayList<>();
    private ArrayList<String> fee_loket=new ArrayList<>();
    private ArrayList<String> fee_bulanan=new ArrayList<>();
    private ArrayList<String> harga_loket=new ArrayList<>();
    private ArrayList<String> nama_produk=new ArrayList<>();
    private ArrayList<String> nama=new ArrayList<>();
    private ArrayList<String> usernames=new ArrayList<>();
    private ArrayList<String> total=new ArrayList<>();
    private ArrayList<String> kode_loket=new ArrayList<>();
    private ArrayList<String> jumlah_transaksi=new ArrayList<>();
    private ArrayList<String> status=new ArrayList<>();
    private DecimalFormat format=new DecimalFormat("###,###,###.##");
    private String bulan,tahun,namanya,datanya;
    private TextView tvTotal,tvTotalfee,tvHarlok,tvTotaltrx;
    private Button btpdf;
    public static float totalsemua=0,totalhargaloket=0,totalfee=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boyprefs = getSharedPreferences(getResources().getString(R.string.sp), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_hasil_rekap);
        rvList=findViewById(R.id.rvList);
        tvTotal=findViewById(R.id.tvTotal);
        tvTotalfee=findViewById(R.id.tvTotalfee);
        tvHarlok=findViewById(R.id.tvHarlok);
        tvTotaltrx=findViewById(R.id.tvTotaltrx);
        btpdf=findViewById(R.id.btPdf);
        btpdf.setVisibility(View.GONE);
        datareport=boyprefs.getString("datareport","");
        bulan=boyprefs.getString("bulan","");
        tahun=boyprefs.getString("tahun","");
        kode_produk=PojoMion.AmbilArray(datareport,"kode_produk","datareport");
        nama_produk=PojoMion.AmbilArray(datareport,"nama_produk","datareport");
        inputs=PojoMion.AmbilArray(datareport,"inputs","datareport");
        keterangan=PojoMion.AmbilArray(datareport,"keterangan","datareport");
        usernames=PojoMion.AmbilArray(datareport,"username","datareport");
        fee_loket=PojoMion.AmbilArray(datareport,"fee_loket","datareport");
        harga_loket=PojoMion.AmbilArray(datareport,"harga_loket","datareport");
        kode_loket=PojoMion.AmbilArray(datareport,"kode_loket","datareport");
        total=PojoMion.AmbilArray(datareport,"total","datareport");
        jumlah_transaksi=PojoMion.AmbilArray(datareport,"jumlah_transaksi","datareport");
        status=PojoMion.AmbilArray(datareport,"status","datareport");
        fee_bulanan=PojoMion.AmbilArray(datareport,"fee_bulanan","datareport");
        ViewCompat.setNestedScrollingEnabled(rvList, false);

        RekapAdapter boyiadapter2=new RekapAdapter(HasilRekap.this,
                kode_produk,inputs,nama,total,bulan,tahun,nama_produk,usernames,keterangan,
                fee_loket,harga_loket,tvTotal,tvTotalfee,tvHarlok,jumlah_transaksi,status,fee_bulanan);
        boyiadapter2.notifyDataSetChanged();
        boyiadapter2.setHasStableIds(true);
        rvList.setAdapter(boyiadapter2);
        for(int x=0;x<total.size();x++) {
          //  DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

            int ttltrx3= Integer.parseInt(jumlah_transaksi.get(x));
            ttltrx2=ttltrx2+ttltrx3;
            tvTotaltrx.setText(String.valueOf(ttltrx2));




        }
        btpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dest = context.getExternalFilesDir(null) + "/RTS";
                File myDir = new File(dest + "/PDF");
                if (!myDir.exists())
                    myDir.mkdirs();
                File h = new File(dest
                        + "/PDF/" + "REKAP" + ".pdf");
                Document document = new Document(PageSize.A4);
                Uri path = FileProvider.getUriForFile(
                        HasilRekap.this,
                        HasilRekap.this.getApplicationContext().getPackageName() + ".mudahbayar.fileprovider", //(use your app signature + ".provider" )
                        h);
// Create Pdf Writer for Writting into New Created Document
                try {
                    writer = PdfWriter.getInstance(document, new FileOutputStream(h));

                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }

// Open Document for Writting into document
                document.open();
                addMetaData2(document);

                try {
                    addTitlePage1(document);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
// Close Document after writting all content
                document.close();


                // send email
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello...");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Laporan Rekap "+kode_loket.get(0));
                emailIntent.putExtra(Intent.EXTRA_STREAM, path);

                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            //    emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //    emailIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //    startActivity(emailIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HasilRekap.this,Laporan.class));
        finish();
    }
    public void addMetaData2(Document document) {
        document.addTitle("Faktur");
        document.addSubject("Faktur");
        document.addKeywords("Company");
    }
    public void addTitlePage1(Document document) throws DocumentException {
        // Creating a table object
        String tanggal=GetDate.ambiltanggal();
        Paragraph cv = new Paragraph();
        cv.setAlignment(Element.ALIGN_CENTER);
        cv.add("Laporan Rekap "+kode_loket.get(0)+"\n\n");
        cv.setIndentationLeft(20);
        cv.setIndentationRight(20);
        document.add(cv);
        PdfPTable table = new PdfPTable(6);
        table.setWidths(new int[]{1, (int) 1, 2, 2, 2, 2});
        PdfPCell cell = new PdfPCell(new Phrase("No."));
        PdfPCell cell2 = new PdfPCell(new Phrase("USER"));
        PdfPCell cell3 = new PdfPCell(new Phrase("PRODUK"));
        PdfPCell cell4 = new PdfPCell(new Phrase("FEE LOKET"));
        PdfPCell cell5 = new PdfPCell(new Phrase("HARGA LOKET"));
        PdfPCell cell6 = new PdfPCell(new Phrase("TOTAL"));
        table.addCell(cell);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        document.add(table);
        float adm=0;
        float totaltag=0;
        float totsnya=0;
        float dendas=0;
        float pinalti=0;
        int nomer=0;
        int ttltrans=0;
        for(int x=0;x<usernames.size();x++){
            nomer++;
            int ttltrx3= Integer.parseInt(jumlah_transaksi.get(x));
            ttltrans=ttltrans+ttltrx3;

        //    ttltrans++;
            if(x==0){

                float premmmms= Float.parseFloat(total.get(x));
                float harlok= Float.parseFloat(harga_loket.get(x));
                float subfee= Float.parseFloat(fee_loket.get(x));
                subtotal=subtotal+premmmms;
                fees=fees+subfee;
                hargalokets=hargalokets+harlok;
                String jumlah= String.valueOf(jumlah_transaksi.get(x));
                int ttltrx4= Integer.parseInt(jumlah);
                ttltrx=ttltrx4+ttltrx;
                //  ttltrx++;
                user = usernames.get(x);
                float feeloket= Float.parseFloat(fee_loket.get(x));
                float hargaloket= Float.parseFloat(harga_loket.get(x));
                float totals= Float.parseFloat(total.get(x));
                float jml= Float.parseFloat(jumlah_transaksi.get(x));
                PdfPTable table2 = new PdfPTable(6);
                table2.setWidths(new int[]{1, 1, 2, 2, 2, 2});
                PdfPCell cells = new PdfPCell(new Phrase(String.valueOf(nomer)));
                PdfPCell cells2 = new PdfPCell(new Phrase(usernames.get(x)));
                PdfPCell cells3 = new PdfPCell(new Phrase(nama_produk.get(x)));
                PdfPCell cells4 = new PdfPCell(new Phrase("Rp."+format.format(feeloket)+" ( "+format.format(jml)+" )"));
                PdfPCell cells5 = new PdfPCell(new Phrase("Rp."+format.format(hargaloket)));
                PdfPCell cells6 = new PdfPCell(new Phrase("Rp."+format.format(totals)));
                table2.addCell(cells);
                table2.addCell(cells2);
                table2.addCell(cells3);
                table2.addCell(cells4);
                table2.addCell(cells5);
                table2.addCell(cells6);
                document.add(table2);

            }

            else if(x==total.size()-1){
                float premmmms= Float.parseFloat(total.get(x));
                subtotal=subtotal+premmmms;
                float harlok= Float.parseFloat(harga_loket.get(x));
                float subfee= Float.parseFloat(fee_loket.get(x));
                fees=fees+subfee;
                hargalokets=hargalokets+harlok;
                String jumlah= String.valueOf(jumlah_transaksi.get(x));
                int ttltrx4= Integer.parseInt(jumlah);
                ttltrx=ttltrx4+ttltrx;
                //  ttltrx++;
                float feeloket= Float.parseFloat(fee_loket.get(x));
                float hargaloket= Float.parseFloat(harga_loket.get(x));
                float totals= Float.parseFloat(total.get(x));
                float jml= Float.parseFloat(jumlah_transaksi.get(x));
                PdfPTable table2 = new PdfPTable(6);
                table2.setWidths(new int[]{1, 1, 2, 2, 2, 2});
                PdfPCell cells = new PdfPCell(new Phrase(String.valueOf(nomer)));
                PdfPCell cells2 = new PdfPCell(new Phrase(""));
                PdfPCell cells3 = new PdfPCell(new Phrase(nama_produk.get(x)));
                PdfPCell cells4 = new PdfPCell(new Phrase("Rp."+format.format(feeloket)+" ( "+format.format(jml)+" )"));
                PdfPCell cells5 = new PdfPCell(new Phrase("Rp."+format.format(hargaloket)));
                PdfPCell cells6 = new PdfPCell(new Phrase("Rp."+format.format(totals)));
                table2.addCell(cells);
                table2.addCell(cells2);
                table2.addCell(cells3);
                table2.addCell(cells4);
                table2.addCell(cells5);
                table2.addCell(cells6);
                document.add(table2);
                PdfPTable table3 = new PdfPTable(6);
                table3.setWidths(new int[]{1, 1, 2, 2, 2, 2});
                PdfPCell cellsub = new PdfPCell(new Phrase("Sub"));
                PdfPCell cellsub2 = new PdfPCell(new Phrase( String.valueOf(ttltrx)));
                PdfPCell cellsub3 = new PdfPCell(new Phrase(""));
                PdfPCell cellsub4 = new PdfPCell(new Phrase("Rp."+format.format(fees)));
                PdfPCell cellsub5 = new PdfPCell(new Phrase("Rp."+format.format(hargalokets)));
                PdfPCell cellsub6 = new PdfPCell(new Phrase("Rp."+format.format(subtotal)));
                table3.addCell(cellsub);
                table3.addCell(cellsub2);
                table3.addCell(cellsub3);
                table3.addCell(cellsub4);
                table3.addCell(cellsub5);
                table3.addCell(cellsub6);
                document.add(table3);
                subtotal=0;
                hargalokets=0;
                fees=0;
                ttltrx=0;
               /* Paragraph subtotals = new Paragraph();
                subtotals.setAlignment(Element.ALIGN_LEFT);
                subtotals.add("Subtotal: Rp."+format.format(subtotal)+"\n\n");
                subtotals.setIndentationLeft(50);
                subtotals.setIndentationRight(50);
                document.add(subtotals);
                subtotal=0;*/
            }

            else{
                if(user.equalsIgnoreCase(usernames.get(x))) {
                    float premmmms = Float.parseFloat(total.get(x));
                    float harlok= Float.parseFloat(harga_loket.get(x));
                    float subfee= Float.parseFloat(fee_loket.get(x));
                    subtotal=subtotal+premmmms;
                    fees=fees+subfee;
                    hargalokets=hargalokets+harlok;
                    float jml= Float.parseFloat(jumlah_transaksi.get(x));
                    String jumlah= String.valueOf(jumlah_transaksi.get(x));
                    int ttltrx4= Integer.parseInt(jumlah);
                    ttltrx=ttltrx4+ttltrx;
                    //  ttltrx++;
                    float feeloket = Float.parseFloat(fee_loket.get(x));
                    float hargaloket = Float.parseFloat(harga_loket.get(x));
                    float totals = Float.parseFloat(total.get(x));
                    PdfPTable table2 = new PdfPTable(6);
                    table2.setWidths(new int[]{1, 1, 2, 2, 2, 2});
                    PdfPCell cells = new PdfPCell(new Phrase(String.valueOf(nomer)));
                    PdfPCell cells2 = new PdfPCell(new Phrase(""));
                    PdfPCell cells3 = new PdfPCell(new Phrase(nama_produk.get(x)));
                    PdfPCell cells4 = new PdfPCell(new Phrase("Rp." + format.format(feeloket)+" ( "+format.format(jml)+" )"));
                    PdfPCell cells5 = new PdfPCell(new Phrase("Rp." + format.format(hargaloket)));
                    PdfPCell cells6 = new PdfPCell(new Phrase("Rp." + format.format(totals)));
                    table2.addCell(cells);
                    table2.addCell(cells2);
                    table2.addCell(cells3);
                    table2.addCell(cells4);
                    table2.addCell(cells5);
                    table2.addCell(cells6);
                    document.add(table2);
                } else{
                    PdfPTable table3 = new PdfPTable(6);
                    table3.setWidths(new int[]{1, 1, 2, 2, 2, 2});
                    PdfPCell cellsub = new PdfPCell(new Phrase("Sub"));
                    PdfPCell cellsub2 = new PdfPCell(new Phrase(String.valueOf(ttltrx)));
                    PdfPCell cellsub3 = new PdfPCell(new Phrase(""));
                    PdfPCell cellsub4 = new PdfPCell(new Phrase("Rp."+format.format(fees)));
                    PdfPCell cellsub5 = new PdfPCell(new Phrase("Rp."+format.format(hargalokets)));
                    PdfPCell cellsub6 = new PdfPCell(new Phrase("Rp."+format.format(subtotal)));
                    table3.addCell(cellsub);
                    table3.addCell(cellsub2);
                    table3.addCell(cellsub3);
                    table3.addCell(cellsub4);
                    table3.addCell(cellsub5);
                    table3.addCell(cellsub6);
                    document.add(table3);

                    float feeloket = Float.parseFloat(fee_loket.get(x));
                    float hargaloket = Float.parseFloat(harga_loket.get(x));
                    float totals = Float.parseFloat(total.get(x));
                    float jml= Float.parseFloat(jumlah_transaksi.get(x));
                    PdfPTable table2 = new PdfPTable(6);
                    table2.setWidths(new int[]{1, 1, 2, 2, 2, 2});
                    PdfPCell cells = new PdfPCell(new Phrase(String.valueOf(nomer)));
                    PdfPCell cells2 = new PdfPCell(new Phrase(usernames.get(x)));
                    PdfPCell cells3 = new PdfPCell(new Phrase(nama_produk.get(x)));
                    PdfPCell cells4 = new PdfPCell(new Phrase("Rp."+format.format(feeloket)+" ( "+format.format(jml)+" )"));
                    PdfPCell cells5 = new PdfPCell(new Phrase("Rp."+format.format(hargaloket)));
                    PdfPCell cells6 = new PdfPCell(new Phrase("Rp."+format.format(totals)));
                    table2.addCell(cells);
                    table2.addCell(cells2);
                    table2.addCell(cells3);
                    table2.addCell(cells4);
                    table2.addCell(cells5);
                    table2.addCell(cells6);
                    document.add(table2);
                    user=usernames.get(x);
                    subtotal=0;
                    subtotal=0;
                    hargalokets=0;
                    fees=0;
                    ttltrx=0;
                    float premmmms= Float.parseFloat(total.get(x));
                    subtotal=subtotal+premmmms;
                    float harlok= Float.parseFloat(harga_loket.get(x));
                    float subfee= Float.parseFloat(fee_loket.get(x));
                    fees=fees+subfee;
                    String jumlah= String.valueOf(jumlah_transaksi.get(x));
                    int ttltrx4= Integer.parseInt(jumlah);
                    ttltrx=ttltrx4+ttltrx;
                    //  ttltrx++;
                    hargalokets=hargalokets+harlok;
                }


            }



        }




        Paragraph totals = new Paragraph();
        totals.setAlignment(Element.ALIGN_RIGHT);
        totals.add("\n\n");
        totals.setIndentationLeft(20);
        totals.setIndentationRight(20);
        document.add(totals);
        PdfPTable table3 = new PdfPTable(6);
        table3.setWidths(new int[]{1, 1, 2, 2, 2, 2});
        PdfPCell cellsub = new PdfPCell(new Phrase("TTL"));
        PdfPCell cellsub2 = new PdfPCell(new Phrase(String.valueOf(ttltrans)));
        PdfPCell cellsub3 = new PdfPCell(new Phrase(""));
        PdfPCell cellsub4 = new PdfPCell(new Phrase("Rp."+format.format(totalfee)));
        PdfPCell cellsub5 = new PdfPCell(new Phrase("Rp."+format.format(totalhargaloket)));
        PdfPCell cellsub6 = new PdfPCell(new Phrase("Rp."+format.format(totalsemua)));
        table3.addCell(cellsub);
        table3.addCell(cellsub2);
        table3.addCell(cellsub3);
        table3.addCell(cellsub4);
        table3.addCell(cellsub5);
        table3.addCell(cellsub6);
        document.add(table3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK);
        initview();
    }
    void initview() {
        if (printing != null) {
            printing.setPrintingCallback(this);
        }


    }
    @Override
    public void connectingWithPrinter() {

    }
    @Override
    public void connectionFailed(String s) {

    }
    @Override
    public void onError(String s) {

    }
    @Override
    public void onMessage(String s) {

    }
    @Override
    public void printingOrderSentSuccessfully() {
    }

    @Override
    public void disconnected() {

    }
}
