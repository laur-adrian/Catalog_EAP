package catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CSVData {
    private static CSVData instanta;

    private final static String numeFisierGrupe = "csv/grupe.csv";
    private final static String numeFisierStudenti = "csv/studenti.csv";
    private final static String numeFisierProfesori = "csv/profesori.csv";
    private final static String numeFisierAdministratori = "csv/administratori.csv";

    private List<Utilizator> utilizatori;

    private CSVData() {

    }

    public static CSVData getInstance() {
        if(instanta == null) {
            instanta = new CSVData();
        }
        return instanta;
    }

    public List<Grupa> citesteGrupe(List<Utilizator> utilizatori){
        this.utilizatori = utilizatori;
        return new ArrayList<>(citeste(Grupa.class));
    }

    public List<Utilizator> citesteUtilizatori() {
        List<Utilizator> utilizatori = new ArrayList<>();

        utilizatori.addAll(citeste(Administrator.class));
        utilizatori.addAll(citeste(Profesor.class));
        utilizatori.addAll(citeste(Student.class));
        return utilizatori;
    }

    private <T> List<T> citeste(Class<T> clasa) {
        InputStream csv;
        List<T> rezultat = new ArrayList<>();

        if (clasa == Administrator.class) {
            csv = getFileFromResourceAsStream(numeFisierAdministratori);
            InputStreamReader rdr = new InputStreamReader(csv);
            BufferedReader reader = new BufferedReader(rdr);

            try {
                while (reader.ready()){
                    String rand = reader.readLine();
                    String[] celule = rand.split(",");
                    Administrator a = new Administrator(celule[0], celule[1], celule[2]);
                    rezultat.add((T) a);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (clasa == Profesor.class) {
            csv = getFileFromResourceAsStream(numeFisierProfesori);
            InputStreamReader rdr = new InputStreamReader(csv);
            BufferedReader reader = new BufferedReader(rdr);

            try {
                while (reader.ready()){
                    String rand = reader.readLine();
                    String[] celule = rand.split(",");
                    Profesor p = new Profesor(celule[0], celule[1], celule[2]);
                    rezultat.add((T) p);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (clasa == Student.class) {
            csv = getFileFromResourceAsStream(numeFisierStudenti);
            InputStreamReader rdr = new InputStreamReader(csv);
            BufferedReader reader = new BufferedReader(rdr);
            try {
                if (reader.ready()) { // verificare fisier gol sau nu a fost gasit
                    String rand = reader.readLine();

                    while (reader.ready()) {
                        String[] celule = rand.split(",");
                        Student s = new Student(celule[0], celule[1], celule[2]);

                        if (reader.ready()) {
                            rand = reader.readLine();
                            celule = rand.split(",");
                            while (celule.length == 2) {
                                s.adaugareNota(celule[0], Integer.parseInt(celule[1]));
                                if (reader.ready()) {
                                    rand = reader.readLine();
                                    celule = rand.split(",");
                                } else {
                                    break;
                                }
                            }
                            rezultat.add((T) s);
                            if (celule.length == 3) {
                                continue;
                            }
                            rand = reader.readLine();
                        } else {
                            rezultat.add((T) s);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (clasa == Grupa.class){
            csv = getFileFromResourceAsStream(numeFisierGrupe);
            InputStreamReader rdr = new InputStreamReader(csv);
            BufferedReader reader = new BufferedReader(rdr);
            try {
                if (reader.ready()) {
                    String rand = reader.readLine();
                    bigwhile:
                    while (reader.ready()) {
                        Grupa g = new Grupa(rand);
                        if (reader.ready()) {
                            String randStudenti = reader.readLine();
                            if (!randStudenti.startsWith("grupa")) {
                                String[] listaStudenti = randStudenti.split(","); // aici avem doar numele de utilizatori

                                for (String s : listaStudenti) {
                                    Student student = (Student) utilizatori.stream().
                                            filter(st -> s.equals(st.getNumeUtilizator())).
                                            findFirst().orElse(null); // cautam in lista de utilizatori pe cel cu usernameul lui s

                                    g.adaugareStudent(student);
                                }

                                while (reader.ready()) {
                                    rand = reader.readLine();
                                    if (!rand.startsWith("grupa") && !rand.equals("")) {
                                        String[] split = rand.split(",");

                                        for (Utilizator u : utilizatori) {
                                            if (u.getNumeUtilizator().equals(split[1])) {
                                                g.adaugareMaterie(split[0], (Profesor) u);
                                                break;
                                            }
                                        }
                                    } else {
                                        rezultat.add((T) g);
                                        continue bigwhile;
                                    }
                                }
                                rezultat.add((T) g);
                                rand = reader.readLine();
                            } else {
                                rand = randStudenti;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (T o : rezultat) {
            System.out.println(o.toString());
        }

        return rezultat;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("Nu a fost gasit fisierul " + fileName);
        } else {
            return inputStream;
        }
    }

}
