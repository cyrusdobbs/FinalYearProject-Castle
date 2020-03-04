package util.sqlexporter;


public class Entry {

    private String[] hand;
    private String[] castleFu;
    private int castleFdSize;

    private int opHandSize;
    private String[] opCastleFu;
    private int opCastleFdSize;

    private String top;
    private boolean deckEmpty;
    private boolean won;

    public Entry() {
    }

    public String getSqlStatement() {
        return "(" + arrayToString(hand) + ", " + arrayToString(castleFu) + ", " + castleFdSize + ", " + opHandSize + ", " + arrayToString(opCastleFu) + ", " + opCastleFdSize + ", " + "'" + top + "'" + ", " + deckEmpty + ", " + won + ")";
    }

    private String arrayToString(String[] array) {
        if (array.length == 0) {
            return "''";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'");
        for (String st : array) {
            stringBuilder.append(st).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("'");
        return stringBuilder.toString();
    }

    public void setHand(String[] hand) {
        this.hand = hand;
    }

    public void setCastleFu(String[] castleFu) {
        this.castleFu = castleFu;
    }

    public void setCastleFdSize(int castleFdSize) {
        this.castleFdSize = castleFdSize;
    }

    public void setOpHandSize(int opHandSize) {
        this.opHandSize = opHandSize;
    }

    public void setOpCastleFu(String[] opCastleFu) {
        this.opCastleFu = opCastleFu;
    }

    public void setOpCastleFdSize(int opCastleFdSize) {
        this.opCastleFdSize = opCastleFdSize;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public void setDeckEmpty(boolean deckEmpty) {
        this.deckEmpty = deckEmpty;
    }

    public void setWon(boolean won) {
        this.won = won;
    }
}
