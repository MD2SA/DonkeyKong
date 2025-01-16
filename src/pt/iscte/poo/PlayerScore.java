package pt.iscte.poo;

import pt.iscte.poo.utils.Time;

public class PlayerScore implements Comparable<PlayerScore> {

    private final String player;
    private final Time time;

	public PlayerScore(String player, Time time) {
        this.player = player;
        this.time = time;
	}

    @Override
    public String toString(){
        return player+";"+time;
    }

    public String getPlayer(){
        return player;
    }

    public Time getTime(){
        return time;
    }

    @Override
    public int compareTo(PlayerScore s){
        return time.compareTo(s.getTime());
    }

    public static PlayerScore readFrom(String line){
        String[] args = line.trim().split(";");
        if( args.length != 2 ) return null;

        String player = args[0];

        Time time = Time.createTime(args[1]);
        if( time == null ) time = new Time(0);

        return new PlayerScore(player, time);
    }

}
