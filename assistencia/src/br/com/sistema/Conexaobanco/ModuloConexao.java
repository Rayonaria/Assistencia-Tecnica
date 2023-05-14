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
package br.com.sistema.Conexaobanco;

import java.sql.*;

/**
 * Conexão com o banco de dados postgresql
 *
 * @author rayon
 */
public class ModuloConexao {

    static final String URL = "jdbc:postgresql://localhost:5432/Tecnica"; 
    static final String USER = "postgres"; 
    static final String PASS = ""; 
    /**
     * @return conecta Método responsável em conecta o java com o banco de dados
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection conectar() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection conecta = DriverManager.getConnection(URL, USER, PASS);
        if (conecta != null) {
            System.out.print("Conexão efetuada com sucesso...");
            return conecta;
        } else {
            System.out.print("Conexão não efetuada...");
        }
        return null;
    }
}
