import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
public class Leaderboard implements Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private Map<String, PlayerStats> leaderboard;

    public Leaderboard() {
        leaderboard = new HashMap<>();
    }

    public void recordWin(String playerName) {
        PlayerStats stats = leaderboard.getOrDefault(playerName, new PlayerStats());
        stats.incrementWins();
        leaderboard.put(playerName, stats);
    }

    public void recordLoss(String playerName) {
        PlayerStats stats = leaderboard.getOrDefault(playerName, new PlayerStats());
        stats.incrementLosses();
        leaderboard.put(playerName, stats);
    }

    public String getLeaderboard() {
        StringBuilder sb = new StringBuilder();
        sb.append("Leaderboard:\n");
        for (Map.Entry<String, PlayerStats> entry : leaderboard.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public void saveLeaderboard(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLeaderboard(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            leaderboard = (Map<String, PlayerStats>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class PlayerStats implements Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private int wins;
    private int losses;

    public PlayerStats() {
        wins = 0;
        losses = 0;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    @Override
    public String toString() {
        return "Wins: " + wins + ", Losses: " + losses;
    }
}
