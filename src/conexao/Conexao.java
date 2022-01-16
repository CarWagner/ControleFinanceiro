package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

	public Connection getConexao() {
		try {
			// Estabelecendo uma conex�o
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/gestaoFinanceira",
					"root",
					"Wart682759."
			);
			return conn;
		} catch(Exception e) {
			// Se a conex�o falhar...
			System.out.println("Erro ao conectar" + e.getMessage());
			return null;
		}
	}
	
}
