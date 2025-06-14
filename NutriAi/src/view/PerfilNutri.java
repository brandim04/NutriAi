package view;

import model.Nutricionista;
import javax.swing.JFrame;
import javax.swing.JLabel;
import model.Paciente; // Importar a classe Paciente
import java.util.List; // Importar List
import javax.swing.table.DefaultTableModel;
import dao.NutricionistaDAO; // Importar o DAO do nutricionista 

/**
 *
 * @author brand
 */
public class PerfilNutri extends javax.swing.JFrame {
    
    private Nutricionista nutricionistaLogado;
    private DefaultTableModel modeloTabelaPacientes;
    private NutricionistaDAO nutricionistaDAO;

    /**
     * Creates new form PerfilNutri
     */
    public PerfilNutri() {
        initComponents();
        this.nutricionistaDAO = new NutricionistaDAO();
        // Configurações básicas da tabela, caso este construtor seja usado
        modeloTabelaPacientes = (DefaultTableModel) tbPacientes.getModel();
        modeloTabelaPacientes.setColumnCount(0);
        modeloTabelaPacientes.addColumn("Nome do Paciente");
        setLocationRelativeTo(null);
    }

    public PerfilNutri(Nutricionista nutricionista) { 
        this.nutricionistaLogado = nutricionista; 
        initComponents(); 
        this.nutricionistaDAO = new NutricionistaDAO();

        setLocationRelativeTo(null);
        
         modeloTabelaPacientes = (DefaultTableModel) tbPacientes.getModel();

        // 2. MUITO IMPORTANTE: Limpar TODAS as colunas existentes ANTES de adicionar as novas.
        // Isso sobrescreve qualquer coluna definida no NetBeans Designer.
        modeloTabelaPacientes.setColumnCount(0); 

        // 3. Adicionar APENAS a coluna "Nome do Paciente"
        modeloTabelaPacientes.addColumn("Nome do Paciente"); 
        // --- FIM DO REFORÇO NA CONFIGURAÇÃO DA TABELA ---

        if (jLabel3 != null && nutricionistaLogado != null) {
            jLabel3.setText("Dr(a). " + nutricionistaLogado.getNome());
        }
        if (nutricionistaLogado != null && nutricionistaLogado.getIdNutricionista() > 0) {
            try {
                // Busca os pacientes usando o ID do nutricionista logado
                List<Paciente> pacientesDoNutricionista = nutricionistaDAO.buscarPacientesDoNutricionista(nutricionistaLogado.getIdNutricionista());
                // Popula a tabela com os pacientes encontrados
                popularTabelaPacientes(pacientesDoNutricionista);
                System.out.println("Pacientes carregados para nutricionista " + nutricionistaLogado.getNome() + ": " + pacientesDoNutricionista.size());
            } catch (Exception e) {
                System.err.println("Erro ao carregar pacientes na tela do nutricionista: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Nutricionista logado ou ID do nutricionista inválido. Não foi possível carregar pacientes.");
        }
    }

    // --- Método para popular a tabela ---
    public void popularTabelaPacientes(List<Paciente> pacientes) {
        // Este bloco garante que o modelo esteja configurado mesmo se popularTabelaPacientes for chamado de outra forma.
        // Para seu caso, o construtor já faz isso, mas é uma camada extra de segurança.
        if (modeloTabelaPacientes == null || modeloTabelaPacientes.getColumnCount() == 0) {
            modeloTabelaPacientes = (DefaultTableModel) tbPacientes.getModel();
            modeloTabelaPacientes.setColumnCount(0); 
            modeloTabelaPacientes.addColumn("Nome do Paciente"); 
        }

        modeloTabelaPacientes.setRowCount(0); // Limpa as linhas existentes

        for (Paciente paciente : pacientes) {
            modeloTabelaPacientes.addRow(new Object[]{
                paciente.getNome() // Adiciona APENAS o nome
            });
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbPacientes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(0, 204, 153));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/woman.png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Dra. Maria de Fátima");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(14, 14, 14)))
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbPacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Nome paciente"
            }
        ));
        jScrollPane2.setViewportView(tbPacientes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PerfilNutri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PerfilNutri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PerfilNutri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PerfilNutri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PerfilNutri().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbPacientes;
    // End of variables declaration//GEN-END:variables
}
