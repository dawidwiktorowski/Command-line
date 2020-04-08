package com.company;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Path obecnaSciezka = Paths.get("");
        System.out.println();
        System.out.println("Dawid Wiktorowski (DawWik Systems) [ Wersja 1.0 ]");
        System.out.println("(c) 2020 Dawid Wiktorowski (DaWik Systems) . Wszelkie prawa zastrzeżone.");
        System.out.println();
        String klawisz;
        while (true) {
            System.out.print(wyswietle(obecnaSciezka) + ">");
            String w = scanner.nextLine();
            String[] podziela = w.split(" ", 2);
            switch (podziela[0]) {
                case "cd":
                    try {
                        obecnaSciezka = podacSciezke(obecnaSciezka, podziela[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Podana komenda potrzebuje argumentu");
                    }
                    break;
                case "exit":
                    System.exit(0);
                    break;
                case "mkdir":
                    try {
                        getFile(obecnaSciezka, podziela[1]);

                    } catch (ArrayIndexOutOfBoundsException s) {
                        System.out.println("Podana komenda potrzebuje argumentu");
                    }
                    break;
                case "help":

                    System.out.println("CD             Wyświetla nazwę lub zmienia bieżący katalog.");
                    System.out.println("DIR            Wyświetla listę plików i podkatalogów w katalogu.");
                    System.out.println("EXIT           Zamyka program CMD.EXE (interpreter poleceń).");
                    System.out.println("RENAME         Zmienia nazwę pliku lub plików.");
                    System.out.println("RMDIR          Usuwa katalog.");
                    System.out.println("DEL            Usuwa jeden lub więcej plików.");
                    System.out.println("DATE           Wyswietla datę.");
                    System.out.println("Time           Wyswietla czas.");
                    System.out.println("PAUSE          Zawiesza przetwarzanie pliku wsadowego i wyświetla komunikat.");
                    System.out.println("MKDIR          Tworzy katalog..");
                    System.out.println("CLS            Czysci konsole");
                    System.out.println("IPCONFIG       Podaje IP");
                    System.out.println("GETMAC         Podaje addres MAC");
                    System.out.println("HOSTNAME       Podaje nazwe hosta");

                    break;
                case "cd..":
                    obecnaSciezka = sciezkaDostepu(obecnaSciezka);
                    break;
                case "dir":
                    System.out.println("Katalog: " + wyswietle(obecnaSciezka));
                    List<Path> pathList = Files.list(obecnaSciezka).collect(Collectors.toList());
                    System.out.println();
                    int liczPliki = 0;
                    int liczPliki2 = 0;
                    for (Path pokoleji : pathList) {
                        BasicFileAttributes attr = Files.readAttributes(pokoleji, BasicFileAttributes.class);
                        if (attr.isDirectory())
                            liczPliki2++;
                        else
                            liczPliki++;
                        System.out.println(attr.creationTime() + "  " + (attr.isDirectory() ? "<DIR> " : "      ")
                                + pokoleji.getFileName());
                    }
                    System.out.println("liczba plików: " + liczPliki);
                    System.out.println("liczba katalogów: " + liczPliki2);
                    break;


                case "rename":
                    rename(obecnaSciezka, podziela[1], podziela[2]);
                    break;

                case "rmdir":

                    obecnaSciezka = rmDir(obecnaSciezka, podziela[1]);

                    break;

                case "del":
                    try {
                        obecnaSciezka = del(obecnaSciezka, podziela[1]);
                    } catch (ArrayIndexOutOfBoundsException s) {
                        System.out.println("Podana komenda potrzebuje argumentu,");
                    }
                    break;
                case "date":
                    Date pokazDzisDate = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    System.out.println("Dzisiejsza data to: " + simpleDateFormat.format(pokazDzisDate));
                    break;
                case "time":
                    Date pokazCzas = new Date();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
                    System.out.println("Aktualna godzina to: " + simpleDateFormat1.format(pokazCzas));
                    break;

                case "pause":
                    System.out.println("Nacisnij klawisz aby kontynuowac ...");
                    klawisz = scanner.nextLine();
                    break;
/////////////////////////////////////////////////////////////////////////////////
                //DODATKOWE


                case "getmac":
                    podanieMAC();
                    break;
                case "hostname":
                    nazwaHosta();
                    break;
                case "ipconfig":
                    podanieIP();
                    break;
                case "cls":
                    czysciKonsole();
                    break;


                default:
                    System.out.println("Komenda nie jest rozpoznawana jako polecenie wewnetrzne lub zewnetrzne");
            }
        }
    }

    private static Path del(Path sciezkaObecna, String nazwaSciezki) {
        Path sciezkaTymczasow = Paths.get(wyswietle(sciezkaObecna) + "\\" + nazwaSciezki);
        Path fileName = sciezkaTymczasow;
        File file = new File(String.valueOf(fileName));

        boolean fileExistss = file.exists();
        if (fileExistss) {
            fileExistss = file.delete();
        }
        return sciezkaObecna;
    }

    private static String wyswietle(Path path) {
        return path.toAbsolutePath().toString();
    }
    public static void nazwaHosta() throws UnknownHostException {
        InetAddress ip;
        String hostName;
        try {
            ip = InetAddress.getLocalHost();
            hostName = ip.getHostName();
            System.out.println(hostName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void czysciKonsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static Path podacSciezke(Path sciezkaObecna, String informacje) {
        Path sciezkaTymczasowa = Paths.get(wyswietle(sciezkaObecna) + "\\" + informacje);
        if (Files.exists(sciezkaTymczasowa))
            return sciezkaTymczasowa;
        else
            System.out.println("nie znaleziono katalogu");
        return sciezkaObecna;
    }
    public static void podanieIP() throws UnknownHostException {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Your current IP address is: " + ip.getHostAddress());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static Path getFile(Path sciezkaObecna, String nazwaSciezki) {
        Path sciezkaTymczasowa = Paths.get(wyswietle(sciezkaObecna) + "\\" + nazwaSciezki);
        Path fileName = sciezkaTymczasowa;
        File file = new File(String.valueOf(fileName));

        boolean fileExists = file.exists();
        if (!fileExists) {
            fileExists = file.mkdir();
        }
        return sciezkaObecna;
    }

    private static Path rmDir(Path sciezkaObecna, String nazwaSciezki) {
        Path sciezkaTymczasowa = Paths.get(wyswietle(sciezkaObecna) + "\\" + nazwaSciezki);
        Path fileName = sciezkaTymczasowa;
        File file = new File(String.valueOf(fileName));

        boolean fileExists = file.exists();
        if (fileExists) {
            fileExists = file.delete();
        }
        return sciezkaObecna;
    }

    public static void rename(Path sciezkaObecna, String nazwaSciezki, String nazwa) throws IOException {
        Path sciezkaTymczasowa = Paths.get(wyswietle(sciezkaObecna) + "\\" + nazwaSciezki);
        Path sciezkaTymczasowa2 = Paths.get(wyswietle(sciezkaObecna) + "\\" + nazwa);
        if (Files.exists(sciezkaTymczasowa)) {
            Files.move(sciezkaTymczasowa, sciezkaTymczasowa2);
        } else {
            System.out.println("Nie ma takiego pliku");
        }
    }

    private static Path sciezkaDostepu(Path sciezkaObecna) {
        Path sciezkaTymczasowa = Paths.get(wyswietle(sciezkaObecna));
        sciezkaTymczasowa = sciezkaTymczasowa.getParent();
        if (sciezkaTymczasowa != null)
            return sciezkaTymczasowa;
        else
            System.out.println("brak katalogu nadrzędnego");
        return sciezkaObecna;


    }

    public static void podanieMAC() throws SocketException, UnknownHostException {
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();
        System.out.print("Your current MAC address is: ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        System.out.println(sb.toString());
    }
}