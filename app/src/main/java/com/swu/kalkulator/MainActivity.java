package com.swu.kalkulator;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //deklarasi variabel
    EditText editNum1, editNum2;
    Button buttonPlus, buttonMin, buttonMulti, buttonDiv, buttonCalc, buttonClear;
    TextView textResult;


    String operatorTerpilih = "";

    public static ArrayList<Riwayat> dataRiwayat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

//inisialisasi variabel
        editNum1 = findViewById(R.id.editNum1);
        editNum2 = findViewById(R.id.editNum2);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMin = findViewById(R.id.buttonMin);
        buttonMulti = findViewById(R.id.buttonMulti);
        buttonDiv = findViewById(R.id.buttonDiv);
        buttonCalc = findViewById(R.id.buttonCalc);
        buttonClear = findViewById(R.id.buttonClear);
        textResult = findViewById(R.id.textResult);

//tambah event
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "+";
                Toast.makeText(MainActivity.this, "Operator + dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "-";
                Toast.makeText(MainActivity.this, "Operator - dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "x";
                Toast.makeText(MainActivity.this, "Operator x dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = ":";
                Toast.makeText(MainActivity.this, "Operator : dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungHasil();
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNum1.setText("");
                editNum2.setText("");
                textResult.setText("");
                operatorTerpilih= "";
                editNum1.requestFocus();
            }
        });
    }

    private void kirimLokasiKeWa(){
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        try{
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (location != null){
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            String mapsLink = "https://maps.google.com/?q=" + latitude + "," + longitude;
            String pesan = "Halo Retna, ini lokasi terbaru saya : " + mapsLink;

            try {
                String urlwa = "https://wa.me/6281223760052?text=" + java.net.URLEncoder.encode(pesan, "UTF-8");
                android.content.Intent intentwa = new android.content.Intent(Intent.ACTION_VIEW);
                intentwa.setData(android.net.Uri.parse(urlwa));
                startActivity(intentwa);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Terjadi kesalahan saat mendapatkan data GPS dan kirim", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Pastikan GPS HP menyala", Toast.LENGTH_SHORT).show();
        }
    }

    private void hitungHasil(){
        String input1 = editNum1.getText().toString();
        String input2 = editNum2.getText().toString();

//validasi angka terisi atau tidak

        if(input1.isEmpty() || input2.isEmpty()){
            Toast.makeText(this, "Mohon isi kedua angka terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        if(operatorTerpilih.isEmpty()){
            Toast.makeText(this, "Mohon pilih operator dipilih terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

//konversi tipe data string

        double angka1 = Double.parseDouble(input1);
        double angka2 = Double.parseDouble(input2);
        double hasil = 0;
        String teksPerhitungan = angka1 + " " + operatorTerpilih + " " + angka2;

//proses perhitungan
        switch (operatorTerpilih){
            case "+":
                hasil = angka1 + angka2;
                break;
            case "-":
                hasil = angka1 - angka2;
                break;
            case "x":
                hasil = angka1 * angka2;
                break;
            case ":":
                hasil = angka1 / angka2;
                break;
        }
        textResult.setText(String.valueOf(hasil));
        dataRiwayat.add(new Riwayat(teksPerhitungan, String.valueOf(hasil)));

        if (hasil == 0.0){
            android.content.Intent intentKamera = new android.content.Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intentKamera);
        } else if (hasil == 8.0){
            android.content.Intent intentGambar = new android.content.Intent(MainActivity.this, ImageActivity.class);
            startActivity(intentGambar);
        } else if (hasil == 3.0) {
            // Memutar audio (pastikan Anda sudah meletakkan file audio, misal 'suara.mp3', di folder res/raw)
            android.media.MediaPlayer mediaPlayer = android.media.MediaPlayer.create(MainActivity.this, R.raw.minefields);
            mediaPlayer.start();
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.release(); // Melepaskan resource memory
                    }
                }
            }, 10000);

        } else if (hasil == 9.0) {
            if (androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED
                    && androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {

                android.widget.Toast.makeText(this, "Izin lokasi belum diberikan!", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Ambil lokasi menggunakan FusedLocationProviderClient
            com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient =
                    com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // 3. Ubah koordinat menjadi Nama Alamat menggunakan Geocoder
                    android.location.Geocoder geocoder = new android.location.Geocoder(this, java.util.Locale.getDefault());
                    try {
                        java.util.List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String pesanAlamat;

                        if (addresses != null && !addresses.isEmpty()) {
                            pesanAlamat = "Alamat:\n" + addresses.get(0).getAddressLine(0);
                        } else {
                            pesanAlamat = "Koordinat:\nLat: " + latitude + "\nLong: " + longitude;
                        }

                        // 4. Tampilkan AlertDialog (Pop-up)
                        new androidx.appcompat.app.AlertDialog.Builder(this)
                                .setTitle("📍Lokasi GPS")
                                .setMessage(pesanAlamat)
                                .setPositiveButton("Tutup", (dialog, which) -> {
                                    dialog.dismiss(); // Menutup pop-up saat tombol diklik
                                })
                                .show();

                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        android.widget.Toast.makeText(this, "Gagal memuat alamat (Periksa internet/GPS)", android.widget.Toast.LENGTH_SHORT).show();
                    }

                } else {
                    android.widget.Toast.makeText(this, "GPS belum mendeteksi lokasi. Pastikan aktif.", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        } else if (hasil == 5.0) {
            android.content.Intent intentSensor = new android.content.Intent(MainActivity.this, SensorActivity.class);
            startActivity(intentSensor);
        } else if (hasil == 15){
            kirimLokasiKeWa();
        }
    }

}