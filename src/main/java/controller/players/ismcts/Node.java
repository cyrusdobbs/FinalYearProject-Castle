package controller.players.ismcts;

import moves.CastleMove;

import java.util.*;

public class Node {

    private CastleMove move;
    private Node parentNode;
    private List<Node> childNodes;
    private int wins;
    private int visits;
    private int avails;
    private int playerJustMoved;

    // For root node
    Node() {
        this.move = null;
        this.parentNode = null;
        this.childNodes = new ArrayList<>();
        this.wins = 0;
        this.visits = 0;
        this.avails = 1;
        playerJustMoved = -1;
    }

    // For non-root nodes
    private Node(CastleMove move, Node parentNode, int playerJustMoved) {
        this.move = move;
        this.parentNode = parentNode;
        this.childNodes = new ArrayList<>();
        this.wins = 0;
        this.visits = 0;
        this.avails = 1;
        this.playerJustMoved = playerJustMoved;
    }

    CastleMove getMove() {
        return move;
    }

    Node getParentNode() {
        return parentNode;
    }

    List<Node> getChildNodes() {
        return childNodes;
    }

    public int getWins() {
        return wins;
    }

    int getVisits() {
        return visits;
    }

    List<CastleMove> getUntriedMoves(List<CastleMove> legalMoves) {
        List<CastleMove> triedMoves = new ArrayList<>();
        for (Node childNode : childNodes) {
            triedMoves.add(childNode.getMove());
        }
        List<CastleMove> untriedMoves = new ArrayList<>();
        for (CastleMove castleMove : legalMoves) {
            if (!containsMove(triedMoves, castleMove)) {
                untriedMoves.add(castleMove);
            }
        }
        return untriedMoves;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    Node UCBSelectChild(List<CastleMove> legalMoves, double exploration) {
        List<Node> legalChildNodes = new ArrayList<>();
        for (Node childNode : childNodes) {
            if (containsMove(legalMoves, childNode.getMove())) {
                legalChildNodes.add(childNode);
                childNode.avails++;
            }
        }
        Map<Node, Double> UCBs = new HashMap<>();
        for (Node childNode : legalChildNodes) {
            double rewardOverVisits = (childNode.wins == 0 || childNode.visits == 0) ? 0 : childNode.wins / childNode.visits;
            double UCB = rewardOverVisits + exploration * Math.sqrt(Math.log(childNode.avails) / childNode.visits);
            UCBs.put(childNode, UCB);
        }
        if (UCBs.isEmpty()) {
            return null;
        }
        return Collections.max(UCBs.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    void addChild(CastleMove move, int player) {
        childNodes.add(new Node(move, this, player));
    }

    void update(int winningPlayer) {
        visits += 1;
        if (playerJustMoved == winningPlayer) {
            wins ++;
        }
    }

    private boolean containsMove(List<CastleMove> moves, CastleMove moveToCheck) {
        boolean contains = false;
        for (CastleMove move : moves) {
            if (move.getCardsToString().equals(moveToCheck.getCardsToString())) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    String treeToString(int indent) {
        StringBuilder stringBuilder = indentString(indent).append(toString());
        for (Node childNode : childNodes) {
            stringBuilder.append(childNode.treeToString(indent + 1));
        }
        return stringBuilder.toString();
    }

    private StringBuilder indentString(int indent) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (int i = 0; i < indent; i++) {
            stringBuilder.append("| ");
        }
        return stringBuilder;
    }

    String childrenToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node childNode : childNodes) {
            stringBuilder.append(childNode.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return String.format("W/V/A: %d/%d/%d", wins, visits, avails) + " " + (move != null ? move.toString() : "");
    }
}
