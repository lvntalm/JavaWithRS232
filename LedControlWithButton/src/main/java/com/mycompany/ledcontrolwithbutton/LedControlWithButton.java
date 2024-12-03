/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ledcontrolwithbutton;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LedControlWithButton {
    private RS232_Haberlesme1 serialComm;
    private JLabel statusLabel; // Durum göstergesi

    public LedControlWithButton(String portAdi, int baudRate) {
        // Seri iletişimi başlat
        serialComm = new RS232_Haberlesme1(portAdi, baudRate);

        // Arayüzü başlat
        createAndShowGUI();

        // Arduino'dan gelen verileri düzenli olarak oku
        startReadingData();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Arduino LED ve Buton Kontrol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(2, 1));

        JButton statusButton = new JButton("LED Durumu");
        statusLabel = new JLabel("Durum: Bekleniyor...", SwingConstants.CENTER);

        // Butona tıklandığında LED'i aç, bırakıldığında kapat
        statusButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                serialComm.KomutFonksiyonu('A'); // LED aç komutu gönder
                System.out.println("Butona tıklandı");
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                serialComm.KomutFonksiyonu('K'); // LED kapat komutu gönder
            }
        });

        frame.add(statusButton);
        frame.add(statusLabel);
        frame.setVisible(true);
    }

    private void startReadingData() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String veri = serialComm.VeriOku();
                if (veri != null) {
                    System.out.println("Gelen veri: " + veri);
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Durum: " + veri));
                }
            }
        }, 0, 500); // Her 500 ms'de bir veri oku
    }

    public static void main(String[] args) {
        // Arduino'nun port adı ve baud hızı
        String portName = "/dev/ttyUSB0"; // Linux için port adınızı kontrol edin
        int baudRate = 9600;

        new LedControlWithButton(portName, baudRate);
    }
}

