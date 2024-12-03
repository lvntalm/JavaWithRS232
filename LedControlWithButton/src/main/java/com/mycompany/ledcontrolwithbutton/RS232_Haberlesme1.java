/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ledcontrolwithbutton;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;

public class RS232_Haberlesme1 {
    private SerialPort serialPort;
    private InputStream inputStream;

    public RS232_Haberlesme1(String portAdi, int baudRate) {
        // Seri portu seç ve bağlantıyı aç
        serialPort = SerialPort.getCommPort(portAdi);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()) {
            System.out.println("Serial port bağlantısı başarılı");
            inputStream = serialPort.getInputStream();
        } else {
            throw new RuntimeException("Serial port bağlantısı başarısız!");
        }
    }

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

    // Veri okuma işlevi
    public String VeriOku() {
        try {
            if (inputStream != null && inputStream.available() > 0) {
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                return new String(buffer, 0, bytesRead).trim();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Seri port kapat
    public void PortKapat() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Seri port kapatıldı!");
        }
    }
}

