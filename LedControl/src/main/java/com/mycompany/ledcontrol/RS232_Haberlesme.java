/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ledcontrol;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;

public class RS232_Haberlesme {

    private SerialPort serialPort;

    // Constructor: Port adı ve baud rate ile seri bağlantıyı başlatır
    public RS232_Haberlesme(String portAdi, int baudRate) {
        // Seri port seç ve bağlantıyı aç
        serialPort = SerialPort.getCommPort(portAdi);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()) {
            System.out.println("Serial port bağlantısı başarılı.");
        } else {
            throw new RuntimeException("Serial port bağlantısı başarısız!");
        }
    }

    // Komut gönderme fonksiyonu
    public void KomutFonksiyonu(char komut) {
        try {
            OutputStream outputStream = serialPort.getOutputStream();
            outputStream.write(komut);
            outputStream.flush();
            System.out.println("Komut gönderildi: " + komut);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Seri porttan veri okuma fonksiyonu
    public void VeriOku(SerialDataListener listener) {
        new Thread(() -> {
            try {
                InputStream inputStream = serialPort.getInputStream();
                StringBuilder dataBuffer = new StringBuilder();

                while (true) {
                    if (inputStream.available() > 0) {
                        char receivedChar = (char) inputStream.read();
                        if (receivedChar == '\n') {
                            String mesaj = dataBuffer.toString().trim();
                            System.out.println("Gelen mesaj: " + mesaj);

                            // Gelen mesajı arayüze ilet
                            listener.AlinanVeri(mesaj);
                            dataBuffer.setLength(0);
                        } else {
                            dataBuffer.append(receivedChar);
                        }
                    }
                    Thread.sleep(10);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // Seri portu kapatma fonksiyonu
    public void PortKapat() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Seri port kapatıldı!");
        }
    }

    // SerialDataListener Arayüzü
    public interface SerialDataListener {
        void AlinanVeri(String veri);
    }
}

