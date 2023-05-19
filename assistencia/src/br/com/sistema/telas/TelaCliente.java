/*
 * The MIT License
 *
 * Copyright 2023 Audeniza Rayonaria.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.sistema.telas;

import br.com.sistema.Conexaobanco.ModuloConexao;
import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Tela de Cadastro Cliente
 *
 * @author rayon
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Criação de um novo formulario de Cliente
     */
    public TelaCliente() throws ClassNotFoundException, SQLException {
        initComponents();
    }

    /**
     * Método responsável por adicionar um novo cliente no banco de dados
     */
    private void novoCliente() throws ClassNotFoundException {
        String sql = "insert into sistema.cliente(nomecli,endcli,fonecli,emailcli,bairrocli,cidadecli) values(?,?,?,?,?,?)";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtClienteNome.getText());
            pst.setString(2, txtClienteEnd.getText());
            pst.setString(3, txtClienteFone.getText());
            pst.setString(4, txtClienteEmail.getText());
            pst.setString(5, txtClienteBairro.getText());
            pst.setString(6, txtClienteCid.getText());
            if (txtClienteEmail.getText().equals("")) {
                pst.setString(4, null);
            } else {
                pst.setString(4, txtClienteEmail.getText());
            }
            if ((txtClienteNome.getText().isEmpty()) || (txtClienteFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    recuperarCliente();
                    JOptionPane.showMessageDialog(null, "Registro Inserido!");
                    limpar();
                }
            }
        } catch (SQLIntegrityConstraintViolationException e1) {
            JOptionPane.showMessageDialog(null, "Email já existente.\nEscolha outro email.");
            txtClienteEmail.setText(null);
            txtClienteEmail.requestFocus();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     * Método responsável pela edição dos dados do cliente
     */
    private void editar() {

        String sql = "update sistema.cliente set nomecli=?, endcli=?, fonecli=?, emailcli =?, bairrocli=?, cidadecli=? where idcli=?";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtClienteNome.getText());
            pst.setString(2, txtClienteEnd.getText());
            pst.setString(3, txtClienteFone.getText());
            pst.setString(4, txtClienteEmail.getText());
            pst.setString(5, txtClienteBairro.getText());
            pst.setString(6, txtClienteCid.getText());
            pst.setInt(7, Integer.parseInt(txtCliId.getText()));
            if ((txtClienteNome.getText().isEmpty()) || (txtClienteFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preenche todos os campos obrigatórios");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Registro inserido.");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método utilizado para setar os campos de texto com o conteúdo da tabela
     */
    private void setarCampos() {
        int setar = tblcliente.getSelectedRow();
        txtCliId.setText(tblcliente.getModel().getValueAt(setar, 0).toString());
        txtClienteNome.setText(tblcliente.getModel().getValueAt(setar, 1).toString());
        txtClienteEnd.setText(tblcliente.getModel().getValueAt(setar, 2).toString());
        txtClienteFone.setText(tblcliente.getModel().getValueAt(setar, 3).toString());
        txtClienteEmail.setText(tblcliente.getModel().getValueAt(setar, 4).toString());
        txtClienteBairro.setText(tblcliente.getModel().getValueAt(setar, 5).toString());
        txtClienteCid.setText(tblcliente.getModel().getValueAt(setar, 6).toString());

        btnClienteNovo.setEnabled(false);
        btnClienteEditar.setEnabled(true);
        btnClienteDeletar.setEnabled(true);
    }

    /**
     * Método responsável pela pesquisa de clientes pelo o nome no campo
     * pesquisar
     */
    private void pesquisar_cliente() throws ClassNotFoundException {

        String sql = "select idcli as id, nomecli as nome, endcli as endereço, fonecli as fone, emailcli as email, bairrocli as bairro, cidadecli as cidade from sistema.cliente where nomecli like ?";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtClipesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblcliente.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     * Método responsável para excluir um cliente
     */
    private void excluirCliente() throws ClassNotFoundException {
        int confirma = JOptionPane.showConfirmDialog(null, "Confima a exclusão deste cliente?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from sistema.cliente where idcli=?";
            try {
                conexao = ModuloConexao.conectar();
                pst = conexao.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtCliId.getText()));
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                }
            } catch (SQLIntegrityConstraintViolationException e1) {
                JOptionPane.showMessageDialog(null, "Exclusão não realizada.\nCliente possui OS pendente.");
            } catch (HeadlessException | SQLException e2) {
                JOptionPane.showMessageDialog(null, e2);

            } finally {
                try {
                    conexao.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
    }

    /**
     * Método responsável por limpar os campos e gerenciar os componentes
     */
    private void limpar() {

        txtClienteNome.setText(null);
        txtClienteEnd.setText(null);
        txtClipesquisar.setText(null);
        ((DefaultTableModel) tblcliente.getModel()).setRowCount(0);
        txtClienteFone.setText(null);
        txtClienteEmail.setText(null);
        txtClienteBairro.setText(null);
        txtClienteCid.setText(null);
        btnClienteNovo.setEnabled(true);
        btnClienteEditar.setEnabled(true);
        btnClienteDeletar.setEnabled(true);

    }
/**
     * Método responsável para recuperar o ID do Usuário
     */
    private void recuperarCliente() throws ClassNotFoundException {
        String sql = "select max(idcli) from sistema.cliente";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtCliId.setText(rs.getString(1));
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtClienteEnd = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtClienteFone = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtClienteNome = new javax.swing.JTextField();
        txtClienteCid = new javax.swing.JTextField();
        txtClienteBairro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtClienteEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnClienteNovo = new javax.swing.JButton();
        btnClienteEditar = new javax.swing.JButton();
        btnClienteDeletar = new javax.swing.JButton();
        txtClipesquisar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcliente = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jLabel8.setText("jLabel8");

        jLabel10.setText("jLabel10");

        jLabel11.setText("jLabel11");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(0, 0));
        setNormalBounds(new java.awt.Rectangle(0, 0, 128, 0));
        setPreferredSize(new java.awt.Dimension(939, 497));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(txtClienteEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 262, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("* Fone");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, -1, -1));
        getContentPane().add(txtClienteFone, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 262, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("* Nome");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("ID");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 40, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Endereço");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, -1, -1));
        getContentPane().add(txtClienteNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 260, -1));

        txtClienteCid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteCidActionPerformed(evt);
            }
        });
        getContentPane().add(txtClienteCid, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 262, -1));
        getContentPane().add(txtClienteBairro, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 262, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("* Email");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 210, -1, -1));

        txtClienteEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteEmailActionPerformed(evt);
            }
        });
        getContentPane().add(txtClienteEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 261, -1));

        jLabel12.setBackground(new java.awt.Color(0, 102, 102));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("CADASTRO CLIENTE");
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 11, 990, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnClienteNovo.setBackground(new java.awt.Color(0, 102, 102));
        btnClienteNovo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClienteNovo.setForeground(new java.awt.Color(255, 255, 255));
        btnClienteNovo.setText("Salvar");
        btnClienteNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteNovoActionPerformed(evt);
            }
        });

        btnClienteEditar.setBackground(new java.awt.Color(0, 102, 102));
        btnClienteEditar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClienteEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnClienteEditar.setText("Editar");
        btnClienteEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteEditarActionPerformed(evt);
            }
        });

        btnClienteDeletar.setBackground(new java.awt.Color(0, 102, 102));
        btnClienteDeletar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClienteDeletar.setForeground(new java.awt.Color(255, 255, 255));
        btnClienteDeletar.setText("Deletar");
        btnClienteDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteDeletarActionPerformed(evt);
            }
        });

        txtClipesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClipesquisarActionPerformed(evt);
            }
        });
        txtClipesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtClipesquisarKeyReleased(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/imagens/pesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClienteNovo)
                .addGap(37, 37, 37)
                .addComponent(btnClienteEditar)
                .addGap(42, 42, 42)
                .addComponent(btnClienteDeletar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtClipesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClienteNovo)
                        .addComponent(btnClienteEditar)
                        .addComponent(btnClienteDeletar)
                        .addComponent(txtClipesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 690, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblcliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tblcliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Cidade", "Endereço", "Fone", "E-mail"
            }
        ));
        tblcliente.setGridColor(new java.awt.Color(255, 255, 255));
        tblcliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblclienteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblcliente);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 700, 190));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText(" Cidade");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, -1, -1));

        txtCliId.setEditable(false);
        txtCliId.setBackground(new java.awt.Color(204, 204, 204));
        txtCliId.setOpaque(false);
        getContentPane().add(txtCliId, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, 50, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/imagens/cliente.png"))); // NOI18N
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 76, 93, 103));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Bairro");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(544, 163, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        setBounds(0, 0, 1024, 583);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClienteNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteNovoActionPerformed
        try {
            novoCliente();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClienteNovoActionPerformed

    private void btnClienteEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnClienteEditarActionPerformed

    private void btnClienteDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteDeletarActionPerformed
        try {
            excluirCliente();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClienteDeletarActionPerformed

    private void txtClipesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClipesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClipesquisarActionPerformed

    private void txtClienteCidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteCidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteCidActionPerformed

    private void txtClienteEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteEmailActionPerformed

    private void txtClipesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClipesquisarKeyReleased
        try {
            pesquisar_cliente();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtClipesquisarKeyReleased

    private void tblclienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblclienteMouseClicked
        setarCampos();
    }//GEN-LAST:event_tblclienteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClienteDeletar;
    private javax.swing.JButton btnClienteEditar;
    private javax.swing.JButton btnClienteNovo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblcliente;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtClienteBairro;
    private javax.swing.JTextField txtClienteCid;
    private javax.swing.JTextField txtClienteEmail;
    private javax.swing.JTextField txtClienteEnd;
    private javax.swing.JTextField txtClienteFone;
    private javax.swing.JTextField txtClienteNome;
    private javax.swing.JTextField txtClipesquisar;
    // End of variables declaration//GEN-END:variables
}
