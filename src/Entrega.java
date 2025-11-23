import java.util.Objects;

public class Entrega implements Comparable<Entrega> {

    int identificador;
    int categoria;
    int importancia;
    int massa;
    String detalhe;
    int nivelPrioridade;

    public Entrega(int identificador, int categoria, int importancia, int massa, String detalhe) {
        this.identificador = identificador;
        this.categoria = categoria;
        this.importancia = importancia;
        this.massa = massa;
        this.detalhe = detalhe;

        this.nivelPrioridade = (this.importancia * 10) + (this.massa * 2) + (this.categoria * 5);
    }

    @Override
    public int compareTo(Entrega outra) {
        if (this.nivelPrioridade != outra.nivelPrioridade) {
            return Integer.compare(this.nivelPrioridade, outra.nivelPrioridade);
        }

        if (this.importancia != outra.importancia) {
            return Integer.compare(this.importancia, outra.importancia);
        }

        if (this.massa != outra.massa) {
            return Integer.compare(this.massa, outra.massa);
        }

        // Menor ID tem precedência (retorna positivo quando this.id < outra.id)
        return Integer.compare(outra.identificador, this.identificador);
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-4d | %-8d | %-4d | %-9d | %-20s |",
                identificador, categoria, importancia, massa, nivelPrioridade, detalhe);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrega entrega = (Entrega) o;
        return identificador == entrega.identificador;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }
}
