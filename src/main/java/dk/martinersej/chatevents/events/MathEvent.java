package dk.martinersej.chatevents.events;

import dk.martinersej.chatevents.ChatEvent;
import dk.martinersej.chatevents.hooks.CoinsHook;
import org.bukkit.Bukkit;
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

    private final Random random = new Random();
    private BukkitTask cooldown;
    private String mathExpression;
    private String mathResult;
    private Level level;

    private void newSolution() {
        level = Level.values()[random.nextInt(Level.values().length)];
        String expression = generateExpression();
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

    private String generateExpression() {
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
        }
    }

    public void winnerFound(Player player) {
        cooldown.cancel();
        Bukkit.getServer().broadcastMessage(player.getName() + " gættede tallet!");
        double randomCoins = Math.random() * 75 + 25 * Math.max(level.ordinal(), 1); // 25-100 * level
        CoinsHook.addCoins(player, randomCoins);
    }

    @Override
    public void run() {
        start();
        Bukkit.getServer().broadcastMessage("Regn ud: " + mathExpression);
        cooldown = startCountdown();
    }

    public BukkitTask startCountdown() {
        return new BukkitRunnable() {
            int time = getCooldownTime();

            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                    sendNoOneGuessed();
                    return;
                }
                time--;
            }

        }.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 0, 20);
    }

    public BukkitRunnable c() {
        return new MathEvent();
    }

    public void sendNoOneGuessed() {
        Bukkit.getServer().broadcastMessage("Ingen gættede svaret!");
        Bukkit.getServer().broadcastMessage("svaret var: " + mathResult);
    }

    public void start() {
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(getClass()));
        newSolution();
    }

    public void stop() {
        Bukkit.getServer().broadcastMessage("MathEvent stopped");
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

    @Override
    public int getCooldownTime() {
        return 10;
    }

    public enum Level {
        EASY,
        MEDIUM,
        HARD
    }
}