package ro.fmi.cti;

import catalog.Catalog;
import catalog.Grupa;
import catalog.Utilizator;

public class Main {

    private static Catalog catalog = new Catalog();

    public static void main(String[] args) {
        catalog.logare("laurica", "farafrica");
        catalog.adaugareUtilizator("BOB", "bobel", "bobicasielfarafrica", Utilizator.Drepturi.STUDENT);
        catalog.adaugareUtilizator("Ion", "ionica", "ionicasieltotfarafrica", Utilizator.Drepturi.PROFESOR);
        Grupa g252 = new Grupa("252");
        catalog.adaudareGrupa(g252);
        catalog.adaugareStudentGrupa("252", "BOB");
        catalog.adugareMaterieGrupa("EAP","Ion", "252");
        catalog.adaugareNota("252","EAP",10, "BOB");
        catalog.afisareNoteStudent("BOB");
        catalog.afisareNoteMaterieGrupa("EAP","252");
        catalog.afisareStudentiGrupa("252");
        catalog.afisareMateriiGrupa("252");
        catalog.delogare();
    }
}
