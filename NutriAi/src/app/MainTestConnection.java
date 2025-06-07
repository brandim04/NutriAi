package app;

import model.Usuario;
import model.Paciente;
import model.Nutricionista;
import model.QuestionarioRespostas;
import dao.UsuarioDAO;
import dao.PacienteDAO;
import dao.NutricionistaDAO;
import dao.QuestionarioRespostasDAO;
import java.sql.Connection;
import util.ConnectionFactory;
import java.util.Date; // Necessário se você mantiver o campo Date na classe QuestionarioRespostas
import java.util.List; // Import necessário para List
import java.util.Random; // Import necessário para Random

public class MainTestConnection {

    public static void main(String[] args) {
        System.out.println("Iniciando teste de conexão e DAOs...");

        // Declarar variáveis de teste no início do método main para garantir escopo
        Usuario usuarioTeste1 = null;
        Paciente novoPaciente = null;
        Usuario usuarioNutricionista = null;
        Nutricionista novoNutricionista = null;

        // Objeto Random para gerar valores únicos
        Random random = new Random();

        // --- TESTE DE CONEXÃO (manter para garantir) ---
        Connection conn = null;
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Driver PostgreSQL carregado com sucesso!");
                System.out.println("Conexão com o banco de dados estabelecida!");
                System.out.println("Status da Conexão: SUCESSO!");
            } else {
                System.out.println("Status da Conexão: FALHA! Verifique os logs.");
            }
        } catch (Exception e) {
            System.err.println("Erro inesperado no teste de conexão: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        System.out.println("------------------------------------------");


        // --- TESTE DO USUARIODAO ---
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        System.out.println("\n--- Testes do UsuarioDAO (revalidação) ---");
        // Gerar CPF único e garantir 11 dígitos exatos
        String cpfPaciente = "11123" + String.format("%06d", random.nextInt(1000000));
        String emailPaciente = "maria.oliver.teste." + System.currentTimeMillis() + "@example.com";

        usuarioTeste1 = new Usuario(
            "Maria Oliveira Teste",
            cpfPaciente,
            "9998877665",
            emailPaciente,
            "senha456"
        );
        int idUsuarioTeste1Gerado = usuarioDAO.inserir(usuarioTeste1);

        if (idUsuarioTeste1Gerado != -1) {
            System.out.println("Novo usuário teste 1 (Maria) inserido com ID: " + idUsuarioTeste1Gerado);
            usuarioTeste1.setId(idUsuarioTeste1Gerado);

            Usuario usuarioEncontrado1 = usuarioDAO.buscarPorId(idUsuarioTeste1Gerado);
            if (usuarioEncontrado1 != null) {
                System.out.println("Usuário teste 1 encontrado por ID: " + usuarioEncontrado1.getNome());
            }

        } else {
            System.out.println("Falha na inserção do usuário teste 1, não é possível prosseguir com testes de Paciente/Nutricionista.");
        }
        System.out.println("--- Testes do UsuarioDAO concluídos para Usuario 1. ---");

        System.out.println("------------------------------------------");


        // --- TESTE DO PACIENTEDAO ---
        System.out.println("\n--- Testes do PacienteDAO ---");
        PacienteDAO pacienteDAO = new PacienteDAO();
        if (idUsuarioTeste1Gerado != -1) {
            novoPaciente = new Paciente(
                usuarioTeste1.getId(),
                usuarioTeste1.getNome(),
                usuarioTeste1.getCpf(),
                usuarioTeste1.getTelefone(),
                usuarioTeste1.getEmail(),
                usuarioTeste1.getSenha()
            );

            int idPacienteGerado = pacienteDAO.inserir(novoPaciente);

            if (idPacienteGerado != -1) {
                System.out.println("Novo paciente inserido com ID_Paciente: " + idPacienteGerado);
                novoPaciente.setIdPaciente(idPacienteGerado);

                System.out.println("\n--- Teste de Busca por ID_Paciente ---");
                Paciente pacienteEncontrado = pacienteDAO.buscarPorId(idPacienteGerado);
                if (pacienteEncontrado != null) {
                    System.out.println("Paciente encontrado por ID_Paciente: " + pacienteEncontrado.getIdPaciente() +
                                       " - Nome Usuário: " + pacienteEncontrado.getNome() +
                                       " - Email Usuário: " + pacienteEncontrado.getEmail());
                } else {
                    System.out.println("Paciente com ID_Paciente " + idPacienteGerado + " não encontrado.");
                }

                System.out.println("\n--- Teste de Atualização de Paciente (entrada) ---");
                if (pacienteEncontrado != null) {
                    boolean atualizado = pacienteDAO.atualizar(pacienteEncontrado);
                    if (atualizado) {
                        System.out.println("Paciente ID_Paciente " + pacienteEncontrado.getIdPaciente() + " atualizado com sucesso!");
                    } else {
                        System.out.println("Falha ao atualizar paciente ID_Paciente " + pacienteEncontrado.getIdPaciente());
                    }
                }

                System.out.println("\n--- Teste de Atualização de Dados do Usuario associado ao Paciente ---");
                // Garantir unicidade na atualização do e-mail
                usuarioTeste1.setEmail("maria.oliver.nova.teste." + System.currentTimeMillis() + "@example.com");
                boolean usuarioPacienteAtualizado = usuarioDAO.atualizar(usuarioTeste1);
                if(usuarioPacienteAtualizado) {
                    System.out.println("Email do Usuario associado ao Paciente atualizado para: " + usuarioTeste1.getEmail());
                } else {
                    System.out.println("Falha ao atualizar email do Usuario associado ao Paciente.");
                }
            } else {
                System.out.println("Falha na inserção do paciente, testes subsequentes não serão executados.");
            }
        } else {
            System.out.println("Não foi possível testar PacienteDAO porque o usuário não foi inserido.");
        }
        System.out.println("\n--- Testes de PacienteDAO concluídos. ---");

        System.out.println("------------------------------------------");


        // --- TESTES DO NUTRICIONISTADAO ---
        NutricionistaDAO nutricionistaDAO = new NutricionistaDAO();
        System.out.println("\n--- Testes do NutricionistaDAO ---");
        // Gerar CPF e email únicos para o nutricionista
        String cpfNutricionista = "99987" + String.format("%06d", random.nextInt(1000000));
        String emailNutricionista = "carlos.saude.teste." + System.currentTimeMillis() + "@clinic.com";

        usuarioNutricionista = new Usuario(
            "Dr. Carlos Saúde Teste",
            cpfNutricionista,
            "1122334455",
            emailNutricionista,
            "nutri@senha"
        );
        int idUsuarioNutricionistaGerado = usuarioDAO.inserir(usuarioNutricionista);

        if (idUsuarioNutricionistaGerado != -1) {
            System.out.println("Novo usuário nutricionista inserido com ID: " + idUsuarioNutricionistaGerado);
            usuarioNutricionista.setId(idUsuarioNutricionistaGerado);

            // Gerar CRN único
            String crnNutricionista = "CRN" + System.currentTimeMillis() % 100000;
            novoNutricionista = new Nutricionista(
                crnNutricionista,
                "Nutrição Clínica",
                usuarioNutricionista.getId(),
                usuarioNutricionista.getNome(),
                usuarioNutricionista.getCpf(),
                usuarioNutricionista.getTelefone(),
                usuarioNutricionista.getEmail(),
                usuarioNutricionista.getSenha()
            );

            int idNutricionistaGerado = nutricionistaDAO.inserir(novoNutricionista);

            if (idNutricionistaGerado != -1) {
                System.out.println("Novo nutricionista inserido com ID_Nutricionista: " + idNutricionistaGerado);
                novoNutricionista.setIdNutricionista(idNutricionistaGerado);

                Nutricionista nutricionistaEncontrado = nutricionistaDAO.buscarPorId(idNutricionistaGerado);
                if (nutricionistaEncontrado != null) {
                    System.out.println("Nutricionista encontrado por ID_Nutricionista: " + nutricionistaEncontrado.getIdNutricionista() +
                                       " - Nome Usuário: " + nutricionistaEncontrado.getNome() +
                                       " - CRN: " + nutricionistaEncontrado.getCrn());
                }

                if (nutricionistaEncontrado != null) {
                    nutricionistaEncontrado.setEspecialidade("Nutrição Esportiva e Clínica");
                    boolean atualizadoNutri = nutricionistaDAO.atualizar(nutricionistaEncontrado);
                    if (atualizadoNutri) {
                        System.out.println("Nutricionista ID_Nutricionista " + nutricionistaEncontrado.getIdNutricionista() + " atualizado com sucesso!");
                    }
                }
            } else {
                System.out.println("Falha na inserção do nutricionista.");
            }
        } else {
            System.out.println("Falha na inserção do usuário nutricionista, não é possível prosseguir com testes de Nutricionista.");
        }
        System.out.println("\n--- Testes de NutricionistaDAO concluídos. ---");

        System.out.println("------------------------------------------");


        // --- NOVO BLOCO DE TESTES DO QUESTIONARIORESPOSTASDAO ---
        QuestionarioRespostasDAO qrDAO = new QuestionarioRespostasDAO();
        System.out.println("\n--- Testes do QuestionarioRespostasDAO ---");

        if (novoPaciente != null && novoPaciente.getIdPaciente() != 0) { // Garante que temos um paciente válido
            // Criar respostas de questionário
            QuestionarioRespostas novasRespostas = new QuestionarioRespostas(
                novoPaciente.getIdPaciente(), // ID do paciente associado
                1, // pergunta1: seu diagnostico principal? (alt 1)
                2, // pergunta2: há quanto tempo tem esse diagnostico? (alt 2)
                3, // pergunta3: qual tratamento vc ta fazendo ultimamente? (alt 3)
                "Metformina e Insulina", // pergunta4: medicamentos de uso continuo? (resposta escrita)
                4, // pergunta5: principais sintomas que você apresenta? (alt 4)
                5, // pergunta6: Você possui alergias ou intolerâncias alimentares? (alt 5)
                1, // pergunta7: Você faz monitoramento de glicose? (alt 1)
                2  // pergunta8: Principal objetivo com o plano alimentar? (alt 2)
            );

            // 1. Teste de Inserção de Respostas
            int idRespostasGerado = qrDAO.inserir(novasRespostas);

            if (idRespostasGerado != -1) {
                System.out.println("Respostas do questionário inseridas com ID: " + idRespostasGerado);
                novasRespostas.setId(idRespostasGerado);

                // 2. Teste de Busca por ID das Respostas
                System.out.println("\n--- Teste de Busca por ID das Respostas ---");
                QuestionarioRespostas respostasEncontradas = qrDAO.buscarPorId(idRespostasGerado);
                if (respostasEncontradas != null) {
                    System.out.println("Respostas encontradas para ID " + respostasEncontradas.getId() +
                                       " - Paciente ID: " + respostasEncontradas.getPacienteId() +
                                       " - Pergunta 4: " + respostasEncontradas.getPergunta4());
                } else {
                    System.out.println("Respostas com ID " + idRespostasGerado + " não encontradas.");
                }

                // 3. Teste de Busca por Paciente ID
                System.out.println("\n--- Teste de Busca por Paciente ID ---");
                List<QuestionarioRespostas> respostasDoPaciente = qrDAO.buscarPorPacienteId(novoPaciente.getIdPaciente());
                if (!respostasDoPaciente.isEmpty()) {
                    System.out.println("Encontradas " + respostasDoPaciente.size() + " respostas para o Paciente ID: " + novoPaciente.getIdPaciente());
                    for (QuestionarioRespostas qr : respostasDoPaciente) {
                        System.out.println("  - Resposta ID: " + qr.getId() + " - Pergunta 4: " + qr.getPergunta4());
                    }
                } else {
                    System.out.println("Nenhuma resposta encontrada para o Paciente ID: " + novoPaciente.getIdPaciente());
                }

                // 4. Teste de Atualização de Respostas
                System.out.println("\n--- Teste de Atualização de Respostas ---");
                if (respostasEncontradas != null) {
                    respostasEncontradas.setPergunta4("Novo medicamento: Aspirina e Vitamina D"); // Atualiza uma resposta
                    boolean atualizadoQR = qrDAO.atualizar(respostasEncontradas);
                    if (atualizadoQR) {
                        System.out.println("Respostas do questionário ID " + respostasEncontradas.getId() + " atualizadas com sucesso!");
                    } else {
                        System.out.println("Falha ao atualizar respostas do questionário ID " + respostasEncontradas.getId());
                    }
                }

                // 5. Teste de Deleção de Respostas
                System.out.println("\n--- Teste de Deleção de Respostas ---");
                boolean deletadoQR = qrDAO.deletar(idRespostasGerado);
                if (deletadoQR) {
                    System.out.println("Respostas do questionário ID " + idRespostasGerado + " deletadas com sucesso!");
                    QuestionarioRespostas qrDeletado = qrDAO.buscarPorId(idRespostasGerado);
                    if (qrDeletado == null) {
                        System.out.println("Confirmação: Respostas do questionário ID " + idRespostasGerado + " não estão mais no banco de dados.");
                    }
                } else {
                    System.out.println("Falha ao deletar respostas do questionário ID " + idRespostasGerado);
                }

            } else {
                System.out.println("Falha na inserção das respostas do questionário.");
            }
        } else {
            System.out.println("Não foi possível testar QuestionarioRespostasDAO porque o paciente não foi inserido.");
        }
        System.out.println("\n--- Testes do QuestionarioRespostasDAO concluídos. ---");

        System.out.println("------------------------------------------");


        // --- LIMPEZA FINAL: Deletando Dados de Teste ---
        System.out.println("\n--- Limpeza Final: Deletando Dados de Teste ---");

        // 1. Deletar Nutricionista (filho) primeiro
        if (novoNutricionista != null && novoNutricionista.getIdNutricionista() != 0) {
            boolean nutriDeletado = nutricionistaDAO.deletar(novoNutricionista.getIdNutricionista());
            if (nutriDeletado) {
                System.out.println("Nutricionista ID " + novoNutricionista.getIdNutricionista() + " deletado com sucesso!");
            } else {
                System.out.println("Falha ao deletar Nutricionista ID " + novoNutricionista.getIdNutricionista());
            }
        }

        // 2. Deletar Paciente (filho) em seguida
        if (novoPaciente != null && novoPaciente.getIdPaciente() != 0) {
            boolean pacienteDeletado = pacienteDAO.deletar(novoPaciente.getIdPaciente());
            if (pacienteDeletado) {
                System.out.println("Paciente ID " + novoPaciente.getIdPaciente() + " deletado com sucesso!");
            } else {
                System.out.println("Falha ao deletar Paciente ID " + novoPaciente.getIdPaciente());
            }
        }

        // 3. Deletar Usuários (pai) por último
        if (usuarioNutricionista != null && usuarioNutricionista.getId() != 0) {
            boolean usuarioNutriDeletadoFinal = usuarioDAO.deletar(usuarioNutricionista.getId());
            if (usuarioNutriDeletadoFinal) {
                System.out.println("Usuário Nutricionista ID " + usuarioNutricionista.getId() + " deletado com sucesso!");
            } else {
                System.out.println("Falha ao deletar Usuário Nutricionista ID " + usuarioNutricionista.getId());
            }
        }

        if (usuarioTeste1 != null && usuarioTeste1.getId() != 0) {
            boolean usuarioPacienteDeletadoFinal = usuarioDAO.deletar(usuarioTeste1.getId());
            if (usuarioPacienteDeletadoFinal) {
                System.out.println("Usuário Paciente ID " + usuarioTeste1.getId() + " deletado com sucesso!");
            } else {
                System.out.println("Falha ao deletar Usuário Paciente ID " + usuarioTeste1.getId());
            }
        }
        System.out.println("\n--- Limpeza Final Concluída. ---");

        System.out.println("\n--- Todos os Testes de DAOs concluídos. ---");
    }
}