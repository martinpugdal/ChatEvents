package dk.martinersej.chatevents.events;

import dk.martinersej.chatevents.ChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MathEvent extends BukkitRunnable implements IEvent {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private BukkitTask cooldown;
    private String mathExpression;
    private String mathResult;

    public MathEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void newSolution() {
        String expression = generateExpression(Level.values()[random.nextInt(Level.values().length)]);
        String result = calculate(expression);
        saveMathResult(expression, result);
    }

    private String calculate(String expression) {
        String[] split = expression.split(" ");
        List<String> numbers = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        for (String s : split) {
            if (s.matches("[0-9]+")) {
                numbers.add(s);
            } else {
                operators.add(s);
            }
        }

        double result = 0;

        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("*") || operators.get(i).equals("/")) {
                switch (operators.get(i)) {
                    case "*":
                        if (numbers.size() >= 2) {
                            result = Double.parseDouble(numbers.get(i)) * Double.parseDouble(numbers.get(i+1));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i + 1);
                        } else {
                            result = Double.parseDouble(numbers.get(i));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i);
                        }
                        break;
                    case "/":
                        if (numbers.size() >= 2) {
                            result = Double.parseDouble(numbers.get(i)) / Double.parseDouble(numbers.get(i+1));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i + 1);
                        } else {
                            result = Double.parseDouble(numbers.get(i));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i);
                        }
                        break;
                }
                operators.remove(i);
                i--;
            }
        }

        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("+") || operators.get(i).equals("-")) {
                switch (operators.get(i)) {
                    case "+":
                        if (numbers.size() >= 2) {
                            result = Double.parseDouble(numbers.get(i)) + Double.parseDouble(numbers.get(i+1));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i + 1);
                        } else {
                            result = Double.parseDouble(numbers.get(i));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i);
                        }
                        break;
                    case "-":
                        if (numbers.size() >= 2) {
                            result = Double.parseDouble(numbers.get(i)) - Double.parseDouble(numbers.get(i+1));
                            numbers.set(i, String.valueOf(result));
                            numbers.remove(i + 1);
                        } else {
                            result = Double.parseDouble(numbers.get(i));
                            numbers.remove(i);
                        }
                        break;
                }
                operators.remove(i);
                i--;
            }
        }

        if (result % 1 == 0) {
            return String.valueOf((int) result);
        } else {
            return String.format("%.2f", result);
        }
    }

    private String generateExpression(Level level) {
        List<CharSequence> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));
        int numberOfNumbers;
        StringBuilder expression = new StringBuilder();
        switch (level) {
            case EASY:
                numberOfNumbers = random.nextInt(2) + 2;
                generateOperator(operators, 2, numberOfNumbers, expression);
                return expression.toString();
            case MEDIUM:
                numberOfNumbers = random.nextInt(2) + 2;
                generateOperator(operators, 3, numberOfNumbers, expression);
                return expression.toString();
            case HARD:
                numberOfNumbers = random.nextInt(3) + 2;
                generateOperator(operators, 4, numberOfNumbers, expression);
                return expression.toString();
        }
        return "";
    }

    private void generateOperator(List<CharSequence> operators, int operatorsAmount, int numberOfNumbers, StringBuilder expression) {
        for (int i = 0; i < numberOfNumbers; i++) {

            CharSequence operator = operators.get(random.nextInt(operatorsAmount));

            // number is generated here and added.
            expression.append(random.nextInt(15) + 1);

            // add an operator here, if its "+", "-", "*" or "/"
            if (i < numberOfNumbers - 1) {
                expression.append(" ");
                expression.append(operator);
                expression.append(" ");
            }
        }
    }

    private void saveMathResult(String expression, String result) {
        this.mathExpression = expression;
        this.mathResult = result;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase(mathResult)) {
            winnerFound(event.getPlayer());
            startNextRound();
        }
    }

    public void winnerFound(Player player) {
        cooldown.cancel();
        plugin.getServer().broadcastMessage(player.getName() + " gættede tallet!");
    }

    @Override
    public void run() {
        start();
        plugin.getServer().broadcastMessage("Regn ud: " + mathExpression);
        cooldown = startCountdown();
    }

    public BukkitTask startCountdown() {
        return new BukkitRunnable() {
            int time = 10;

            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                    sendNoOneGuessed();
                    startNextRound();
                    return;
                }
                time--;
            }

        }.runTaskTimer(plugin, 0, 20);
    }

    public void startNextRound() {
        try {
            cancel();
        } catch (IllegalStateException ignored) {
        }
        ChatEvent.get().getMathTask().c().runTaskLaterAsynchronously(plugin, 20); // 5 minutes
    }

    public BukkitRunnable c() {
        return new MathEvent(plugin);
    }

    public void sendNoOneGuessed() {
        plugin.getServer().broadcastMessage("Ingen gættede svaret!");
        plugin.getServer().broadcastMessage("svaret var: " + mathResult);
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        newSolution();
    }

    public void stop() {
        plugin.getServer().broadcastMessage("MathEvent stopped");
        cancel();
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        HandlerList.unregisterAll(this);
        if (cooldown != null) {
            cooldown.cancel();
        }
        super.cancel();
    }

    public enum Level {
        EASY,
        MEDIUM,
        HARD
    }
}