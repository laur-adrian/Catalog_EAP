package ro.fmi.cti;

import catalog.Catalog;
import catalog.Grupa;
import catalog.Utilizator;

import java.util.Scanner;

public class Main {

    private static Catalog catalog = new Catalog();

    public static void main(String[] args) {

        catalog.logare("laurica", "lauricafarafrica");
        boolean on = true;
        while (on){
            System.out.println("Actiuni disponibile:\n" +
                                "1.Adaugare utilizator\n" +
                                "2.Adaugare grupa\n" +
                                "3.Adaugare student într-o grupă\n" +
                                "4.Adaugare materie unei grupe\n" +
                                "5.Adaugare nota unui student\n" +
                                "6.Afisarea notelor unui student\n" +
                                "7.Afisarea notelor tuturor studentilor dintr-o grupă la o anumita materie\n" +
                                "8.Afisarea studentilor dintr-o grupa\n" +
                                "9.Afisarea materiilor unei grupe\n" +
                                "10.Logare cu alt cont\n" +
                                "11.Delogare\n");
            System.out.println("Ce actiune doriți să executați? Introduceți numărul acesteia: ");
            Scanner input = new Scanner(System.in);
            String action = input.nextLine();
            switch (action){
                case "1" :
                    System.out.println("Introduceti numele persoanei:\n");
                    String nume = input.nextLine();
                    System.out.println("Introduceti numele de utilizator(username):\n");
                    String numeUtilizator = input.nextLine();
                    System.out.println("Introduceti parola contului de utilizator:\n");
                    String parola = input.nextLine();
                    System.out.println("Introduceti cifra drepturilor de utilizator(1.ADMIN/2.PROFESOR/3.STUDENT):\n");
                    String drept = input.nextLine();
                    switch (drept) {
                        case "1":
                            catalog.adaugareUtilizator(nume, numeUtilizator, parola, Utilizator.Drepturi.ADMIN);
                            break;
                        case "2":
                            catalog.adaugareUtilizator(nume, numeUtilizator, parola, Utilizator.Drepturi.PROFESOR);
                            break;
                        case "3":
                            catalog.adaugareUtilizator(nume, numeUtilizator, parola, Utilizator.Drepturi.STUDENT);
                            break;
                    }
                    break;
                case "2" :
                    System.out.println("Introduceti numele grupei(grupaXYZ, unde X,Y,Z sunt cifre):\n");
                    String numeGrupa = input.nextLine();
                    Grupa grupaNoua = new Grupa(numeGrupa);
                    catalog.adaudareGrupa(grupaNoua);
                    break;
                case "3" :
                    System.out.println("Introduceti numele studentului:\n");
                    String numeStudent = input.nextLine();
                    System.out.println("Introduceti numele grupei:\n");
                    String numeleGrupei = input.nextLine();
                    catalog.adaugareStudentGrupa(numeleGrupei, numeStudent);
                    break;
                case "4" :
                    System.out.println("Introduceti numele materiei:\n");
                    String numeMaterie = input.nextLine();
                    System.out.println("Introduceti numele profesorului:\n");
                    String numeProf = input.nextLine();
                    System.out.println("Introduceti numele grupei:\n");
                    String numeGr = input.nextLine();
                    catalog.adaugareMaterieGrupa(numeMaterie, numeProf, numeGr);
                    break;
                case "5" :
                    System.out.println("Introduceti numele grupei:\n");
                    String nameGr = input.nextLine();
                    System.out.println("Introduceti numele materiei:\n");
                    String nameMaterie = input.nextLine();
                    System.out.println("Introduceti numele studentului:\n");
                    String numeStud = input.nextLine();
                    System.out.println("Introduceti nota obtinuta:\n");
                    int notaMaterie = input.nextInt();
                    catalog.adaugareNota(nameGr, nameMaterie, notaMaterie, numeStud);
                    break;
                case "6" :
                    System.out.println("Introduceti numele studentului:\n");
                    String nameStud = input.nextLine();
                    catalog.afisareNoteStudent(nameStud);
                    break;
                case "7" :
                    System.out.println("Introduceti numele materiei:\n");
                    String numeMat = input.nextLine();
                    System.out.println("Introduceti numele grupei:\n");
                    String grNume = input.nextLine();
                    catalog.afisareNoteMaterieGrupa(numeMat, grNume);
                    break;
                case "8" :
                    System.out.println("Introduceti numele grupei:\n");
                    String grName = input.nextLine();
                    catalog.afisareStudentiGrupa(grName);
                    break;
                case "9" :
                    System.out.println("Introduceti numele grupei:\n");
                    String grupaNume = input.nextLine();
                    catalog.afisareMateriiGrupa(grupaNume);
                    break;
                case "10":
                    catalog.delogare();
                    System.out.println("Introduceti numele de utilizator:\n");
                    String user = input.nextLine();
                    System.out.println("Introduceti numele de utilizator:\n");
                    parola = input.nextLine();
                    catalog.logare(user, parola);
                    break;
                case "11" :
                    on = false;
                    break;
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\n\n\n");
        }
       /* int nota;
        catalog.logare("laurica", "farafrica");
        System.out.println("Scrieti nota pe care o acordati studentului Bob, de la grupa 252, la Istorie:1");
        Scanner scanner = new Scanner(System.in); // pentru verificarea functionarii scrierii in fisiere
        nota = scanner.nextInt();
        catalog.adaugareNota("grupa252", "Istorie", nota, "BOB");
        catalog.afisareNoteStudent("BOB");
        catalog.afisareNoteMaterieGrupa("Matematica","grupa252");
        catalog.afisareStudentiGrupa("grupa252");
        catalog.afisareMateriiGrupa("grupa252");



        catalog.delogare();*/

        //catalog.salveazaInCSV();

        catalog.salveazainDB();

    }
}
