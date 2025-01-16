package pt.iscte.poo.utils;


public class Time implements Comparable<Time>{

    private final int min,seg;

    public Time(int seg){
        if( seg<0 )
            throw new IllegalArgumentException();
        this.min = seg/60;
        this.seg = seg%60;
    }

    public Time(String time){
        if ( time == null || !time.matches("^\\d+:\\d{2}$"))
            throw new IllegalArgumentException("Time must be in the format \"mm:ss\"");
        String[] s = time.split(":");
        int min = Integer.parseInt(s[0]);
        int seg = Integer.parseInt(s[1]);
        if( min < 0 || seg>59 || seg<0 )
            throw new IllegalArgumentException();
        this.min = min;
        this.seg = seg;
    }

    public static Time createTime(String time){
        try{
            Time t = new Time(time);
            return t;
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    public String toString(){
        if ( seg > 9 )
            return min+":"+seg;
        return min+":0"+seg;
    }

    public int getMin(){
        return min;
    }

    public int getSeg(){
        return seg;
    }

    public int getTotalSeg(){
        return min*60+seg;
    }

    @Override
     public int compareTo(Time o) {
         return this.getTotalSeg()-o.getTotalSeg();
     }

    @Override
    public boolean equals(Object t) {
    	if ( t instanceof Time)
    		return hashCode() == t.hashCode();
    	return false;
    }

    @Override
    public int hashCode() {
    	return getTotalSeg();
    }
}

