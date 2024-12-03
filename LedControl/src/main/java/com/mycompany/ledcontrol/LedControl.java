/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ledcontrol;

import javax.swing.*;
import java.awt.*;

public class LedControl {
    private RS232_Haberlesme serialComm; // Seri haberleşme sınıfı
    private JLabel statusLabel;          // GUI durumu göstermek için etiket

    public LedControl(String portAdi, int baudRate) {
        // Seri haberleşmeyi başlat
        serialComm = new RS232_Haberlesme(portAdi, baudRate);

        // Seri porttan gelen verileri dinle
        serialComm.VeriOku(new RS232_Haberlesme.SerialDataListener() {
            @Override
            public void AlinanVeri(String veri) {
                // Gelen veriyi GUI durumuna yansıt
                SwingUtilities.invokeLater(() -> statusLabel.setText("Durum: " + veri));
            }
        });

        // Kullanıcı arayüzünü başlat
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Arduino LED Kontrol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 1));

        // "LED AÇ" ve "LED KAPAT" butonları
        JButton ledOnButton = new JButton("LED AÇ");
        JButton ledOffButton = new JButton("LED KAPAT");
        statusLabel = new JLabel("Durum: Bekleniyor...", SwingConstants.CENTER);

        // "LED AÇ" butonuna tıklanınca komut gönder
        ledOnButton.addActionListener(e -> serialComm.KomutFonksiyonu('A'));

        // "LED KAPAT" butonuna tıklanınca komut gönder
        ledOffButton.addActionListener(e -> serialComm.KomutFonksiyonu('K'));

        // Butonları ve etiketi ekle
        frame.add(ledOnButton);
        frame.add(ledOffButton);
        frame.add(statusLabel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Port adı ve baud rate Arduino'ya göre ayarlanmalı
        String portName = "/dev/ttyUSB0"; // Linux için USB seri port adı
        int baudRate = 9600;

        // Uygulamayı başlat
        new LedControl(portName, baudRate);
    }
}

