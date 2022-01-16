package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

	public Connection getConexao() {
		try {
			// Estabelecendo uma conexão
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/gestaoFinanceira",
					"root",
					"Wart682759."
			);
			return conn;
		} catch(Exception e) {
			// Se a conexão falhar...
			System.out.println("Erro ao conectar" + e.getMessage());
			return null;
		}
	}
	
}
