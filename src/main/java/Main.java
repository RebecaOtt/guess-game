import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
        static int[]  scoreGuard = new int[10];
        static String[]  difficultyGuard = new String[10];
        static int[] bestScore = new int[4];
        static int totalGames = 0;
        static int highestEasyScore = 0;
        static int highestAverageScore = 0;
        static int highestHardScore = 0;
        static int highestScoreSequence = 0;

    public static void main(String[] args) {
        int userNumber = 0;
        int points = 150;
        int attemptCost = 2;
        int option;
        String[] levels = {"Fácil", "Médio", "Difícil", "Modo sequência"};
        int[] levelNumberLimit = {50, 100, 200, 250};
        int[] limitAttemptsLevel = {10, 7, 5, 10};
        int[] basePointsLevel = {100, 200, 300, 400};
        int[] secretNumberSequence = new int[3];
        int[] userNumberSequence = new int[3];

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao jogo de adivinhação!!");

        do {
            System.out.println("Menu principal:");
            System.out.println("Escolha a opção que desejar:");
            System.out.println("1- Iniciar novo jogo");
            System.out.println("2- Ver regras");
            System.out.println("3- Ver histórico de pontuação");
            System.out.println("4- Sair");
            option = scanner.nextInt();

            int chosenLevel;
            if (option == 1) {
                System.out.println("Ótima opção!");
                System.out.println("Escolha o nível: 0- Fácil, 1- Médio, 2- Difícil, 3- Modo sequencia");
                chosenLevel = scanner.nextInt();

                int attempts = 0;
                if (chosenLevel < 0 || chosenLevel > 3) {
                    System.out.println("Nível inválido, Tente novamente");
                } else if (points < basePointsLevel[chosenLevel]) {
                    System.out.printf("Ops! Você só pode acessar o nivel: %s  com no minimo: %d pontos \n", levels[chosenLevel], basePointsLevel[chosenLevel]);
                } else {
                    Random random = new Random();
                    int MaxLimitNumbers = levelNumberLimit[chosenLevel];
                    int secretNumber = random.nextInt(MaxLimitNumbers);

                    if (chosenLevel == 3) {
                        for (int i = 0; i < secretNumberSequence.length; i++) {
                            secretNumberSequence[i] = random.nextInt(MaxLimitNumbers);
                            System.out.println(secretNumberSequence[i]);
                        }
                    }

                    int attemptsByLevel = limitAttemptsLevel[chosenLevel];
                    System.out.printf("Nivel: %s ", levels[chosenLevel]);
                    boolean correctEverything;

                    do {
                        System.out.println(secretNumber);
                        attempts++;

                        if (chosenLevel == 3) {
                            for (int i = 0; i < secretNumberSequence.length; i++) {
                                System.out.printf("Digite o %dº valor da sequência, sendo um número entre 1 e %d: \n", i + 1, MaxLimitNumbers);
                                userNumber = scanner.nextInt();
                                userNumberSequence[i] = userNumber;
                            }
                            correctEverything = Arrays.equals(secretNumberSequence, userNumberSequence);
                        } else {
                            System.out.printf("Digite um número entre 1 e %d: \n", MaxLimitNumbers);
                            userNumber = scanner.nextInt();
                            correctEverything = (secretNumber == userNumber);
                        }

                        if (!correctEverything) {
                            System.out.println("Você errou, quer uma dica? Sistema de dicas | 0- Pariedade 1- Intervalo 2- Proximidade 3- Sem Dicas");
                            int chosenTipSystem = scanner.nextInt();
                            if (chosenTipSystem != 3) {
                                if (chosenLevel == 3){
                                    points = useTipsSequence(chosenTipSystem, secretNumberSequence, userNumberSequence, MaxLimitNumbers, points);
                                } else {
                                    points = useSystemTips(chosenTipSystem, userNumber, secretNumber, MaxLimitNumbers, points);
                                }
                            }
                        }

                        points -= attemptCost;
                    } while (attempts < attemptsByLevel && !correctEverything);

                    points = returnResult(correctEverything, points, attempts, attemptsByLevel);

                    if (chosenLevel <= 2) {
                        System.out.printf("Numero secreto %d\n", secretNumber);
                    } else {
                        for (int i = 0; i < secretNumberSequence.length; i++) {
                            System.out.printf("%dº valor da sequência, número secreto: %d \n", i+1, secretNumberSequence[i]);
                        }
                    }

                    saveScore(levels[chosenLevel], points);
                    saveRecordSystem(chosenLevel, points);
                }
            }

            if (option == 2) {
                String ruleOption;

                do {
                    System.out.println("Bem vindo as regras!");
                    System.out.println("Digite A- Níveis de Dificuldade B- Sistema de Pontuação C- Sistema de Dicas D- Voltar");
                    ruleOption = scanner.next();

                    if (ruleOption.equalsIgnoreCase("a")) {
                        System.out.println("Níveis de dificuldade:");
                        System.out.println("Ao iniciar um novo jogo, o jogador pode escolher entre quatro níveis ");
                        System.out.println("Fácil: Adivinhar um número entre 1 e 50, com 10 tentativas");
                        System.out.println("Médio: Adivinhar um número entre 1 e 100, com 7 tentativas");
                        System.out.println("Difícil: Adivinhar um número entre 1 e 200, com 5 tentativas");
                        System.out.println("Modo Sequência: Adivinhar cada número da sequência de 3 números, com 10 tentativas");
                    } else if (ruleOption.equalsIgnoreCase("b")) {
                        System.out.println("Sistema de Pontuação:");
                        System.out.println("Pontuação base por nível:");
                        System.out.println("Fácil (100 pontos)");
                        System.out.println("Médio (200 pontos)");
                        System.out.println("Difícil (300 pontos)");
                        System.out.println("Modo Sequência (400 pontos)");
                        System.out.println("A cada tentativa usada, são descontados pontos. Bônus por conclusão rápida: +50 pontos para cada tentativa não utilizada");
                    } else if (ruleOption.equalsIgnoreCase("c")) {
                        System.out.println("Sistema de Dicas:");
                        System.out.println("Dica de paridade (par/ímpar): -10 pontos");
                        System.out.println("Dica de intervalo (metade superior/inferior): -20 ponto");
                        System.out.println("Dica de proximidade (quente/frio): -15 pontos");
                    } else if (ruleOption.equalsIgnoreCase("d")) {
                        System.out.println("Você voltará para tela incial");
                    } else {
                        System.out.println("Opção inválida, tente novamente!");
                    }
                } while (!ruleOption.equalsIgnoreCase("d"));
            }

            if (option == 3) {
                String historyOption;
                do {
                    System.out.println("Bem vindo ao Histórico de pontuações!");
                    System.out.println("Digite A- Histórico de pontuação B-Melhores pontuações de cada nível C- Voltar");
                    historyOption = scanner.next();

                    if (historyOption.equalsIgnoreCase("a")) {
                        showScore();
                    } else if (historyOption.equalsIgnoreCase("b")) {
                        for (int i = 0; i < bestScore.length; i++) {
                            System.out.printf("Nível: %s, melhores pontuações: %d\n", levels[i], bestScore[i]);
                        }
                    } else if (historyOption.equalsIgnoreCase("c")) {
                        System.out.println("Você voltará para tela incial");
                    } else {
                        System.out.println("Opção inválida, tente novamente!");
                    }
                } while (!historyOption.equalsIgnoreCase("c"));
            }
        } while (option != 4);

        System.out.println("Jogo encerrado, volte novamente!");
    }

    public static int returnResult(boolean correctEverything, int points, int attempts, int attemptsByLevel) {
        if (!correctEverything) {
            System.out.println("Ops! Você chegou perto, tente novamente!");
            System.out.println("seus pontos: " + points);
        } else {
            System.out.println("Parabens!! Você teve uma conclusão rápida!");
            int remainingAttempts = attemptsByLevel - attempts;
            int bonus = remainingAttempts * 50;
            points += bonus;
            System.out.println("seus pontos: " + points);
        }
        return points;
    }

    public static void saveScore(String levelChosenRound, int points) {
        if (totalGames < 10) {
            scoreGuard[totalGames] = points;
            difficultyGuard[totalGames] = levelChosenRound;
            totalGames++;
        } else {
            for (int i = 0; i< 9; i++) {
                scoreGuard[i] = scoreGuard[i+1];
                difficultyGuard[i] = difficultyGuard[i+1];
            }
            scoreGuard[9] = points;
            difficultyGuard[9] = levelChosenRound;
        }
    }

    public static void showScore() {
        System.out.println("Histórico de pontuação:");
        if (totalGames == 0) {
            System.out.println("Sem pontuações registradas, inicie jogando!");
        }
        for (int i = 0; i < totalGames; i++) {
            System.out.printf("jogo %d: %d pontos ,Nível: %s\n", (i + 1),scoreGuard[i], difficultyGuard[i]);
        }
    }

    public static int useSystemTips(int chosenTipSystem, int userNumber, int secretNumber, int MaxLimitNumbers, int points){
        if (chosenTipSystem == 0) {
            System.out.println(secretNumber % 2 == 0 ? "Número secreto é par" : "Número secreto é impar");
            points -= 10;
        } else if (chosenTipSystem == 1) {
            int half = MaxLimitNumbers / 2;
            System.out.println(secretNumber <= half ? "O número secreto está na metade inferior" : "O número secreto está na metade superior");
            points -= 20;
        } else if (chosenTipSystem == 2) {
            int distance = Math.abs(secretNumber - userNumber);
            int hotMargin= MaxLimitNumbers / 10;
            System.out.println(distance <= hotMargin ? "Está quente" : "Está frio");
            points -= 15;
        } else {
            System.out.println("Opção incorreta!");
        }
        return points;
    }

    public static int useTipsSequence(int chosenTipSystem, int[] secretNumberSequence, int[] userNumberSequence , int MaxLimitNumbers, int points){
        Scanner scanner = new Scanner(System.in);

        System.out.printf("Qual posição da sequência você quer usar a dica? (1 a %d)", secretNumberSequence.length);
        int indexPosition = scanner.nextInt() - 1;

        int secretValue = secretNumberSequence[indexPosition];
        int userValue = userNumberSequence[indexPosition];

        if (secretValue == userValue) {
            System.out.println("Você acertou o valor dessa posição");
        } else {
            if (chosenTipSystem == 0) {
                System.out.println(secretValue % 2 == 0 ? "O número é par" : "O número é impar");
                points -= 10;
            } else if (chosenTipSystem == 1) {
                int metade = MaxLimitNumbers / 2;
                System.out.println(secretValue <= metade ? "Está na metade inferior" : "Está na metade superior");
                points -= 20;
            } else if (chosenTipSystem == 2) {
                int margemQuente= MaxLimitNumbers / 10;
                int distancia = Math.abs(secretValue - userValue);
                System.out.println(distancia <= margemQuente ? "Está quente" : "Está frio");
                points -= 15;
            } else {
                System.out.println("Opção incorreta!");
            }
        }
        return points;
    }

    public static void saveRecordSystem(int chosenLevel, int points){
        switch (chosenLevel) {
            case 0:
                if (highestEasyScore <= points) {
                    highestEasyScore = points;
                    bestScore[0] = highestEasyScore;
                }
                break;
            case 1:
                if (highestAverageScore < points) {
                    highestAverageScore = points;
                    bestScore[1] = highestAverageScore;
                }
                break;
            case 2:
                if (highestHardScore < points) {
                    highestHardScore = points;
                    bestScore[2] = highestHardScore;
                }
                break;
            default:
                if (highestScoreSequence < points) {
                    highestScoreSequence = points;
                    bestScore[3] = highestScoreSequence;
                }
        }
    }
}
