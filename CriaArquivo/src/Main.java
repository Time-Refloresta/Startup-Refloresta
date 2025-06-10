import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static Formatter arqSaida;
    private static Scanner arqEnt;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int escolha;
        boolean programaAtivo = true;

        abreArqEscrita();
        arqSaida.format("%-12s %-35s %-8s %-6s %-10s %-6s %-20s%n",
                "Nome", "Email", "ID", "Senha", "Valor", "Cotas", "Brinde");
        arqSaida.flush();

        while (programaAtivo) {
            System.out.println("\n--- Menu Inicial ---");
            System.out.println("1 - Cadastrar novo usuário ou investir");
            System.out.println("2 - Fazer login");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            escolha = sc.nextInt();

            if (escolha == 1) {
                adicionaRegistro();
            } else if (escolha == 2) {
                int idUsuario, senha2;
                boolean loginRealizado = false;
                String nome = "", email = "", brinde = "-";
                double valor = 0;
                int cotas = 0;

                System.out.print("Digite seu ID: ");
                idUsuario = sc.nextInt();
                System.out.print("Digite sua senha: ");
                senha2 = sc.nextInt();

                boolean erroNaLeitura = false;
                try (Scanner leitor = new Scanner(new File("clientes.txt"))) {
                    if (leitor.hasNextLine()) leitor.nextLine(); // pula cabeçalho
                    while (leitor.hasNext()) {
                        String n = leitor.next();
                        String e = leitor.next();
                        int idArq = leitor.nextInt();
                        int sArq = leitor.nextInt();
                        double vArq = leitor.nextDouble();
                        int cArq = leitor.nextInt();
                        String bArq = leitor.next();

                        if (idUsuario == idArq && senha2 == sArq) {
                            loginRealizado = true;
                            nome = n;
                            email = e;
                            valor = vArq;
                            cotas = cArq;
                            brinde = bArq;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao acessar arquivo.");
                    erroNaLeitura = true;
                }

                if (!erroNaLeitura) {
                    if (!loginRealizado) {
                        System.out.println("ID ou senha incorretos.");
                    } else {
                        System.out.println("Login realizado com sucesso! Bem-vindo, " + nome);
                        System.out.printf("Saldo: R$ %.2f | Cotas: %d | Brinde: %s%n", valor, cotas, brinde);

                        if (valor > 0) {
                            System.out.println("Cada cota equivale a uma árvore plantada.");
                            System.out.print("Deseja plantar agora? (1-Sim / 2-Não): ");
                            int plantar = sc.nextInt();
                            if (plantar == 1) {
                                System.out.println("Sucesso! " + cotas + " árvores foram plantadas.");
                                valor = 0.0;
                                cotas = 0;
                            }
                            else {
                                System.out.println("Tudo bem! Você pode plantar depois.");
                            }
                            arqSaida.format("%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                                    nome, email, idUsuario, senha2, valor, cotas, brinde);
                            arqSaida.flush();
                        }
                        else {
                            System.out.println("Você ainda não possui investimento.");
                        }
                        if(!brinde.equals("-")) {
                            System.out.print("Deseja resgatar seu brinde agora? (1-Sim / 2-Não): ");
                            int resgatar = sc.nextInt();
                            while (resgatar != 1 && resgatar != 2) {
                                System.out.print("Opção inválida. Digite 1 ou 2: ");
                                resgatar = sc.nextInt();
                            }
                            if (resgatar == 1) {
                                System.out.println("Brinde resgatado. Obrigado!");
                                brinde = "-";
                                arqSaida.format("%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                                        nome, email, idUsuario, senha2, valor, cotas, brinde);
                                arqSaida.flush();
                            } else {
                                System.out.println("Você pode resgatar quando quiser.");
                            }
                        }
                    }
                }

            } else if (escolha == 0) {
                fechaArqEsc();
                abreArqLeitura();
                leRegistro();
                fechaArqLeit();
                programaAtivo = false;
                System.out.println("Obrigado.");
            } else {
                System.out.println("Opção inválida.");
            }
        }

        sc.close();
    }

    public static void abreArqEscrita() {
        try {
            arqSaida = new Formatter("clientes.txt");
        } catch (FileNotFoundException | SecurityException e) {
            System.err.println("Erro ao abrir o arquivo para escrita.");
            System.exit(1);
        }
    }

    public static void abreArqLeitura() {
        try {
            arqEnt = new Scanner(new File("clientes.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao abrir o arquivo para leitura.");
            System.exit(1);
        }
    }
    public static void adicionaRegistro() {
        Scanner input = new Scanner(System.in);
        String nome = "", email = "", brindeGanho = "-";
        int id = 0, senha = 0, cadastro, investimento;
        double valor = 0;
        int cotas = 0;
        boolean usuarioEncontrado = false;
        double antigoValor = 0;
        int antigasCotas = 0;

        String[] brindes = {
                "Onca-Pintada", "Macaco-Aranha", "Boto-Cor-de-Rosa",
                "Mico-Leao-Dourado", "Ararajuba", "Capivara",
                "Jacaré-Açu", "Lobo-Guará", "Sucuri", "Pirarucu"
        };

        System.out.print("Já realizou o cadastro? (1-Sim / 2-Não): ");
        cadastro = input.nextInt();
        while (cadastro != 1 && cadastro != 2) {
            System.out.print("Opção inválida. Digite 1 ou 2: ");
            cadastro = input.nextInt();
        }

        if (cadastro == 1) {
            System.out.print("Digite seu ID de usuário: ");
            id = input.nextInt();
            System.out.print("Digite sua senha: ");
            senha = input.nextInt();

            boolean erroNaLeitura = false;
            try (Scanner leitor = new Scanner(new File("clientes.txt"))) {
                if (leitor.hasNextLine()) leitor.nextLine(); 
                while (leitor.hasNext()) {
                    String n = leitor.next();
                    String e = leitor.next();
                    int idArq = leitor.nextInt();
                    int sArq = leitor.nextInt();
                    double vArq = leitor.nextDouble();
                    int cArq = leitor.nextInt();
                    String bArq = leitor.next();

                    if (id == idArq && senha == sArq) {
                        usuarioEncontrado = true;
                        nome = n;
                        email = e;
                        antigoValor = vArq;
                        antigasCotas = cArq;
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao acessar arquivo de usuários.");
                erroNaLeitura = true;
            }

            if (!erroNaLeitura) {
                if (!usuarioEncontrado) {
                    System.out.println("ID ou senha inválidos.");
                } else {
                    System.out.print("Quanto deseja investir? R$ ");
                    double aporte = input.nextDouble();
                    cotas = (int) (aporte / 50);
                    valor = antigoValor + aporte;
                    cotas += antigasCotas;
                    brindeGanho = brindes[(int) (Math.random() * brindes.length)];

                    arqSaida.format("%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                            nome, email, id, senha, valor, cotas, brindeGanho);
                    arqSaida.flush();
                }
            }

        } else {  // cadastro novo
            System.out.print("Digite o primeiro nome: ");
            nome = input.next();
            System.out.print("Digite o e-mail: ");
            email = input.next();
            System.out.print("Digite o ID de usuário: ");
            id = input.nextInt();
            System.out.print("Digite a senha: ");
            senha = input.nextInt();
            System.out.print("Deseja investir agora? (1-Sim / 2-Não): ");
            investimento = input.nextInt();
            while (investimento != 1 && investimento != 2) {
                System.out.print("Opção inválida. Digite 1 ou 2: ");
                investimento = input.nextInt();
            }

            if (investimento == 1) {
                System.out.print("Quanto deseja investir? R$ ");
                valor = input.nextDouble();
                cotas = (int) (valor / 50);
                brindeGanho = brindes[(int) (Math.random() * brindes.length)];
            } else {
                System.out.println("Cadastro realizado sem investimento.");
            }

            arqSaida.format("%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                    nome, email, id, senha, valor, cotas, brindeGanho);
            arqSaida.flush();
        }
    }
    public static void leRegistro() {
        System.out.printf("\n%-12s %-35s %-8s %-6s %-10s %-6s %-20s%n",
                "Nome", "Email", "ID", "Senha", "Valor", "Cotas", "Brinde");
        try {
            if (arqEnt.hasNextLine()) arqEnt.nextLine(); 
            while (arqEnt.hasNext()) {
                String nome = arqEnt.next();
                String email = arqEnt.next();
                int id = arqEnt.nextInt();
                int senha = arqEnt.nextInt();
                double valor = arqEnt.nextDouble();
                int cotas = arqEnt.nextInt();
                String brinde = arqEnt.next();

                System.out.printf("%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                        nome, email, id, senha, valor, cotas, brinde);
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Erro na leitura do arquivo.");
            System.exit(1);
        }
    }
    public static void fechaArqEsc() {
        if (arqSaida != null) {
            arqSaida.close();
        }
    }

    public static void fechaArqLeit() {
        if (arqEnt != null) {
            arqEnt.close();
        }
    }
}
