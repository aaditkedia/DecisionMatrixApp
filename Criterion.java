public class Criterion {
    String name;
    int weight;

    public Criterion(String name, int weight) {
        this.name = name;
        this.weight = weight;
        System.out.println("[DEBUG] Criterion Object Created: " + name + " (Weight: " + weight + ")");
    }
}