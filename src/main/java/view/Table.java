package view;

import model.PlayerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    private List<List<String>> table;

    Table(List<List<String>> table) {
        this.table = table;
    }

    void addPlayer(PlayerModel player) {
        addCol(player.getName(), player.getHand().toString(), player.getFaceUpCastleCards().toString(), player.getFaceDownCastleCards().toString());
    }

    void addHumanPlayer(PlayerModel player) {
        addCol(player.getName(), player.getHand().toString(), player.getFaceUpCastleCards().toString(), player.getFaceDownCastleCards().toHiddenString());
    }

    void addHiddenPlayer(PlayerModel player) {
        addCol(player.getName(), player.getHand().toHiddenString(), player.getFaceUpCastleCards().toString(), player.getFaceDownCastleCards().toHiddenString());
    }

    void addCol(String el1, String el2, String el3, String el4) {
        List<String> col = Arrays.asList(el1, el2, el3, el4);
        table.add(col);
    }

    public String toString(int spacing) {
        List<Integer> maxLengths = findMaxLengths();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.get(0).size(); i++) {
            for (int j = 0; j < table.size(); j++) {
                String currentValue = table.get(j).get(i);
                sb.append(currentValue);
                for (int k = 0; k < (maxLengths.get(j) - currentValue.length() + spacing); k++) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    private List<Integer> findMaxLengths() {
        List<Integer> maxLengths = new ArrayList<>();
        for (List<String> row : table) {
            int maxLength = 0;
            for (String value : row) {
                if (value.length() > maxLength) {
                    maxLength = value.length();
                }
            }
            maxLengths.add(maxLength);
        }
        return maxLengths;
    }
}