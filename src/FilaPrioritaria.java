public class FilaPrioritaria {

    private Entrega[] fila;
    private int tam;
    private int lim;

    public FilaPrioritaria(int limiteInicial) {
        if (limiteInicial <= 0) {
            throw new IllegalArgumentException("Ao iniciar o limite deve ser positivo.");
        }
        this.lim = limiteInicial;
        this.fila = new Entrega[lim + 1];
        this.tam = 0;
    }

    public int obterTam() {
        return this.tam;
    }

    public boolean eVazia() {
        return this.tam == 0;
    }

    private void expCapac() {
        if (tam == lim) {
            int novoLim = lim * 2;
            Entrega[] novaFila = new Entrega[novoLim + 1];

            for (int i = 1; i <= tam; i++) {
                novaFila[i] = fila[i];
            }
            this.fila = novaFila;
            this.lim = novoLim;
        }
    }

    private void permutar(int i, int j) {
        Entrega tmp = fila[i];
        fila[i] = fila[j];
        fila[j] = tmp;
    }

    private void elevar(int k) {
        while (k > 1 && fila[k/2].compareTo(fila[k]) < 0) {
            permutar(k/2, k);
            k = k/2;
        }
    }

    public void inserir(Entrega entrega) {
        expCapac();

        tam++;
        fila[tam] = entrega;

        elevar(tam);
    }

    private void abaixar(int k) {
        while (k * 2 <= tam) {
            int j = k * 2;

            if (j < tam && fila[j].compareTo(fila[j+1]) < 0) {
                j++;
            }

            if (fila[k].compareTo(fila[j]) >= 0) {
                break;
            }

            permutar(k, j);
            k = j;
        }
    }

    public Entrega retirarMax() {
        if (eVazia()) {
            System.out.println("Fila vazia. Nenhuma entrega para retirar.");
            return null;
        }

        Entrega max = fila[1];

        permutar(1, tam);
        tam--;

        abaixar(1);

        fila[tam + 1] = null;

        return max;
    }

    public Entrega verTopo() {
        if (eVazia()) {
            return null;
        }
        return fila[1];
    }
}
