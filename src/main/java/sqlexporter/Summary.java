package sqlexporter;

public class Summary {

    private int totalWins;
    private int aiWins;
    private int lowestWins;

    public Summary(int totalWins, int aiWins, int lowestWins) {
        this.totalWins = totalWins;
        this.aiWins = aiWins;
        this.lowestWins = lowestWins;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getAiWins() {
        return aiWins;
    }

    public int getLowestWins() {
        return lowestWins;
    }
}
