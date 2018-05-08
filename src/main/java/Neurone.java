public class Neurone {

    private static int index = 1;
    private int id;

    public Neurone() {
        this.id = Neurone.index++;
    }

    public static void resetIndex() {
        Neurone.index = 1;
    }

    @Override
    public String toString() {
        return "Neurone: " + Integer.toString(id);
    }
}
