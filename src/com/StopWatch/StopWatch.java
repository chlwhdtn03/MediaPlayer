package com.StopWatch;

public class StopWatch {
    public static String secToTime(int sec) {
    	int seconds = sec;
    	int minutes = 0;
    	
    	for(;;) {
    		if(seconds >= 60) {
    			seconds -= 60;
    			minutes += 1;
    		} else {
    			break;
    		}
    	}
    	
    	String second = Integer.toString(seconds);
    	
    	switch(seconds) {
    	case 0:
    		second = "00";
    		break;
    	case 1:
    		second = "01";
    		break;
    	case 2:
    		second = "02";
    		break;
    	case 3:
    		second = "03";
    		break;
    	case 4:
    		second = "04";
    		break;
    	case 5:
    		second = "05";
    		break;
    	case 6:
    		second = "06";
    		break;
    	case 7:
    		second = "07";
    		break;
    	case 8:
    		second = "08";
    		break;
    	case 9:
    		second = "09";
    		break;
 
    	}
    	
    	return minutes + ":" + second;
    }
}