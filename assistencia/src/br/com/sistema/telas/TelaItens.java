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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author rayon
 */
public class TelaItens extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaItens
     */
    public TelaItens() {
        initComponents();
    }

    /**
     * Método responsável por adicionar um novo produto no banco de dados
     */
    private void salvarProduto() throws ClassNotFoundException {
        String sql = "insert into sistema.produto(nome,precocompra,quant,provenda) values(?,?,?,?)";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProd.getText());
            pst.setString(2, txtCompra.getText());
            pst.setString(3, txtQuant.getText());
            pst.setString(4, txtVenda1.getText());
            if ((txtProd.getText().isEmpty()) || (txtQuant.getText().isEmpty()) || (txtVenda1.getText()).isEmpty() || (txtCompra.getText()).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    recuperarProduto();
                    JOptionPane.showMessageDialog(null, "Registro inserido.");
                    txtCod.setEnabled(false);
                    txtProd.setEnabled(false);
                    txtCompra.setEnabled(false);
                    txtQuant.setEnabled(false);
                    txtVenda1.setEnabled(false);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método responsável pela edição dos dados do produto
     */
    private void editarProduto() {

        String sql = "update sistema.produto set nome=?, precocompra=?, quant=?, provenda=? where cod=?";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProd.getText());
            pst.setString(2, txtCompra.getText());
            pst.setString(3, txtQuant.getText());
            pst.setString(4, txtVenda1.getText());
            pst.setInt(5, Integer.parseInt(txtCod.getText()));
            if ((txtProd.getText().isEmpty()) || (txtQuant.getText().isEmpty()) || (txtVenda1.getText()).isEmpty() || (txtCompra.getText()).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preenche todos os campos obrigatórios");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Registro inserido.");
                    limpar();
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

        int setar = tbProd.getSelectedRow();
        txtCod.setText(tbProd.getModel().getValueAt(setar, 0).toString());
        txtProd.setText(tbProd.getModel().getValueAt(setar, 1).toString());
        txtCompra.setText(tbProd.getModel().getValueAt(setar, 2).toString());
        txtQuant.setText(tbProd.getModel().getValueAt(setar, 3).toString());
        txtVenda1.setText(tbProd.getModel().getValueAt(setar, 4).toString());
        btnProdNovo.setEnabled(false);
        btnProdEditar.setEnabled(true);
        btnProddDeletar.setEnabled(true);
    }

    /**
     * Método responsável pela pesquisa do produto pelo o nome no campo
     * pesquisar
     */
    private void pesquisar_produto() throws ClassNotFoundException {

        String sql = "select cod , nome, precocompra, quant, provenda from sistema.produto where nome like ?";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdutopesquisa.getText() + "%");
            rs = pst.executeQuery();
            tbProd.setModel(DbUtils.resultSetToTableModel(rs));
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
     * Método responsável para excluir um produto
     */
    private void excluirProduto() throws ClassNotFoundException {
        int confirma = JOptionPane.showConfirmDialog(null, "Confima a exclusão deste produto?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from sistema.produto where cod=?";
            try {
                conexao = ModuloConexao.conectar();
                pst = conexao.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtCod.getText()));
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto removido com sucesso");
                    limpar();
                }

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

        txtCod.setText(null);
        txtProd.setText(null);
        txtProdutopesquisa.setText(null);
        ((DefaultTableModel) tbProd.getModel()).setRowCount(0);
        txtCompra.setText(null);
        txtQuant.setText(null);
        txtVenda1.setText(null);
        btnProdNovo.setEnabled(true);
        btnProdEditar.setEnabled(true);
        btnProddDeletar.setEnabled(true);

    }

    /**
     * Método responsável para recuperar o ID do produto
     */
    private void recuperarProduto() throws ClassNotFoundException {
        String sql = "select max(cod) from sistema.produto";
        try {
            conexao = ModuloConexao.conectar();
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtCod.setText(rs.getString(1));
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel13 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtProd = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCompra = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtQuant = new javax.swing.JTextField();
        txtVenda1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProd = new javax.swing.JTable();
        btnProddDeletar = new javax.swing.JButton();
        btnProdEditar = new javax.swing.JButton();
        btnProdNovo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtProdutopesquisa = new javax.swing.JTextField();

        setBackground(new java.awt.Color(204, 204, 204));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(1024, 592));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setBackground(new java.awt.Color(0, 102, 102));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("CADASTRO PRODUTO");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 11, 990, -1));

        txtCod.setEditable(false);
        txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodActionPerformed(evt);
            }
        });
        getContentPane().add(txtCod, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 85, 50, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("ID");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 86, 21, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Produto");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 137, -1, -1));
        getContentPane().add(txtProd, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 136, 260, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Preço de Compra");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 189, -1, -1));
        getContentPane().add(txtCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 188, 260, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Quantidade");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 137, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Preço de Venda");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 189, -1, -1));

        txtQuant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantActionPerformed(evt);
            }
        });
        getContentPane().add(txtQuant, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 136, 151, -1));
        getContentPane().add(txtVenda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 188, 260, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbProd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Produto", "Quantidade", "Preço"
            }
        ));
        tbProd.setGridColor(new java.awt.Color(255, 255, 255));
        tbProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProd);

        btnProddDeletar.setBackground(new java.awt.Color(0, 102, 102));
        btnProddDeletar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProddDeletar.setForeground(new java.awt.Color(255, 255, 255));
        btnProddDeletar.setText("Deletar");
        btnProddDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProddDeletarActionPerformed(evt);
            }
        });

        btnProdEditar.setBackground(new java.awt.Color(0, 102, 102));
        btnProdEditar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProdEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnProdEditar.setText("Editar");
        btnProdEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdEditarActionPerformed(evt);
            }
        });

        btnProdNovo.setBackground(new java.awt.Color(0, 102, 102));
        btnProdNovo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProdNovo.setForeground(new java.awt.Color(255, 255, 255));
        btnProdNovo.setText("Salvar");
        btnProdNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdNovoActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sistema/imagens/pesquisar.png"))); // NOI18N

        txtProdutopesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProdutopesquisaActionPerformed(evt);
            }
        });
        txtProdutopesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProdutopesquisaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 922, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(btnProdNovo)
                .addGap(37, 37, 37)
                .addComponent(btnProdEditar)
                .addGap(42, 42, 42)
                .addComponent(btnProddDeletar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProdutopesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnProdNovo)
                        .addComponent(btnProdEditar)
                        .addComponent(btnProddDeletar))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtProdutopesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(260, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtProdutopesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProdutopesquisaKeyReleased
        try {
            pesquisar_produto();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaItens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtProdutopesquisaKeyReleased

    private void txtProdutopesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProdutopesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProdutopesquisaActionPerformed

    private void btnProddDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProddDeletarActionPerformed
        try {
            excluirProduto();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaItens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProddDeletarActionPerformed

    private void btnProdEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdEditarActionPerformed
        editarProduto();
    }//GEN-LAST:event_btnProdEditarActionPerformed

    private void btnProdNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdNovoActionPerformed
        try {
            salvarProduto();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaItens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProdNovoActionPerformed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtQuantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantActionPerformed

    private void tbProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProdMouseClicked
        setarCampos();
    }//GEN-LAST:event_tbProdMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProdEditar;
    private javax.swing.JButton btnProdNovo;
    private javax.swing.JButton btnProddDeletar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbProd;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtCompra;
    private javax.swing.JTextField txtProd;
    private javax.swing.JTextField txtProdutopesquisa;
    private javax.swing.JTextField txtQuant;
    private javax.swing.JTextField txtVenda1;
    // End of variables declaration//GEN-END:variables
}
