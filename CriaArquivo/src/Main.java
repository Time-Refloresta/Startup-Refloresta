import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static Formatter arqSaida; // para escrever no arquivo
    private static Scanner arqEnt;      // para ler do arquivo

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int escolha;
        boolean programaAtivo = true;

        abreArqEscrita();
        escreverCabecalho();

        while (programaAtivo) {
            exibirMenu();
            escolha = sc.nextInt();

            switch (escolha) {
                case 1:
                    adicionaRegistro();
                    break;
                case 2:
                    realizarLogin();
                    break;
                case 0:
                    fecharESalvar();
                    programaAtivo = false;
                    System.out.println("Obrigado.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente de novo.");
            }
        }

        sc.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- Menu Inicial ---");
        System.out.println("1 - Cadastrar novo usuário ou investir");
        System.out.println("2 - Fazer login");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private static void escreverCabecalho() {
        arqSaida.format(
                "%-12s %-35s %-8s %-6s %-10s %-6s %-20s%n",
                "Nome", "Email", "ID", "Senha", "Valor", "Cotas", "Brinde"
        );
        arqSaida.flush();
    }

    private static void fecharESalvar() {
        fechaArqEsc();
        abreArqLeitura();
        leRegistro();
        fechaArqLeit();
    }

    private static void abrirArquivoEscrita() throws FileNotFoundException, SecurityException {
        arqSaida = new Formatter("clientes.txt");
    }

    public static void abreArqEscrita() {
        try {
            abrirArquivoEscrita();
        } catch (Exception e) {
            System.err.println("Erro ao abrir o arquivo para escrita. Encerrando...");
            System.exit(1);
        }
    }

    public static void abreArqLeitura() {
        try {
            arqEnt = new Scanner(new File("clientes.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Erro na abertura do arquivo para leitura.");
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

    public static void adicionaRegistro() {
        Scanner input = new Scanner(System.in);
        String nome = "", email = "";
        String brindeGanho = "-";
        int id = 0, senha = 0;
        double valor = 0;
        int cotas = 0;
        int cadastro;
        boolean usuarioEncontrado = false;
        double antigoValor = 0;
        int antigasCotas = 0;

        String[] brindes = {
                "Onca-Pintada","Macaco-Aranha","Boto-Cor-de-Rosa",
                "Mico-Leao-Dourado","Ararajuba","Capivara",
                "Jacaré-Açu","Lobo-Guará","Sucuri","Pirarucu"
        };

        System.out.print("Já realizou o cadastro? (1-Sim / 2-Não): ");
        cadastro = input.nextInt();
        while (cadastro != 1 && cadastro != 2) {
            System.out.print("Opção inválida. Digite 1 ou 2: ");
            cadastro = input.nextInt();
        }

        if (cadastro == 1) {
            // autenticar usuário existente
            System.out.print("Digite seu ID de usuário: ");
            id = input.nextInt();
            System.out.print("Digite sua senha: ");
            senha = input.nextInt();

            try (Scanner leitor = new Scanner(new File("clientes.txt"))) {
                leitor.nextLine(); // pula cabeçalho
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
                return;
            }

            if (!usuarioEncontrado) {
                System.out.println("ID ou senha inválidos. Cadastro não localizado.");
                return;
            }

            System.out.print("Quanto deseja investir? R$ ");
            double aporte = input.nextDouble();
            cotas = (int) (aporte / 50);
            valor = antigoValor + aporte;
            antigasCotas = antigasCotas; // corrige typo
            cotas += antigasCotas;
            brindeGanho = brindes[(int) (Math.random() * brindes.length)];

        } else {
            // novo cadastro
            System.out.print("Digite o primeiro nome: ");
            nome = input.next();
            System.out.print("Digite o e-mail: ");
            email = input.next();
            System.out.print("Digite o ID de usuário: ");
            id = input.nextInt();
            System.out.print("Digite a senha: ");
            senha = input.nextInt();
            System.out.print("Deseja investir agora? (1-Sim / 2-Não): ");
            int inv = input.nextInt();
            while (inv != 1 && inv != 2) {
                System.out.print("Opção inválida. Digite 1 ou 2: ");
                inv = input.nextInt();
            }
            if (inv == 1) {
                System.out.print("Quanto deseja investir? R$ ");
                valor = input.nextDouble();
                cotas = (int) (valor / 50);
                brindeGanho = brindes[(int) (Math.random() * brindes.length)];
            } else {
                System.out.println("Cadastro realizado sem investimento.");
            }
        }

        // grava registro (novo ou acumulado)
        arqSaida.format(
                "%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                nome, email, id, senha, valor, cotas, brindeGanho
        );
        arqSaida.flush();
    }

    public static void realizarLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite seu id de usuário: ");
        int idUsuario = sc.nextInt();
        System.out.print("Digite sua senha: ");
        int senha2 = sc.nextInt();

        boolean loginRealizado = false;
        String nome = "", email = "", brinde = "-";
        double valor = 0;
        int cotas = 0;

        try (Scanner leitor = new Scanner(new File("clientes.txt"))) {
            leitor.nextLine();
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
        } catch (Exception ex) {
            System.out.println("Arquivo de usuários não encontrado.");
            return;
        }

        if (!loginRealizado) {
            System.out.println("ID ou senha incorretos. Tente novamente.");
            return;
        }

        System.out.println("\nLogin realizado com sucesso! Bem-vindo, " + nome);
        System.out.printf("Seu saldo atual: R$ %.2f e %d cotas (brinde: %s)%n", valor, cotas, brinde);

        if (valor > 0) {
            System.out.println("Cada cota equivale a uma árvore plantada.");
            System.out.print("Deseja plantar agora? (1-Sim / 2-Não): ");
            int plantar = sc.nextInt();
            if (plantar == 1) {
                System.out.println("Sucesso! " + cotas + " árvores foram plantadas.");
                System.out.print("Deseja resgatar agora o seu brinde? (1-Sim / 2-Não): ");
                int resgate = sc.nextInt();
                while (resgate != 1 && resgate != 2) {
                    System.out.print("Opção inválida. Digite 1 ou 2: ");
                    resgate = sc.nextInt();
                }
                System.out.println(resgate == 1
                        ? "Brinde Resgatado! Muito obrigado!!!"
                        : "Fique à vontade para resgatar quando desejar.");

                // grava registro zerado
                arqSaida.format(
                        "%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                        nome, email, idUsuario, senha2, 0.0, 0, "-"
                );
                arqSaida.flush();
            } else {
                System.out.println("Tudo bem! Você poderá plantar quando quiser.");
            }
        } else {
            System.out.println("Você ainda não tem investimento. Faça um aporte!");
        }
    }

    public static void leRegistro() {
        System.out.printf("\n%-12s %-35s %-8s %-6s %-10s %-6s %-20s%n",
                "Nome", "Email", "ID", "Senha", "Valor", "Cotas", "Brinde"
        );
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

                System.out.printf(
                        "%-12s %-35s %-8d %-6d %-10.2f %-6d %-20s%n",
                        nome, email, id, senha, valor, cotas, brinde
                );
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Erro na leitura do arquivo");
            System.exit(1);
        }
    }
}
