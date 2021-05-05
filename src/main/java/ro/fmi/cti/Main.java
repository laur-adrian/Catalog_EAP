package ro.fmi.cti;

import catalog.Catalog;
import catalog.Grupa;
import catalog.Utilizator;

import java.util.Scanner;

public class Main {

    private static Catalog catalog = new Catalog();

    public static void main(String[] args) {
        int nota;
        catalog.logare("laurica", "farafrica");
        System.out.println("Scrieti nota pe care o acordati studentului Bob, de la grupa 252, la Istorie:1");
        Scanner scanner = new Scanner(System.in); // pentru verificarea functionarii scrierii in fisiere
        nota = scanner.nextInt();
        catalog.adaugareNota("grupa252", "Istorie", nota, "BOB");
        catalog.afisareNoteStudent("BOB");
        catalog.afisareNoteMaterieGrupa("Matematica","grupa252");
        catalog.afisareStudentiGrupa("grupa252");
        catalog.afisareMateriiGrupa("grupa252");



        catalog.delogare();

        catalog.salveazaInCSV();
    }
}
