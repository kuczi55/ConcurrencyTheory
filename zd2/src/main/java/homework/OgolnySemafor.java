package homework;

public class OgolnySemafor {
    private Semafor blokadaSekcji;
    private int licznik;
    private Semafor blokadaLicznika;

    public OgolnySemafor() {
        this(0);
    }

    public OgolnySemafor(int licznik) {
        this.licznik = licznik;
        blokadaLicznika = new Semafor();
        blokadaSekcji = new Semafor();
    }

    public void P() {
        blokadaSekcji.P();
        blokadaLicznika.P();
        licznik--;
        if(licznik > 0) {
            blokadaSekcji.V();
        }
        blokadaLicznika.V();
    }

    public void V() {
        blokadaLicznika.P();
        licznik++;
        if(licznik == 1) {
            blokadaSekcji.V();
        }
        blokadaLicznika.V();
    }



    public int pobierzLicznik() {
        int licznik;
        blokadaLicznika.P();
        licznik = this.licznik;
        blokadaLicznika.V();
        return licznik;
    }

    //testowanie semafora ogolnego
    public static void main(String[] args) {
        OgolnySemafor test = new OgolnySemafor();

        Thread[] zmniejszajace = new Thread[5];

        for (int i = 0; i < 5; i++) {
            int idx = i;
            zmniejszajace[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    for (int k = 0; k < 1000; k++)
                        test.P();
                    System.out.println
                            ("watek " + idx + " lacznie dokonal "
                                    + ((j + 1) * 1000) + " operacji opuszczenia semafora");
                }

                System.out.println
                        ("watek " + idx + " zakonczyl swoje dzialanie");
            });

            zmniejszajace[i].start();
        }

        System.out.println("Zostalo utworzonych 5 watkow, " +
                "ktore beda opuszczac semafor i informowac o postepach " +
                "(kazdy co 1000 operacji opuszczenia semafora, " +
                "kazdy watek powinien dokonac 10000 operacji opuszczenia)");

        System.out.println("Oczekiwanie");

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.getMessage();
                System.exit(-1);
            }

            System.out.println("Zostalo utworzonych 10 watkow, ktore " +
                    "lacznie wykonaja 5000 operacji podniesienia semafora");

            Thread[] zwiekszajace = new Thread[10];

            for (int j = 0; j < 10; j++) {
                zwiekszajace[j] = new Thread(() -> {
                    for (int k = 0; k < 500; k++)
                        test.V();
                });
            }

            for (int j = 0; j < 10; j++)
                zwiekszajace[j].start();

            try {
                for (int j = 0; j < 10; j++)
                    zwiekszajace[j].join();
            } catch (InterruptedException ex) {
                ex.getMessage();
                System.exit(-1);
            }
        }

        try {
            for (int i = 0; i < 5; i++)
                zmniejszajace[i].join();
        } catch (InterruptedException ex) {
            ex.getMessage();
            System.exit(-1);
        }

        System.out.println
                ("Ostateczna wartosc semafora: "
                        + test.pobierzLicznik());
    }
}
