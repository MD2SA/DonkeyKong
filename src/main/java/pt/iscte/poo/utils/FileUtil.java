package pt.iscte.poo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.PlayerScore;

public class FileUtil {


	public static File askUserFile() {
        //do not close scanner
		Scanner scanner = new Scanner(System.in);
		System.out.println("Insert full path to the file");
		if( scanner.hasNextLine()) {
			String filePath = scanner.nextLine().trim();
			return filePath==null? null : new File(filePath);
		}
		return null;
	}

    public static String getCompleteDir(String filePath){
        if( filePath == null ) return "";
        int index = filePath.length()-1;
        while( index>=0 && filePath.charAt(index) != '/' )
            index--;
        return filePath.substring(0, index+1);
    }

    public static List<PlayerScore> getSavedScores(File file){
        List<PlayerScore> list = new ArrayList<>();

        try(Scanner scanner = new Scanner(file) ){
            while( scanner.hasNextLine() ) {
                String line = scanner.nextLine();
                PlayerScore ps = PlayerScore.readFrom(line);
                if( ps != null)
                	list.add(ps);
            }
        } catch( FileNotFoundException e){
            return list;
        }
        return list;
    }

    public static void savePlayerScore(PlayerScore playerScore, String roomsDir) {
        File directory = new File("scores/"+roomsDir);
        if (!directory.exists()) {
            //System.out.println("Creating new directory: "+directory.getName());
            directory.mkdirs(); //creates dirs if doesnt exists
        }
        File file = new File("scores/"+roomsDir+"best10.txt");

        List<PlayerScore> scores = getSavedScores(file);
        scores.add(playerScore);
        //order with comparable of PlayerScore
        scores.sort(null);

        try(PrintWriter writer = new PrintWriter(file) ) {
            for( int i = 0; i< Math.min(10, scores.size()); i++){
                writer.println(scores.get(i));
            }
        } catch ( FileNotFoundException e ) {
            throw new RuntimeException();
        }
    }
}
