package catalog;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CSVData {
    private static CSVData instanta;

    private final static String numeFisierGrupe = "src/main/csv/grupe.csv";
    private final static String numeFisierStudenti = "src/main/csv/studenti.csv";
    private final static String numeFisierProfesori = "src/main/csv/profesori.csv";
    private final static String numeFisierAdministratori = "src/main/csv/administratori.csv";

    private List<Utilizator> listaUtilizatori;

    private CSVData() {

    }

    public static CSVData getInstance() {
        if(instanta == null) {
            instanta = new CSVData();
        }
        return instanta;
    }

    public List<Grupa> citesteGrupe(List<Utilizator> utilizatori){
        this.listaUtilizatori = utilizatori;
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
                                    Student student = (Student) listaUtilizatori.stream().
                                            filter(st -> s.equals(st.getNumeUtilizator())).
                                            findFirst().orElse(null); // cautam in lista de utilizatori pe cel cu usernameul lui s

                                    g.adaugareStudent(student);
                                }

                                while (reader.ready()) {
                                    rand = reader.readLine();
                                    if (!rand.startsWith("grupa") && !rand.equals("")) {
                                        String[] split = rand.split(",");

                                        for (Utilizator u : listaUtilizatori) {
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

//        for (T o : rezultat) {
//            System.out.println(o.toString());
//        }

        return rezultat;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream == null) {
            throw new IllegalArgumentException("Nu a fost gasit fisierul " + fileName);
        } else {
            return inputStream;
        }
    }

    public void scrieUtilizatoriInCSV(List<Utilizator> utilizatori) {
        try {
            FileWriter fwAdministrator = new FileWriter(numeFisierAdministratori);
            FileWriter fwProfesor = new FileWriter(numeFisierProfesori);
            FileWriter fwStudent = new FileWriter(numeFisierStudenti);

            BufferedWriter csvAdministrator = new BufferedWriter(fwAdministrator);
            BufferedWriter csvProfesor = new BufferedWriter(fwProfesor);
            BufferedWriter csvStudent = new BufferedWriter(fwStudent);


            for (Utilizator u : utilizatori) {
                if (u instanceof Administrator) {
                    csvAdministrator.write(u.getNume() + ',' + u.getNumeUtilizator() + ',' + u.getParola() + '\n');
                } else if (u instanceof Profesor) {
                    csvProfesor.write(u.getNume() + ',' + u.getNumeUtilizator() + ',' + u.getParola() + '\n');
                } else if (u instanceof Student) {
                    csvStudent.write(u.getNume() + ',' + u.getNumeUtilizator() + ',' + u.getParola() + '\n');
                    for (String m : ((Student) u).getNote().keySet()) {
                        csvStudent.write(m + ',' + ((Student) u).getNote().get(m) + '\n');
                    }
                }
            }
            csvAdministrator.close();
            csvStudent.close();
            csvProfesor.close();

            fwAdministrator.close();
            fwProfesor.close();
            fwStudent.close();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void scrieGrupeInCSV(List<Grupa> grupe) {
        try {
            FileWriter fw = new FileWriter(numeFisierGrupe);
            BufferedWriter csv = new BufferedWriter(fw);

            for (Grupa g : grupe) {
                csv.write(g.getNume() + "\n");

                for (int i = 0; i < g.getStudenti().size() - 1; i++) {
                    csv.write(g.getStudenti().get(i).getNumeUtilizator() + ",");
                }
                csv.write(g.getStudenti().get(g.getStudenti().size() - 1).getNumeUtilizator() + "\n");

                for (String m : g.getMaterii().keySet()) {
                    csv.write(m + "," + g.getMaterii().get(m).getNumeUtilizator() + "\n");
                }
            }
            csv.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
