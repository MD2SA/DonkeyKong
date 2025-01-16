package pt.iscte.poo.game;

public class Main {

	public static void main(String[] args) {
		String file1 = null;
		if( args.length > 0 )
			file1 = args[0];
		GameEngine.getInstance().startGame(file1);
	}

}
